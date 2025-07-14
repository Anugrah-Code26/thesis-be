package com.thesis.backend.service.product.impl;

import com.thesis.backend.common.exceptions.UnauthorizedException;
import com.thesis.backend.entity.product.Product;
import com.thesis.backend.entity.user.User;
import com.thesis.backend.infrastructure.auth.Claims;
import com.thesis.backend.infrastructure.product.dto.ProductDTO;
import com.thesis.backend.infrastructure.product.repository.ProductRepository;
import com.thesis.backend.infrastructure.user.repository.UserRepository;
import com.thesis.backend.service.product.ProductService;
import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.service.product.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        Long userId = Claims.getUserIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Product product = new Product();
        product.setUser(user);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setDeleted(false);

        return productRepository.save(product);
    }

    @Override
    public List<Product> findProducts(String search, Double minPrice, Double maxPrice, String sortBy, String sortDir) {
        Long userId = Claims.getUserIdFromJwt();

        Specification<Product> spec = Specification.where(ProductSpecification.hasUserId(userId))
                .and(ProductSpecification.containsTextInNameOrDescription(search))
                .and(ProductSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ProductSpecification.priceLessThanOrEqual(maxPrice))
                .and((root, query, cb) -> cb.isFalse(root.get("deleted")));

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> products = productRepository.findAll(spec, sort);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }
        return products;
    }

    @Override
    public List<Product> getAllActiveProducts() {
        Long userId = Claims.getUserIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return productRepository.findActiveByUserId(user.getId());
    }

    @Override
    public List<Product> getAllProducts() {
        Long userId = Claims.getUserIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return productRepository.findByUserId(user.getId());
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        Long userId = Claims.getUserIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return productRepository.findById(id)
                .filter(product -> product.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Long userId = Claims.getUserIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Product product = productRepository.findByIdAndUserId(id, user.getId());
        if (product == null) {
            throw new UnauthorizedException("Unauthorized!");
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        return productRepository.save(product);
    }

    @Override
    public void softDeleteProduct(Long id) throws DataNotFoundException {
        Long userId = Claims.getUserIdFromJwt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Product checkProduct = productRepository.findByIdAndUserId(id, user.getId());
        if (checkProduct == null) {
            throw new UnauthorizedException("Unauthorized!");
        }

        Product product = getProductById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }
}