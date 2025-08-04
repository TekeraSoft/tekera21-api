package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CompanyAdminDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.CompanyException;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
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

    public SellerService(SellerRepository sellerRepository, CategoryRepository categoryRepository,
                         FileService fileService, SearchItemService searchItemService,
                         ShippingCompanyService shippingCompanyService, UserService userService,
                         AuthenticationFacade authenticationFacade) {
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.searchItemService = searchItemService;
        this.shippingCompanyService = shippingCompanyService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @Transactional
    public ApiResponse<?> createSeller(CreateCompanyRequest req, List<MultipartFile> files, MultipartFile logo) {

        if(sellerRepository.existsByNameAndTaxNumber(req.getName(),req.getTaxNumber())) {
            throw new CompanyException("Company already exists");
        }

        try {
            ShippingCompany shippingCompany = shippingCompanyService.getShippingCompany(req.getShippingCompanyId());
            User user = userService.getByUsername(authenticationFacade.getCurrentUserEmail())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            Set<ShippingCompany> shippingCompanySet = new HashSet<>();
            shippingCompanySet.add(shippingCompany);
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
            seller.setActive(true);
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

                // CompanyDocument nesnesi oluştur
                SellerDocument document = new SellerDocument(
                        documentTitle,
                        documentPath,
                        VerificationStatus.PENDING
                );
                sellerDocuments.add(document);
            }

            // Bu companyDocuments listesini company entity’sine kaydeder
            seller.setIdentityDocumentPaths(sellerDocuments);
            seller.setVerificationStatus(VerificationStatus.PENDING);
            // Company kaydını veritabanına kaydet
            sellerRepository.save(seller);

            return new ApiResponse<>("Create Company", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Seller getCompanyById(String id) {
        return sellerRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new NotFoundException("Company not found: " + id));
    }

    public ApiResponse<?> deleteCompany(String id) {
        Seller seller = sellerRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Company not found")
        );
        try {
            seller.getIdentityDocumentPaths().forEach(path -> {
                fileService.deleteFileProduct(path.getDocumentPath());
            });
            fileService.deleteFileProduct(seller.getLogo());
            searchItemService.deleteItem(seller.getId().toString());
            sellerRepository.delete(seller);
            return new ApiResponse<>("Delete Company", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new CompanyException(e.getMessage());
        }
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

    public Page<String> getAllCompanyMedia(String id, Pageable pageable) throws Exception {
        Seller seller = sellerRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Company not found: " + id));
        return fileService.listUserMedia(seller.getSlug(),pageable);
    }

    public Page<CompanyAdminDto> getAllCompanies(Pageable pageable) {
        return sellerRepository.findActiveCompanies(pageable).map(CompanyAdminDto::toDto);
    }

    public void sellerActive() {
        Seller seller =  sellerRepository.findById(UUID.fromString("sellerId"))
                .orElseThrow(() -> new NotFoundException("Seller not found"));
        seller.setActive(true);
        seller.setVerified(true);
        seller.setVerificationStatus(VerificationStatus.VERIFIED);
        sellerRepository.save(seller);
    }

}