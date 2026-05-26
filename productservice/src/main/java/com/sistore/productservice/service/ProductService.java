package com.sistore.productservice.service;

import com.sistore.productservice.dto.ProductRequest;
import com.sistore.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse saveProduct(ProductRequest product);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse deleteProduct(Long id);

    ProductResponse updateProduct(Long id, ProductRequest product);
}
