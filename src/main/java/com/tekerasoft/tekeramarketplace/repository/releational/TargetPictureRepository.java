package com.tekerasoft.tekeramarketplace.repository.releational;

import com.tekerasoft.tekeramarketplace.model.entity.TargetPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TargetPictureRepository extends JpaRepository<TargetPicture, UUID> {
}
