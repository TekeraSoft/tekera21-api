package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.SellerAdminDto;
import com.tekerasoft.tekeramarketplace.dto.SellerReportAggregation;
import com.tekerasoft.tekeramarketplace.dto.request.CreateSellerRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateSellerRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.CompanyException;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.exception.SellerVerificationException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItemType;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import com.tekerasoft.tekeramarketplace.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final SearchItemService searchItemService;
    private final ShippingCompanyService shippingCompanyService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final SellerVerificationService sellerVerificationService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public SellerService(SellerRepository sellerRepository, CategoryRepository categoryRepository,
                         FileService fileService, SearchItemService searchItemService,
                         ShippingCompanyService shippingCompanyService, UserService userService,
                         AuthenticationFacade authenticationFacade, SellerVerificationService sellerVerificationService,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.searchItemService = searchItemService;
        this.shippingCompanyService = shippingCompanyService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.sellerVerificationService = sellerVerificationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public ApiResponse<?> createSeller(CreateSellerRequest req, List<MultipartFile> files, MultipartFile logo) {

        if(sellerRepository.existsByNameAndTaxNumber(req.getName(),req.getTaxNumber())) {
            throw new CompanyException("Company already exists");
        }
        try {
            User user = userService.getUserInformation(authenticationFacade.getCurrentUserId());
            userService.setApprovalSellerRole(user);
            Set<ShippingCompany> shippingCompanySet = new HashSet<>();
            for(String sc: req.getShippingCompanies()) {
                ShippingCompany shippingCompany = shippingCompanyService.getShippingCompany(sc);
                shippingCompanySet.add(shippingCompany);
            }

            Seller seller = new Seller();
            seller.setName(req.getName());
            seller.setSlug(SlugGenerator.generateSlug(seller.getName()));
            seller.setEmail(req.getEmail());
            seller.setGsmNumber(req.getGsmNumber());
            seller.setAlternativePhoneNumber(req.getAlternativePhoneNumber());
            seller.setSupportPhoneNumber(req.getSupportPhoneNumber());
            seller.setTaxNumber(req.getTaxNumber());
            seller.setTaxOffice(req.getTaxOffice());
            seller.setMerisNumber(req.getMerisNumber());
            seller.setRegistrationDate(req.getRegistrationDate());
            seller.setContactPersonNumber(req.getContactPersonNumber());
            seller.setContactPersonTitle(req.getContactPersonTitle());
            seller.setAddress(req.getAddress());
            seller.setBankAccounts(req.getBankAccount());
            seller.setShippingCompanies(shippingCompanySet);
            seller.setActive(false);
            seller.setUsers(Set.of(user));
            // Category
            Set<Category> categoryList = req.getCategoryId()
                    .stream()
                    .map(id-> categoryRepository.findById(UUID.fromString(id))
                            .orElseThrow()).collect(Collectors.toSet());
            seller.setCategories(categoryList);

            String companyReplaceName = seller.getName().toLowerCase().replaceAll("\\s+", "_");

            String logoPath = fileService.folderFileUpload(logo, String.format("/company/logo/%s", companyReplaceName));
            if(logoPath != null) {
                seller.setLogo(logoPath);
            } else {
                throw new CompanyException("Logo could not be loaded");
            }

            List<SellerDocument> sellerDocuments = new ArrayList<>();

            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();

                // Dosya adından uzantıyı çıkar: imzasurkusu.pdf -> imzasurkusu
                assert originalFilename != null;
                String documentTitle = originalFilename.contains(".")
                        ? originalFilename.substring(0, originalFilename.lastIndexOf('.'))
                        : originalFilename;

                // Dosyayı MinIO'ya yükle ve path'ini al
                String documentPath = fileService.folderFileUpload(file,
                        String.format("/company/documents/%s", companyReplaceName));

                com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType documentTitleEnum =
                        com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType.valueOf(documentTitle.toUpperCase());

                // CompanyDocument nesnesi oluştur
                SellerDocument document = new SellerDocument(
                        documentTitleEnum,
                        documentPath,
                        VerificationStatus.PENDING,
                        null
                );
                sellerDocuments.add(document);
            }

            // Bu satır selerDocuments listesini company entity’sine kaydeder
            seller.setSellerDocuments(sellerDocuments);
            seller.setVerificationStatus(VerificationStatus.PENDING);

            // Seller kaydını veritabanına kaydet
            Seller savedSeller = sellerRepository.save(seller);
            sellerVerificationService.assignToSupervisorSeller(user.getId().toString(),savedSeller);
            return new ApiResponse<>("Create Company", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ApiResponse<?> updateSeller(UpdateSellerRequest req,
                                       List<MultipartFile> files, MultipartFile logo) {

        Seller seller = sellerRepository.findById(UUID.fromString(req.getId()))
                .orElseThrow(() -> new NotFoundException("Seller not found"));

        seller.setName(req.getName());
        seller.setSlug(SlugGenerator.generateSlug(seller.getName()));
        seller.setEmail(req.getEmail());
        seller.setGsmNumber(req.getGsmNumber());
        seller.setAlternativePhoneNumber(req.getAlternativePhoneNumber());
        seller.setSupportPhoneNumber(req.getSupportPhoneNumber());
        seller.setTaxNumber(req.getTaxNumber());
        seller.setTaxOffice(req.getTaxOffice());
        seller.setMerisNumber(req.getMerisNumber());
        seller.setRegistrationDate(req.getRegistrationDate());
        seller.setContactPersonNumber(req.getContactPersonNumber());
        seller.setContactPersonTitle(req.getContactPersonTitle());
        seller.setAddress(req.getAddress());
        seller.setBankAccounts(req.getBankAccount());

        if (req.getShippingCompanies() != null) {
            Set<ShippingCompany> shippingCompanySet = req.getShippingCompanies().stream()
                    .map(sc -> shippingCompanyService.getShippingCompany(sc))
                    .collect(Collectors.toSet());
            seller.setShippingCompanies(shippingCompanySet);
        }

        if (req.getCategoryId() != null) {
            Set<Category> categoryList = req.getCategoryId().stream()
                    .map(id -> categoryRepository.findById(UUID.fromString(id))
                            .orElseThrow(() -> new NotFoundException("Category not found")))
                    .collect(Collectors.toSet());
            seller.setCategories(categoryList);
        }

        String companyReplaceName = seller.getName().toLowerCase().replaceAll("\\s+", "_");

        // Logo güncelle
        if (logo != null && !logo.isEmpty()) {
            String logoPath = fileService.folderFileUpload(logo,
                    String.format("/company/logo/%s", companyReplaceName));
            if (logoPath != null) {
                seller.setLogo(logoPath);
            } else {
                throw new CompanyException("Logo could not be loaded");
            }
        }

        // Belgeleri güncelle
        if (files != null && !files.isEmpty()) {
            List<MultipartFile> nonEmptyFiles = files.stream()
                    .filter(f -> f != null && !f.isEmpty())
                    .toList();

            if (!nonEmptyFiles.isEmpty()) {
                List<SellerDocument> existingDocs = new ArrayList<>(seller.getSellerDocuments());
                Map<com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType, SellerDocument> docMap =
                        existingDocs.stream().collect(Collectors.toMap(SellerDocument::getDocumentTitle, d -> d));

                List<String> oldPathsToDelete = new ArrayList<>();

                for (MultipartFile file : nonEmptyFiles) {
                    String originalFilename = file.getOriginalFilename();
                    assert originalFilename != null;

                    String documentTitle = originalFilename.contains(".")
                            ? originalFilename.substring(0, originalFilename.lastIndexOf('.'))
                            : originalFilename;

                    com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType documentTitleEnum;
                    try {
                        documentTitleEnum = com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType
                                .valueOf(documentTitle.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new CompanyException("Geçersiz belge tipi: " + documentTitle);
                    }

                    String newDocumentPath = fileService.folderFileUpload(file,
                            String.format("/company/documents/%s", companyReplaceName));

                    // Eğer eski belge varsa, path değişmişse silinmek üzere işaretle
                    if (docMap.containsKey(documentTitleEnum)) {
                        SellerDocument oldDoc = docMap.get(documentTitleEnum);
                        if (!oldDoc.getDocumentPath().equals(newDocumentPath)) {
                            oldPathsToDelete.add(oldDoc.getDocumentPath());
                        }
                        // Güncelle
                        oldDoc.setDocumentPath(newDocumentPath);
                        oldDoc.setVerificationStatus(VerificationStatus.PENDING);
                    } else {
                        // Yeni belge ekleniyor
                        SellerDocument newDoc = new SellerDocument(
                                documentTitleEnum,
                                newDocumentPath,
                                VerificationStatus.PENDING,
                                null
                        );
                        existingDocs.add(newDoc);
                    }
                }

                // Kafka ile silinecek dosyaları gönder
                if (!oldPathsToDelete.isEmpty()) {
                    kafkaTemplate.send("delete-image-processing", oldPathsToDelete);
                }

                // Mevcut seller belgelerini set et
                seller.setSellerDocuments(existingDocs);
            }
        }

        seller.setVerificationStatus(VerificationStatus.PENDING);
        sellerRepository.save(seller);

        return new ApiResponse<>("Seller updated successfully", HttpStatus.OK.value());
    }

    // TODO seller document status change method - call the active method status if verified seller activation true
    public ApiResponse<?> changeStatusFaultyDocument(String sellerId,
                                                     com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType documentName ,
                                                     VerificationStatus status)
    {
        Seller seller = sellerRepository.findById(UUID.fromString(sellerId))
                .orElseThrow(() -> new NotFoundException("Seller not found"));
        seller.getSellerDocuments().forEach(path -> {
            if(documentName.equals(path.getDocumentTitle())) {
                path.setVerificationStatus(status);
            }
        });
        sellerRepository.save(seller);
        return new ApiResponse<>("Changed document status", HttpStatus.OK.value());
    }

    public ApiResponse<?> sellerActivation(String sellerId) {
        Seller seller =  sellerRepository.findById(UUID.fromString(sellerId))
                .orElseThrow(() -> new NotFoundException("Seller not found"));
        // Enum’daki tüm belgeler
        // List<SellerDocumentType> allRequired = Arrays.asList(SellerDocumentType.values());

        List<SellerDocument> findNotVerifiedSellerDocuments = sellerRepository.findUnverifiedDocumentsBySeller(
                UUID.fromString(sellerId),
                VerificationStatus.VERIFIED
        );
        if(findNotVerifiedSellerDocuments.isEmpty() && !seller.getMerisNumber().isEmpty()) {
            seller.setActive(true);
            seller.setVerified(true);
            seller.setVerificationStatus(VerificationStatus.VERIFIED);
            sellerRepository.save(seller);
            userService.attachSellerRole(seller.getUsers().stream().findFirst().get());
            return new ApiResponse<>("Satıcı onaylandı !", HttpStatus.OK.value());
        }
        throw new SellerVerificationException("Sellers not verified", findNotVerifiedSellerDocuments);
    }

    public Seller getSellerById(String id) {
        return sellerRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new NotFoundException("Seller not found: " + id));
    }

    public Seller getSellerByUserId(String userId) {
        return sellerRepository.findSellerByUserId(UUID.fromString(userId));
    }

    public void saveSellerOrder(String sellerId, SellerOrder sellerOrder) {
        Seller seller = sellerRepository.findById(UUID.fromString(sellerId))
                        .orElseThrow(() -> new NotFoundException("Seller not found: " + sellerId));
        List<SellerOrder> orders = new ArrayList<>();
        orders.add(sellerOrder);
        seller.setSellerOrders(orders);
        sellerRepository.save(seller);
    }

    public ApiResponse<?> changeSellerActiveStatus(String sellerId) {
        Seller seller = sellerRepository.findById(UUID.fromString(sellerId))
                .orElseThrow(() -> new NotFoundException("Company not found: " + sellerId));
        seller.setActive(!seller.isActive());
        Seller changedStatusSeller = sellerRepository.save(seller);
        try {
            if(changedStatusSeller.isActive()) {
                SearchItem searchItem = new SearchItem();
                searchItem.setId(changedStatusSeller.getId().toString());
                searchItem.setName(seller.getName());
                searchItem.setImageUrl(seller.getLogo());
                searchItem.setItemType(SearchItemType.SELLER);
                searchItemService.createIndex(searchItem);
            } else {
                searchItemService.deleteItem(changedStatusSeller.getId().toString());
            }

            return new ApiResponse<>("Company Status Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating company", e);
        }
    }

    public SellerAdminDto getSellerInformation(){
        String sellerUserId = authenticationFacade.getCurrentUserId();
        Seller seller = sellerRepository.findSellerByUserId(UUID.fromString(sellerUserId));
        if (seller == null) {
            return null;
        }
        return SellerAdminDto.toDto(seller);
    }

    public Page<ProductListDto> getSellerProducts(Pageable pageable) {
        return sellerRepository.findProductsByUserId(UUID.fromString(authenticationFacade.getCurrentUserId()),pageable)
                .map(ProductListDto::toDto);
    }

    public Page<String> getAllCompanyMedia(String id, Pageable pageable) throws Exception {
        Seller seller = sellerRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Company not found: " + id));
        return fileService.listUserMedia(seller.getSlug(),pageable);
    }

    public Page<SellerAdminDto> getAllCompanies(Pageable pageable) {
        return sellerRepository.findAll(pageable).map(SellerAdminDto::toDto);
    }

    public SellerReportAggregation getSellerReportBySellerUserId() {
        String seller = sellerRepository.findSellerByUserId(UUID.fromString(authenticationFacade.getCurrentUserId()))
                .getId().toString();
        return sellerRepository.getSellerAggregatedProfit(UUID.fromString(seller));
    }

}