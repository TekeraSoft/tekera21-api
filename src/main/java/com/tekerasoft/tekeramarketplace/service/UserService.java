package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.CreateUserRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.Role;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.repository.jparepository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptyMap;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
        user.setRoles(Set.of(Role.CUSTOMER));
        user.setHashedPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public void changeLastLoginUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    public String createToken(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                return jwtService.generateToken(emptyMap(),email);
            }else {
                throw new UsernameNotFoundException(email);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
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
            throw new RuntimeException(e.getMessage());
        }
        return new ApiResponse<>("Beklenmedik bir hata oluştu lütfen tekrar deneyin",HttpStatus.NOT_FOUND.value());
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
}
