package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.FashionCollectionDto;
import com.tekerasoft.tekeramarketplace.dto.FashionCollectionListDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateFashionCollectionRequest;
import com.tekerasoft.tekeramarketplace.dto.request.UpdateFashionCollectionRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.exception.NotFoundException;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.FashionCollection;
import com.tekerasoft.tekeramarketplace.model.entity.Product;
import com.tekerasoft.tekeramarketplace.repository.jparepository.FashionCollectionRepository;
import com.tekerasoft.tekeramarketplace.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FashionCollectionService {

    private final FashionCollectionRepository fashionCollectionRepository;
    private final ProductService productService;
    private final FileService fileService;
    private final SellerService sellerService;

    public FashionCollectionService(FashionCollectionRepository fashionCollectionRepository,
                                    ProductService productService, FileService fileService, SellerService sellerService) {
        this.fashionCollectionRepository = fashionCollectionRepository;
        this.productService = productService;
        this.fileService = fileService;
        this.sellerService = sellerService;
    }

    public ApiResponse<?> createFashionCollection(CreateFashionCollectionRequest req) {
        Set<Product> productList = new LinkedHashSet<>(); {
        };
        req.getProducts().forEach(product -> {
            productList.add(productService.getById(UUID.fromString(product)));
        });
        Seller seller = sellerService.getCompanyById(req.getCompanyId());
        FashionCollection collection = new FashionCollection();
        String imagePath = fileService.folderFileUpload(req.getImage(), "fashion-collection-images");
        collection.setImage(imagePath);
        collection.setSlug(SlugGenerator.generateSlug(req.getCollectionName()));
        collection.setProducts(productList);
        collection.setCollectionName(req.getCollectionName());
        collection.setDescription(req.getDescription());
        collection.setActive(true);
        collection.setSeller(seller);
        fashionCollectionRepository.save(collection);
        return new ApiResponse<>("Collection Created", HttpStatus.CREATED.value());
    }

    @Transactional                          // tek transaction
    public ApiResponse<?> updateFashionCollection(UpdateFashionCollectionRequest req) {

        FashionCollection collection = fashionCollectionRepository.findById(
                        UUID.fromString(req.getId()))
                .orElseThrow(() -> new NotFoundException("Fashion Collection not found"));

        /* 1️⃣ Koleksiyonu yerinde temizle */
        collection.getProducts().clear();

        /* 2️⃣ Yeni Product’ları ekle */
        req.getProducts().forEach(productId -> {
            Product p = productService.getById(UUID.fromString(productId));
            collection.getProducts().add(p);       // ⇦ addAll da olur
        });

        /* 3️⃣ Diğer alanlar */
        if (!req.getImage().isEmpty()) {
            fileService.deleteInFolderFile(collection.getImage());
            String path = fileService.folderFileUpload(req.getImage(), "fashion-collection-images");
            collection.setImage(path);
        }
        collection.setCollectionName(req.getCollectionName());
        collection.setSlug(req.getCollectionName());
        collection.setDescription(req.getDescription());

        /* 4️⃣ Kaydet (flush otomatik) */
        fashionCollectionRepository.save(collection);

        return new ApiResponse<>("Collection Updated", HttpStatus.OK.value());
    }

    public Page<FashionCollectionListDto> getFashionCollectionsByCompany(String companyId, Pageable pageable) {
        return fashionCollectionRepository.findCompanyCollection(UUID.fromString(companyId), pageable)
                .map(FashionCollectionListDto::toUiListDto);
    }

    public Page<FashionCollectionListDto> getAllFashionCollection(Pageable pageable) {
        return fashionCollectionRepository.findActiveCollections(pageable).map(FashionCollectionListDto::toUiListDto);
    }

    public FashionCollectionDto getFashionCollection(String id) {
        return fashionCollectionRepository.findById(UUID.fromString(id))
                .map(FashionCollectionDto::toDto).orElseThrow(() -> new NotFoundException("Fashion Collection not found"));
    }

    public ApiResponse<?> deleteFashionCollection(String id) {
        FashionCollection collection = fashionCollectionRepository.findById(UUID.fromString(id))
                        .orElseThrow(() -> new NotFoundException("Fashion Collection not found"));
       fileService.deleteInFolderFile(collection.getImage());
       fashionCollectionRepository.delete(collection);
        return new ApiResponse<>("Collection Deleted", HttpStatus.OK.value());
    }


}
