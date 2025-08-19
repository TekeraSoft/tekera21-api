package com.tekerasoft.tekeramarketplace.repository.mongorepository;

import com.tekerasoft.tekeramarketplace.model.document.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingRepository extends MongoRepository<Setting, String> {
}
