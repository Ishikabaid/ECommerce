package com.sistore.productservice.controller;

import com.sistore.productservice.dto.ProductRequest;
import com.sistore.productservice.dto.ProductResponse;
import com.sistore.productservice.exception.ProductNotFoundException;
import com.sistore.productservice.aspect.LogExecutionTime;
import com.sistore.productservice.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @PostMapping
    @Transactional
    @LogExecutionTime
    public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductRequest product){
        ProductResponse productResponse = productService.saveProduct(product);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        List<ProductResponse> allProducts = productService.getAllProducts();
        if(allProducts.isEmpty()){
            throw new ProductNotFoundException("No Records Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        ProductResponse productResponse = productService.deleteProduct(id);
        if(Objects.isNull(productResponse)){
            throw new ProductNotFoundException("No Product Available to Delete", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @LogExecutionTime
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody(required = true) ProductRequest product){
        ProductResponse productResponse = productService.updateProduct(id, product);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
