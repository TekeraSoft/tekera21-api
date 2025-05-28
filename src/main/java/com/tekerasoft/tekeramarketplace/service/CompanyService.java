package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.CompanyDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.Category;
import com.tekerasoft.tekeramarketplace.model.entity.Company;
import com.tekerasoft.tekeramarketplace.model.entity.CompanyDocument;
import com.tekerasoft.tekeramarketplace.model.entity.VerificationStatus;
import com.tekerasoft.tekeramarketplace.repository.releational.CategoryRepository;
import com.tekerasoft.tekeramarketplace.repository.releational.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public CompanyService(CompanyRepository companyRepository, CategoryRepository categoryRepository,
                          FileService fileService) {
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ApiResponse<?> createCompany(CreateCompanyRequest req, List<MultipartFile> files, MultipartFile logo) {
        try {
            Company company = new Company();
            company.setName(req.getName());
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

            // Category
            Set<Category> categoryList = req.getCategoryId()
                    .stream()
                    .map(id-> categoryRepository.findById(UUID.fromString(id))
                            .orElseThrow()).collect(Collectors.toSet());
            company.setCategories(categoryList);

            String companyReplaceName = company.getName().replaceAll("\\s+", "-");

            String logoPath = fileService.folderFileUpload(logo, String.format("/company/logo/%s", companyReplaceName));
            company.setLogo(logoPath);

            List<CompanyDocument> companyDocuments = new ArrayList<>();

            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();

                // Dosya adından uzantıyı çıkar: imzasurkusu.pdf -> imzasurkusu
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

    public Page<CompanyDto> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).map(CompanyDto::toDto);
    }
}
