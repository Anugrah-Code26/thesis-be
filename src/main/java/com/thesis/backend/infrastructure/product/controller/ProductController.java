package com.thesis.backend.infrastructure.product.controller;

import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.infrastructure.product.dto.ProductDTO;
import com.thesis.backend.service.product.ProductService;
import com.thesis.backend.common.exceptions.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {
        return ApiResponse.success(HttpStatus.OK.value(), "Create product success!", productService.createProduct(productDTO));
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ApiResponse.success(HttpStatus.OK.value(), "Get product success!", productService.findProducts(search, minPrice, maxPrice, sortBy, sortDir));
    }

    @GetMapping("/active/all")
    public ResponseEntity<?> getAllActiveProducts() {
        return ApiResponse.success(HttpStatus.OK.value(), "Get all active products success!", productService.getAllActiveProducts());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        return ApiResponse.success(HttpStatus.OK.value(), "Get all product success!", productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long id) throws DataNotFoundException {
        return ApiResponse.success(HttpStatus.OK.value(), "Get product by id success!", productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) throws DataNotFoundException {
        return ApiResponse.success(HttpStatus.OK.value(), "Update product by id success!", productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteProduct(
            @PathVariable Long id) throws DataNotFoundException {
        productService.softDeleteProduct(id);
        return ApiResponse.success("Soft delete product success!");
    }
}
