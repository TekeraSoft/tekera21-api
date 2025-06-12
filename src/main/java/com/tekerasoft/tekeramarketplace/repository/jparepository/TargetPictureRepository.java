package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.TargetPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TargetPictureRepository extends JpaRepository<TargetPicture, UUID> {

    Optional<TargetPicture> findByIdAndProductId(UUID targetId, String productId);

    Optional<TargetPicture> findByProductId(String productId);

}
