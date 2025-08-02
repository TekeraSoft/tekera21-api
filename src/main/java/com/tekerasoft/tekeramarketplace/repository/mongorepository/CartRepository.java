package com.tekerasoft.tekeramarketplace.repository.mongorepository;

import com.tekerasoft.tekeramarketplace.model.document.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart,String> { }
