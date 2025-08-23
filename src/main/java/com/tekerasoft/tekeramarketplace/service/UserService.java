package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.ProductUiDto;
import com.tekerasoft.tekeramarketplace.dto.UserAdminDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateUserRequest;
import com.tekerasoft.tekeramarketplace.dto.request.SellerVerificationRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.exception.UnauthorizedException;
import com.tekerasoft.tekeramarketplace.model.entity.Product;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.model.entity.UserLikeReaction;
import com.tekerasoft.tekeramarketplace.model.enums.LikeState;
import com.tekerasoft.tekeramarketplace.model.enums.Role;
import com.tekerasoft.tekeramarketplace.repository.jparepository.ProductRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.SellerRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.UserRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();
    private final RoleService roleService;
    private final AuthenticationFacade authenticationFacade;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
                       RoleService roleService, AuthenticationFacade authenticationFacade,
                       SellerRepository sellerRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.authenticationFacade = authenticationFacade;
        this.sellerRepository = sellerRepository;
        this.productRepository = productRepository;
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

    public void updateUser(User user) {
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

    @Transactional
    public ApiResponse<?> followUnfollowSeller(String sellerId) {
        Seller seller = sellerRepository.findById(UUID.fromString(sellerId))
                .orElseThrow(() -> new NotFoundException("Satıcı bulunamadı"));

        if(authenticationFacade.getCurrentUserId() == null) {
            throw new UnauthorizedException("Takip etmek için lütfen üye olunuz.");
        }
        User user = userRepository.findById(UUID.fromString(authenticationFacade.getCurrentUserId()))
                .orElseThrow(() -> new UnauthorizedException("Takip etmek için lütfen üye olunuz."));

        if (user.getFollowSellers().contains(seller)) {
            // zaten takip ediyorsa çıkar → UNFOLLOW
            user.getFollowSellers().remove(seller);
            seller.getFollowUsers().remove(user); // iki taraflı ilişkiyi güncellemek için
            userRepository.save(user);
            return new ApiResponse<>("Satıcı takipten çıkarıldı", HttpStatus.OK.value());
        } else {
            // takip etmiyorsa ekle → FOLLOW
            user.getFollowSellers().add(seller);
            seller.getFollowUsers().add(user); // iki taraflı ilişkiyi güncellemek için
            userRepository.save(user);
            return new ApiResponse<>("Satıcı takibe başlandı", HttpStatus.OK.value());
        }
    }

    public ApiResponse<?> addToFavoriteProduct(String productId) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Ürün Bulunamadı!"));

        if(authenticationFacade.getCurrentUserId() == null) {
            throw new UnauthorizedException("Farilere eklemek için üye olunuz.");
        }

        User user = userRepository.findById(UUID.fromString(authenticationFacade.getCurrentUserId()))
                .orElseThrow(() -> new NotFoundException("Kullanıcı Bulunamadı"));

        if(user.getFavProducts().contains(product)) {
           user.getFavProducts().remove(product);
            userRepository.save(user);
            return new ApiResponse<>("Ürün favorilerden çıkarıldı", HttpStatus.OK.value());
        }else {
            user.getFavProducts().add(product);
            userRepository.save(user);
            return new ApiResponse<>("Ürün favorilere eklendi", HttpStatus.OK.value());
        }

    }

    @Transactional
    public ApiResponse<?> likeProduct(String productId) {

        String currentUserId = authenticationFacade.getCurrentUserId();
        if (currentUserId == null) {
            throw new UnauthorizedException("Favorilere eklemek için üye olunuz.");
        }

        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Ürün bulunamadı"));

        User user = userRepository.findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        UserLikeReaction reaction = user.getLikedProducts().stream()
                .filter(r -> r.getProduct().equals(product))
                .findFirst()
                .orElse(new UserLikeReaction(user, product, LikeState.EMPTY));

        if (reaction.getState() == LikeState.LIKED) {
            reaction.setState(LikeState.EMPTY); // Like varsa kaldır
            userRepository.save(user);
            return new ApiResponse<>("Ürün beğenilerden çıkarıldı", HttpStatus.OK.value());
        } else {
            reaction.setState(LikeState.LIKED); // Like veya dislike varsa like yap
            user.getLikedProducts().add(reaction); // Eğer yeni reaction ise set'e ekle
            userRepository.save(user);
            return new ApiResponse<>("Ürün beğenildi", HttpStatus.OK.value());
        }
    }

    public Page<ProductUiDto> getLikedProducts(Pageable pageable) {
        User user = userRepository.findById(UUID.fromString(authenticationFacade.getCurrentUserId()))
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        return new PageImpl<>(
                user.getLikedProducts()
                        .stream()
                        .map(p -> ProductUiDto.toProductUiDto(p.getProduct(), user))
                        .collect(Collectors.toList()),
                pageable, // Pageable parametresini servisten alman lazım
                user.getLikedProducts().size()
        );

    }

    public Page<ProductUiDto> getFavoriteProducts(Pageable pageable) {
        User user = userRepository.findById(UUID.fromString(authenticationFacade.getCurrentUserId()))
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı"));

        return new PageImpl<>(
                user.getFavProducts()
                        .stream()
                        .map(p -> ProductUiDto.toProductUiDto(p, user))
                        .collect(Collectors.toList()),
                pageable, // Pageable parametresini servisten alman lazım
                user.getFavProducts().size()
        );

    }

}
