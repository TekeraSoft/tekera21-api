package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.SellerVerificationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerVerificationRepository;
import org.springframework.stereotype.Service;

@Service
public class SellerVerificationService {
    private final SellerVerificationRepository sellerVerificationRepository;
    private final SellerService sellerService;
    private final UserService userService;

    public SellerVerificationService(SellerVerificationRepository sellerVerificationRepository,
                                     SellerService sellerService, UserService userService) {
        this.sellerVerificationRepository = sellerVerificationRepository;
        this.sellerService = sellerService;
        this.userService = userService;
    }

//    public ApiResponse<?> checkDocumentVerification(String sellerVerificationId) {
//
//    }
//
//    public ApiResponse<?> sellerVerification(SellerVerificationRequest req) {
//        try {
//            User sellerUser = userService.getUserInformation(req.getUserId());
//            User supervisorUser = userService.getUserInformation(req.getSupervisorId());
//
//            SellerVerification sellerVerification = new SellerVerification();
//            sellerVerification.setSellerUser();
//        }
//    }

}
