package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query(
            value = "SELECT u.* FROM users u " +
                    "JOIN user_roles ur ON ur.user_id = u.id " +
                    "WHERE ur.roles = 'SELLER_SUPPORT' AND (u.assign_count < 7 OR u.assign_count IS NULL)",
            nativeQuery = true
    )
    List<User> findEligibleSupports();

    @Query(
            value = "SELECT u.* FROM users u " +
                    "JOIN user_roles ur ON ur.user_id = u.id " +
                    "WHERE ur.roles = 'SELLER_SUPPORT'",
            nativeQuery = true
    )
    List<User> findAllSupports();
}
