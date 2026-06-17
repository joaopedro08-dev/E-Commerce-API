package com.crud.ecommerce.controller;

import com.crud.ecommerce.dto.response.product.ProductResponse;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.business.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductResponse> getAllProducts(@RequestParam(defaultValue = "name") String sortBy) {
        return productService.getAllProducts(sortBy);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Response createProduct(@RequestBody ProductCreateInput input) {
        return productService.createProduct(input);
    }

    @PutMapping("/{id}")
    public Response updateProduct(@PathVariable Long id, @RequestBody ProductUpdateInput input) {
        return productService.updateProduct(id, input);
    }

    @DeleteMapping("/{id}")
    public Response deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}