package com.gameshop.dto;

import com.gameshop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private Product.ProductCategory category;
    private String description;
    private Integer stock;
    
    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImage(),
            product.getCategory(),
            product.getDescription(),
            product.getStock()
        );
    }
    
    public Product toEntity() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setPrice(this.price);
        product.setImage(this.image);
        product.setCategory(this.category);
        product.setDescription(this.description);
        product.setStock(this.stock);
        return product;
    }
}













