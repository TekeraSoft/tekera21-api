package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.SellerAdminDto;
import com.tekerasoft.tekeramarketplace.dto.SellerVerificationSupportDto;
import com.tekerasoft.tekeramarketplace.dto.request.SellerVerificationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.SellerDocument;
import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerVerificationRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SellerVerificationService {
    private final SellerVerificationRepository sellerVerificationRepository;
    private final AuthenticationFacade  authenticationFacade;
    private final UserService userService;

    public SellerVerificationService(SellerVerificationRepository sellerVerificationRepository,
                                     AuthenticationFacade authenticationFacade,
                                     UserService userService) {
        this.sellerVerificationRepository = sellerVerificationRepository;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

    public Page<SellerVerificationSupportDto> getSupportVerificationList(Pageable pageable) {
        String supportId = authenticationFacade.getCurrentUserId();
        return sellerVerificationRepository.findBySupportId(UUID.fromString(supportId),pageable)
                .map(SellerVerificationSupportDto::toDto);
    }

    public User getSellerSupport(String sellerId) {
        return sellerVerificationRepository.findSupervisorVerificationBySupportId(UUID.fromString(sellerId))
                .getSupervisor();
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
}