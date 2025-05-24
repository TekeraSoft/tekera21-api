package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.CreateCompanyRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.Company;
import com.tekerasoft.tekeramarketplace.model.entity.CompanyDocument;
import com.tekerasoft.tekeramarketplace.model.entity.VerificationStatus;
import com.tekerasoft.tekeramarketplace.repository.releational.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final FileService fileService;

    public CompanyService(CompanyRepository companyRepository, FileService fileService) {
        this.companyRepository = companyRepository;
        this.fileService = fileService;
    }

    @Transactional
    public ApiResponse<?> createCompany(CreateCompanyRequest req, List<MultipartFile> files, MultipartFile logo) {
        try {
            Company company = new Company();
            company.setName(req.getName());
            company.setCategory(req.getCategoryName());
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

            String logoPath = fileService.folderFileUpload(logo, String.format("/company/logo/%s", company.getName()));
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
                        String.format("/company/documents/%s", company.getName()));

                // CompanyDocument nesnesi oluştur
                CompanyDocument document = new CompanyDocument(
                        documentTitle,
                        documentPath,
                        VerificationStatus.PENDING // varsayılan zaten bu
                );
                companyDocuments.add(document);
            }

            // Bu companyDocuments listesini company entity’sine kaydeder
            company.setIdentityDocumentPaths(companyDocuments);
            company.setVerificationStatus(VerificationStatus.PENDING);
            // Company kaydını veritabanına kaydet
            companyRepository.save(company);

            return new ApiResponse<>("Create Company", null, true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
