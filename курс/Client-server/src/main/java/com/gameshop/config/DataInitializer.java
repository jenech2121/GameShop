package com.gameshop.config;

import com.gameshop.model.Product;
import com.gameshop.model.User;
import com.gameshop.repository.ProductRepository;
import com.gameshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Создание админа по умолчанию
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(User.UserRole.ADMIN);
            admin.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(admin);
        }
        
        if (productRepository.count() == 0) {
            // Видеокарты (GPU)
            productRepository.save(new Product(null, "NVIDIA GeForce RTX 3060", 5000.0,
                    "https://dlcdnwebimgs.asus.com/files/media/80edfc49-cc69-47e1-afe6-992488abbb96/img/kv/product-kv_s.jpg",
                    Product.ProductCategory.GPU, "Видеокарта NVIDIA GeForce RTX 3060, 12GB GDDR6", 5));
            
            productRepository.save(new Product(null, "AMD Radeon RX 6700 XT", 6000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.GPU, "Видеокарта AMD Radeon RX 6700 XT, 12GB GDDR6", 7));
            
            productRepository.save(new Product(null, "NVIDIA GeForce RTX 4070", 8500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.GPU, "Видеокарта NVIDIA GeForce RTX 4070, 12GB GDDR6X", 3));
            
            // Материнские платы (MOTHERBOARD)
            productRepository.save(new Product(null, "Asus Prime A520M-A", 1000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUcMzmoQ2ysXJrw-W6Hk9_VGJlfdFXk_Ye4A&s",
                    Product.ProductCategory.MOTHERBOARD, "Материнская плата Asus Prime A520M-A, Socket AM4, Micro-ATX", 10));
            
            productRepository.save(new Product(null, "Asus Prime B560M-A", 1200.0,
                    "https://www.tiburcc.it/img/prodimg_l/ea2b8b82b23608897103573c4458ca4d-hi.jpg",
                    Product.ProductCategory.MOTHERBOARD, "Материнская плата Asus Prime B560M-A, Socket LGA1200, Micro-ATX", 8));
            
            productRepository.save(new Product(null, "MSI B450 Tomahawk Max", 1500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUcMzmoQ2ysXJrw-W6Hk9_VGJlfdFXk_Ye4A&s",
                    Product.ProductCategory.MOTHERBOARD, "Материнская плата MSI B450 Tomahawk Max, Socket AM4, ATX", 6));
            
            // Оперативная память (RAM)
            productRepository.save(new Product(null, "Corsair Vengeance LPX 16GB (2x8GB)", 2000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.RAM, "Оперативная память Corsair Vengeance LPX 16GB DDR4 3200MHz", 20));
            
            productRepository.save(new Product(null, "Kingston Fury Beast 32GB (2x16GB)", 3500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.RAM, "Оперативная память Kingston Fury Beast 32GB DDR4 3200MHz", 15));
            
            productRepository.save(new Product(null, "G.Skill Trident Z RGB 32GB (2x16GB)", 4000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.RAM, "Оперативная память G.Skill Trident Z RGB 32GB DDR4 3600MHz", 12));
            
            // Процессоры (CPU)
            productRepository.save(new Product(null, "AMD Ryzen 5 5600X", 2500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiblkEM2gCJ8hizpXVUwGn7mvukmKVV1CABQ&s",
                    Product.ProductCategory.CPU, "Процессор AMD Ryzen 5 5600X, 6 ядер, 12 потоков, 3.7-4.6 GHz", 15));
            
            productRepository.save(new Product(null, "Intel Core i5-12400F", 3000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkwHI2Ii5730zSfaLgPlkg-OGhjku-Z43H6w&s",
                    Product.ProductCategory.CPU, "Процессор Intel Core i5-12400F, 6 ядер, 12 потоков, 2.5-4.4 GHz", 12));
            
            productRepository.save(new Product(null, "AMD Ryzen 7 5800X", 4500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiblkEM2gCJ8hizpXVUwGn7mvukmKVV1CABQ&s",
                    Product.ProductCategory.CPU, "Процессор AMD Ryzen 7 5800X, 8 ядер, 16 потоков, 3.8-4.7 GHz", 8));
            
            // Блоки питания (POWER)
            productRepository.save(new Product(null, "Corsair RM750x 750W", 3500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.POWER, "Блок питания Corsair RM750x 750W, 80+ Gold, модульный", 10));
            
            productRepository.save(new Product(null, "Seasonic Focus GX-650 650W", 3000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.POWER, "Блок питания Seasonic Focus GX-650 650W, 80+ Gold, модульный", 12));
            
            productRepository.save(new Product(null, "be quiet! Straight Power 11 850W", 4500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.POWER, "Блок питания be quiet! Straight Power 11 850W, 80+ Gold, модульный", 7));
            
            // SSD накопители (SSD)
            productRepository.save(new Product(null, "Samsung 970 EVO Plus 500GB", 2500.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.SSD, "SSD накопитель Samsung 970 EVO Plus 500GB, M.2 NVMe, 3500/3300 MB/s", 18));
            
            productRepository.save(new Product(null, "WD Black SN850 1TB", 4000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.SSD, "SSD накопитель WD Black SN850 1TB, M.2 NVMe, 7000/5300 MB/s", 15));
            
            productRepository.save(new Product(null, "Crucial MX500 1TB SATA", 2000.0,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s",
                    Product.ProductCategory.SSD, "SSD накопитель Crucial MX500 1TB, SATA III, 560/510 MB/s", 20));
        }
    }
}


