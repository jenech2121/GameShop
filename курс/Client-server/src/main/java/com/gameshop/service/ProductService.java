package com.gameshop.service;

import com.gameshop.dto.ProductDTO;
import com.gameshop.model.Product;
import com.gameshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return ProductDTO.fromEntity(product);
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Product.ProductCategory category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productDTO.toEntity();
        product = productRepository.save(product);
        return ProductDTO.fromEntity(product);
    }
    
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImage(productDTO.getImage());
        product.setCategory(productDTO.getCategory());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        
        product = productRepository.save(product);
        return ProductDTO.fromEntity(product);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}









