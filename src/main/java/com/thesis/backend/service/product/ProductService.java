package com.thesis.backend.service.product;

import com.thesis.backend.entity.product.Product;
import com.thesis.backend.infrastructure.product.dto.ProductDTO;
import com.thesis.backend.common.exceptions.DataNotFoundException;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);
    List<Product> findProducts(String search, Double minPrice, Double maxPrice, String sortBy, String sortDir);
    List<Product> getAllActiveProducts();
    List<Product> getAllProducts();
    Product getProductById(Long id) throws DataNotFoundException;
    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;
    void softDeleteProduct(Long id) throws DataNotFoundException;
}
