package com.tekerasoft.tekeramarketplace.repository.nosql;

import com.tekerasoft.tekeramarketplace.model.document.TargetPicture;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TargetPictureRepository extends MongoRepository<TargetPicture, String> {
}
