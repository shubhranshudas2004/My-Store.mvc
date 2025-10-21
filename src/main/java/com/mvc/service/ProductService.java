package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.model.ProductModel;
import com.mvc.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Save new product
    public ProductModel saveProduct(ProductModel product) {
        return productRepository.save(product);
    }

    // Get product by ID
    public ProductModel getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Get all products
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    // Delete product by ID
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    // Update product
    public void updateProduct(ProductModel updatedProduct) {
        ProductModel existingProduct = productRepository.findById(updatedProduct.getId()).orElse(null);
        if (existingProduct != null) {
            existingProduct.setBrand(updatedProduct.getBrand());
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            existingProduct.setDescription(updatedProduct.getDescription());

            // ✅ Update image URL only if a new image is provided
            if (updatedProduct.getImageUrl() != null && !updatedProduct.getImageUrl().isEmpty()) {
                existingProduct.setImageUrl(updatedProduct.getImageUrl());
            }

            productRepository.save(existingProduct);
        }
    }
}
