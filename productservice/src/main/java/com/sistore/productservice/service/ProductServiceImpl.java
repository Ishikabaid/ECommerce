package com.sistore.productservice.service;

import com.sistore.productservice.dto.InventoryRequest;
import com.sistore.productservice.dto.InventoryResponse;
import com.sistore.productservice.dto.ProductRequest;
import com.sistore.productservice.dto.ProductResponse;
import com.sistore.productservice.entity.Inventory;
import com.sistore.productservice.entity.Product;
import com.sistore.productservice.exception.ProductNotFoundException;
import com.sistore.productservice.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductResponse saveProduct(ProductRequest product) {
        Product product1 = mapToProduct(product);
        return mapToProductResponse(productRepository.save(product1));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse).toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found", HttpStatus.NOT_FOUND));
        return mapToProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (Objects.nonNull(product))
            productRepository.delete(product);
        return Objects.nonNull(product) ? mapToProductResponse(product) : null;
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product currentProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found", HttpStatus.NOT_FOUND));

        currentProduct.setName(productRequest.getName());
        currentProduct.setDescription(productRequest.getDescription());
        currentProduct.setPrice(productRequest.getPrice());
        currentProduct.setCategory(productRequest.getCategory());
        currentProduct.setBrand(productRequest.getBrand());
        currentProduct.setAvailable(productRequest.getAvailable());
        currentProduct.setInventory(updateInventory(currentProduct.getInventory(), productRequest.getInventory()));
        Product updatedProduct = productRepository.saveAndFlush(currentProduct);

        return mapToProductResponse(updatedProduct);
    }

    public ProductResponse mapToProductResponse(Product product) {
        ProductResponse productDto = new ProductResponse();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        productDto.setBrand(product.getBrand());
        productDto.setAvailable(product.getAvailable());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setUpdatedAt(product.getUpdatedAt());
        productDto.setStock(product.getInventory().getStock());
        return productDto;
    }

    public Product mapToProduct(ProductRequest productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setBrand(productDto.getBrand());
        product.setAvailable(productDto.getAvailable());
        product.setInventory(mapToInventory(productDto.getInventory()));
        return product;
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        if (Objects.isNull(inventory)) {
            return null;
        }
        InventoryResponse inventoryResponse = new InventoryResponse();
        inventoryResponse.setInventoryId(inventory.getInventoryId());
        inventoryResponse.setStock(inventory.getStock());
        return inventoryResponse;
    }

    private Inventory mapToInventory(InventoryRequest inventoryRequest) {
        if (Objects.isNull(inventoryRequest)) {
            return null;
        }
        Inventory inventory = new Inventory();
        inventory.setStock(inventoryRequest.getStock());
        return inventory;
    }


    private Inventory updateInventory(Inventory currentInventory, InventoryRequest inventoryRequest) {
        if (Objects.isNull(inventoryRequest)) {
            return null;
        }
        currentInventory.setStock(inventoryRequest.getStock());
        return currentInventory;
    }
}
