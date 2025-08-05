package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.SellerAdminDto;
import com.tekerasoft.tekeramarketplace.dto.request.SellerVerificationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerVerificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SellerVerificationService {
    private final SellerVerificationRepository sellerVerificationRepository;
    private final UserService userService;

    public SellerVerificationService(SellerVerificationRepository sellerVerificationRepository, UserService userService) {
        this.sellerVerificationRepository = sellerVerificationRepository;
        this.userService = userService;
    }

    @Transactional
    public void assignToSupervisorSeller(String sellerUserId, Seller seller) {
        SellerVerification sellerVerification = new SellerVerification();
        User sellerUser = userService.getUserInformation(sellerUserId);
        User supervisorUser = userService.assignSupport();
        sellerVerification.setSellerUser(sellerUser);
        sellerVerification.setSupervisor(supervisorUser);
        sellerVerification.setSeller(seller);
        sellerVerificationRepository.save(sellerVerification);
    }

//    public ApiResponse<?> checkDocumentVerification(String sellerVerificationId) {
//
//    }

}
