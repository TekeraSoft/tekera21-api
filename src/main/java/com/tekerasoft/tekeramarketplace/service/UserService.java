package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.UserAdminDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateUserRequest;
import com.tekerasoft.tekeramarketplace.dto.request.SellerVerificationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.model.enums.Role;
import com.tekerasoft.tekeramarketplace.repository.jparepository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    private final RoleService roleService;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public void createUser(CreateUserRequest req) {
        User user = new User();
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setGsmNumber(req.getGsmNumber());
        user.setGender(req.getGender());
        user.setBirthDate(req.getBirthDate());
        user.setHashedPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public User getUserInformation(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException(userId));
    }

    @Transactional
    public User assignSupport() {
        // Öncelikle assignCount < 7 olan supportları al
        List<User> eligibleSupports = userRepository.findEligibleSupports();

        List<User> candidateList;
        if (!eligibleSupports.isEmpty()) {
            candidateList = eligibleSupports;
        } else {
            // Yoksa tüm supportlar arasından seç
            candidateList = userRepository.findAllSupports();
        }

        if (candidateList.isEmpty()) {
            throw new RuntimeException("Not found support!");
        }

        // Random bir support seç
        User selectedSupport = candidateList.get(random.nextInt(candidateList.size()));

        // assignCount artır
        Integer currentCount = selectedSupport.getAssignCount();
        if (currentCount == null) currentCount = 0;
        selectedSupport.setAssignCount(currentCount + 1);

        return selectedSupport;
    }

    @Transactional
    public void supportAssignDecrease(User user) {
        Integer count = user.getAssignCount();
        user.setAssignCount(count != null ? user.getAssignCount() - 1 : 0);
        userRepository.save(user);
    }

    public ApiResponse<?> activeUserSellerRole(SellerVerificationRequest req) {
        User user = userRepository.findById(UUID.fromString(req.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException(req.getUserId()));
        userRepository.save(user);
        return new ApiResponse<>("Seller verification Successfully", HttpStatus.OK.value());
    }

    public void changeLastLoginUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    public void setApprovalSellerRole(User user) {
        user.setRoles(new HashSet<>(List.of(roleService.getRole(Role.WITHOUT_APPROVAL_SELLER.name()))));;
        userRepository.save(user);
    }

    public void attachSellerRole(User user) {
        user.setRoles(new HashSet<>(List.of(roleService.getRole(Role.SELLER.name()))));
        userRepository.save(user);
    }

    public ApiResponse<?> forgotPassword(String email, String password, String token) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                boolean isTokenValid = jwtService.validateToken(token,user.get());
                if(!isTokenValid) {
                    throw new RuntimeException("Doğrulama başarılı değil yetkisiz işlem");
                }
                user.get().setHashedPassword(passwordEncoder.encode(password));
                userRepository.save(user.get());
                return new ApiResponse<>("Şifre başarıyla değiştirildi giriş yapabilirsiniz", HttpStatus.OK.value());
            }
        } catch (RuntimeException e) {
            return null;
        }
        return new ApiResponse<>("Beklenmedik bir hata oluştu lütfen tekrar deneyin !",HttpStatus.NOT_FOUND.value());
    }


    public ApiResponse<?> changePassword(String email,String oldPassword,String password, String token) {
        try {
            Optional<User> useroptional = userRepository.findByEmail(email);
            if(useroptional.isPresent()) {
                User user = useroptional.get();
                boolean isTokenValid = jwtService.validateToken(token,user);
                if(!isTokenValid) {
                    throw new RuntimeException("Doğrulama başarılı değil yetkisiz işlem");
                }
                if(!passwordEncoder.matches(oldPassword,user.getHashedPassword())) {
                    throw new RuntimeException("Eski parolanız hatalı lütfen tekrar deneyin.");
                }
                user.setHashedPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return new ApiResponse<>("Parolanız başarıyla değiştirildi",HttpStatus.OK.value());
            } else {
                throw new RuntimeException("Kullanıcı bulunamadı!");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("İşlem başarısız sunucu cevap vermedi. Lütfen tekrar deneyiniz.");
        }

    }

    public Page<UserAdminDto> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserAdminDto::toDto);
    }

    public ApiResponse<?> changeUserRole(String userId, Role role) {
        try {
            User user = userRepository.findById(UUID.fromString(userId))
                    .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı !"));
            user.setRoles(new HashSet<>(Set.of(roleService.getRole(role.name()))));
            userRepository.save(user);
            return new ApiResponse<>("Kullanıcı rolü değiştirildi",HttpStatus.OK.value());
        } catch (RuntimeException e) {
            return null;
        }
    }

      // TODO: user active deactivate

}
