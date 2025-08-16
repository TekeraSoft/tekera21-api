package com.tekerasoft.tekeramarketplace.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tekerasoft.tekeramarketplace.dto.CategoryLoaderDto;
import com.tekerasoft.tekeramarketplace.dto.SubCategoryLoader;
import com.tekerasoft.tekeramarketplace.model.entity.Category;
import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany;
import com.tekerasoft.tekeramarketplace.model.entity.SubCategory;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.model.entity.Role;
import com.tekerasoft.tekeramarketplace.model.enums.Gender;
import com.tekerasoft.tekeramarketplace.repository.esrepository.SearchItemRepository;
import com.tekerasoft.tekeramarketplace.repository.jparepository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final SearchItemRepository searchItemRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShippingCompanyRepository shippingCompanyRepository;
    private final RoleRepository roleRepository;

    // Constructorı güncelledik
    public DataLoader(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
                      ObjectMapper objectMapper,
                      ProductRepository productRepository,
                      SellerRepository sellerRepository, SearchItemRepository searchItemRepository, UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      ShippingCompanyRepository shippingCompanyRepository,
                      RoleRepository roleRepository) { // <<-- 2. DEĞİŞİKLİK: Bu parametreyi ekledik
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository; // <<-- Bu satırı ekledik (2. değişikliğin parçası)
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.searchItemRepository = searchItemRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.shippingCompanyRepository = shippingCompanyRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Başlangıç rolleri
        List<String> defaultRoles = List.of(
                "SUPER_ADMIN",
                "ADMIN",
                "AUDITOR",
                "FINANCE_MANAGER",
                "MARKETING_MANAGER",
                "MODERATOR",
                "DEVELOPER",
                "WITHOUT_APPROVAL_SELLER",
                "SELLER",
                "SELLER_EMPLOYEE",
                "SELLER_SUPPORT",
                "SELLER_MARKETING_MANAGER",
                "SELLER_FINANCE_MANAGER",
                "CUSTOMER",
                "COURIER"
        );

        // Eğer roller yoksa DB'ye ekle
        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }

        // Eğer kullanıcı yoksa default kullanıcıları ekle
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByName("SUPER_ADMIN");
            Role sellerSupportRole = roleRepository.findByName("SELLER_SUPPORT");
            Role customerRole = roleRepository.findByName("CUSTOMER");

            User superAdmin = new User();
            superAdmin.setFirstName("Admin");
            superAdmin.setLastName("Full Access");
            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setHashedPassword(passwordEncoder.encode("123456"));
            superAdmin.setGsmNumber("123456789");
            superAdmin.setGender(Gender.MALE);
            superAdmin.setBirthDate(LocalDate.of(1980, 1, 1));
            superAdmin.setRoles(new HashSet<>(Set.of(superAdminRole)));

            User sellerSupport = new User();
            sellerSupport.setFirstName("Support");
            sellerSupport.setLastName("Any Access");
            sellerSupport.setEmail("sellersupport@gmail.com");
            sellerSupport.setHashedPassword(passwordEncoder.encode("123456"));
            sellerSupport.setGsmNumber("123456789");
            sellerSupport.setGender(Gender.MALE);
            sellerSupport.setBirthDate(LocalDate.of(1980, 1, 1));
            sellerSupport.setRoles(new HashSet<>(Set.of(sellerSupportRole)));

            User customer = new User();
            customer.setFirstName("Customer");
            customer.setLastName("Any Access");
            customer.setEmail("customer@gmail.com");
            customer.setHashedPassword(passwordEncoder.encode("123456"));
            customer.setGsmNumber("123456789");
            customer.setGender(Gender.MALE);
            customer.setBirthDate(LocalDate.of(1980, 1, 1));
            customer.setRoles(new HashSet<>(Set.of(customerRole)));

            userRepository.saveAll(List.of(superAdmin, sellerSupport, customer));
        }

        // Eğer hiç kargo firması yoksa default ekle
        if (shippingCompanyRepository.count() == 0) {
            ShippingCompany shippingCompany = new ShippingCompany();
            shippingCompany.setName("MNG");
            shippingCompany.setGsmNumber("0242 222 22 22");
            shippingCompany.setEmail("mgn@mng.com");
            shippingCompany.setPrice(BigDecimal.valueOf(89.00));
            shippingCompany.setMinDeliveryDay(2);
            shippingCompany.setMaxDeliveryDay(4);

            shippingCompanyRepository.save(shippingCompany);
        }

        // Eğer hiç kategori yoksa JSON’dan yükle
        if (categoryRepository.count() == 0) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("categories.json")) {
                if (inputStream == null) {
                    System.out.println("categories.json bulunamadı!");
                    return;
                }

                List<CategoryLoaderDto> categoriesDto =
                        objectMapper.readValue(inputStream, new TypeReference<>() {});

                for (CategoryLoaderDto categoryLoaderDto : categoriesDto) {
                    Category category = new Category();
                    category.setName(categoryLoaderDto.getName());
                    category.setSlug(categoryLoaderDto.getSlug());
                    category.setImage(null);
                    categoryRepository.save(category);

                    if (categoryLoaderDto.getSubCategories() != null) {
                        for (SubCategoryLoader subCategoryLoader : categoryLoaderDto.getSubCategories()) {
                            saveSubCategory(subCategoryLoader, category, null);
                        }
                    }
                }
                System.out.println("Kategoriler başarıyla yüklendi!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveSubCategory(SubCategoryLoader subCategoryLoader, Category category, SubCategory parentSubCategory) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryLoader.getName());
        subCategory.setSlug(subCategoryLoader.getSlug());
        subCategory.setImage(null);
        subCategory.setCategory(category);
        subCategory.setParent(parentSubCategory);
        subCategoryRepository.save(subCategory);

        if (subCategoryLoader.getChildren() != null) {
            for (SubCategoryLoader childLoader : subCategoryLoader.getChildren()) {
                saveSubCategory(childLoader, category, subCategory);
            }
        }







//      InputStream inputStream = getClass().getClassLoader().getResourceAsStream("arzuamber-products.json");
//      System.out.println("Veriler yükleniyor...");
//        List<OldProduct> oldProducts = objectMapper.readValue(inputStream, new TypeReference<List<OldProduct>>() {});
//        for (OldProduct old: oldProducts) {
//            Product product = new Product();
//            product.setName(old.getName());
//            product.setSlug(old.getSlug());
//            product.setDescription(old.getDescription());
//            product.setCurrencyType(CurrencyType.TRY);
//            product.setProductType(ProductType.PHYSICAL);
//            product.setActive(true);
//
//            Company company = companyRepository.findById(UUID.fromString("452531d6-343c-4952-a923-af51f3bffe2b")).orElseThrow();
//            product.setCompany(company);
//
//            Category category = categoryRepository.findById(UUID.fromString("83c38579-b156-4af3-a3a9-d43c35a82eee"))
//                    .orElseThrow(() -> new RuntimeException("Category not found"));
//            product.setCategory(category);
//
//            List<Variation> variations = new ArrayList<>();
//            for (ColorSize varReq : old.getColorSize()) {
//                Variation var = new Variation();
//                var.setModelName(varReq.getColor()+"-"+varReq.getStockCode());
//                var.setModelCode(varReq.getStockCode());
//                var.setProduct(product);
//                var.setColor(varReq.getColor());
//
//                // Variation attributes
//                List<Attribute> variationAttributes = varReq.getStockSize().stream()
//                        .map(attr -> {
//                            List<AttributeDetail> attributeDetails = new ArrayList<>();
//                            attributeDetails.add(new AttributeDetail("size",attr.getSize()));
//                           return new Attribute(
//                                    old.getPrice(),
//                                    old.getDiscountPrice(),
//                                    attr.getStock(),
//                                    varReq.getStockCode(),
//                                    null,
//                                    attributeDetails,
//                                    var
//                            );
//                        }).collect(Collectors.toList());
//                var.setAttributes(variationAttributes);
//                var.setImages(varReq.getImages().stream().map((i) -> "/products/arzuamber_moda/" + i).toList());
//                variations.add(var);
//            }
//            product.setVariations(variations);
//            productRepository.save(product);
//        }
//        System.out.println("Veriler sorunsuz yüklendi :)");


//        List<Product> products = productRepository.findAll();
//        products.forEach(product -> {
//            searchItemRepository.save(new SearchItem(
//                    product.getId().toString(),
//                    product.getName(),
//                    product.getSlug(),
//                    product.getCategory().getName(),
//                    product.getCategory().getSlug(),
//                    product.getSeller().getId().toString(),
//                    product.getVariations().get(0).getImages().get(0),
//                    SearchItemType.PRODUCT,
//                    0.0
//            ));
//        });
//
//        List<Category> categories = categoryRepository.findAll();
//        categories.forEach(category -> {
//            searchItemRepository.save(new SearchItem(
//                    category.getId().toString(),
//                    category.getName(),
//                    category.getSlug(),
//                    null,
//                    null,
//                    null,
//                    category.getImage(),
//                    SearchItemType.CATEGORY,
//                    0.0
//            ));
//        });

    }
}