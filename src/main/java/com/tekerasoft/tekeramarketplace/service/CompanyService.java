package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CompanyAdminDto;
import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.ProductListDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.CompanyException;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItem;
import com.tekerasoft.tekeramarketplace.model.esdocument.SearchItemType;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CompanyRepository;
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
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;
    private final SearchItemService searchItemService;
    private final ShippingCompanyService shippingCompanyService;

    public CompanyService(CompanyRepository companyRepository, CategoryRepository categoryRepository,
                          FileService fileService, SearchItemService searchItemService,
                          ShippingCompanyService shippingCompanyService) {
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.searchItemService = searchItemService;
        this.shippingCompanyService = shippingCompanyService;
    }

    @Transactional
    public ApiResponse<?> createCompany(CreateCompanyRequest req, List<MultipartFile> files, MultipartFile logo) {

        if(companyRepository.existsByNameAndTaxNumber(req.getName(),req.getTaxNumber())) {
            throw new CompanyException("Company already exists");
        }

        try {
            ShippingCompany shippingCompany = shippingCompanyService.getShippingCompany(req.getShippingCompanyId());
            Set<ShippingCompany> shippingCompanySet = new HashSet<>();
            shippingCompanySet.add(shippingCompany);
            Company company = new Company();
            company.setName(req.getName());
            company.setSlug(SlugGenerator.generateSlug(company.getName()));
            company.setEmail(req.getEmail());
            company.setGsmNumber(req.getGsmNumber());
            company.setAlternativePhoneNumber(req.getAlternativePhoneNumber());
            company.setSupportPhoneNumber(req.getSupportPhoneNumber());
            company.setTaxNumber(req.getTaxNumber());
            company.setTaxOffice(req.getTaxOffice());
            company.setMerisNumber(req.getMerisNumber());
            company.setRegistrationDate(req.getRegistrationDate());
            company.setContactPersonNumber(req.getContactPersonNumber());
            company.setContactPersonTitle(req.getContactPersonTitle());
            company.setAddress(req.getAddress());
            company.setBankAccounts(req.getBankAccount());
            company.setShippingCompanies(shippingCompanySet);
            company.setActive(true);

            // Category
            Set<Category> categoryList = req.getCategoryId()
                    .stream()
                    .map(id-> categoryRepository.findById(UUID.fromString(id))
                            .orElseThrow()).collect(Collectors.toSet());
            company.setCategories(categoryList);

            String companyReplaceName = company.getName().toLowerCase().replaceAll("\\s+", "_");

            String logoPath = fileService.folderFileUpload(logo, String.format("/company/logo/%s", companyReplaceName));
            if(logoPath != null) {
                company.setLogo(logoPath);
            } else {
                throw new CompanyException("Logo could not be loaded");
            }

            List<CompanyDocument> companyDocuments = new ArrayList<>();

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
                CompanyDocument document = new CompanyDocument(
                        documentTitle,
                        documentPath,
                        VerificationStatus.PENDING
                );
                companyDocuments.add(document);
            }

            // Bu companyDocuments listesini company entity’sine kaydeder
            company.setIdentityDocumentPaths(companyDocuments);
            company.setVerificationStatus(VerificationStatus.PENDING);
            // Company kaydını veritabanına kaydet
            companyRepository.save(company);

            return new ApiResponse<>("Create Company", HttpStatus.CREATED.value());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Company getCompanyById(String id) {
        return companyRepository.findById(UUID.fromString(id)).orElseThrow(() ->
                new NotFoundException("Company not found: " + id));
    }

    public ApiResponse<?> deleteCompany(String id) {
        Company company = companyRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Company not found")
        );
        try {
            company.getIdentityDocumentPaths().forEach(path -> {
                fileService.deleteFileProduct(path.getDocumentPath());
            });
            fileService.deleteFileProduct(company.getLogo());
            searchItemService.deleteItem(company.getId().toString());
            companyRepository.delete(company);
            return new ApiResponse<>("Delete Company", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new CompanyException(e.getMessage());
        }
    }

    public ApiResponse<?> changeCompanyActiveStatus(String companyId, Boolean active) {
        Company company = companyRepository.findById(UUID.fromString(companyId))
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));
        try {
            company.setActive(active);
            Company comp = companyRepository.save(company);
            if(active) {
                SearchItem searchItem = new SearchItem();
                searchItem.setId(comp.getId().toString());
                searchItem.setName(company.getName());
                searchItem.setImageUrl(company.getLogo());
                searchItem.setItemType(SearchItemType.COMPANY);
                searchItemService.createIndex(searchItem);
            } else {
                searchItemService.deleteItem(comp.getId().toString());
            }

            return new ApiResponse<>("Company Status Updated", HttpStatus.OK.value());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating company", e);
        }
    }

    public Page<String> getAllCompanyMedia(String id, Pageable pageable) throws Exception {
        Company company = companyRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Company not found: " + id));
        return fileService.listUserMedia(company.getSlug(),pageable);
    }

    public Page<CompanyAdminDto> getAllCompanies(Pageable pageable) {
        return companyRepository.findActiveCompanies(pageable).map(CompanyAdminDto::toDto);
    }

//    public Page<OrderDto> getAllOrders(String companyId, Pageable pageable) {
//        return companyRepository.findById(UUID.fromString(companyId)).get().getOrders().map(OrderDto::toDto);
//    }
}