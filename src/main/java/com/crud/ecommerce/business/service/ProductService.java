package com.crud.ecommerce.business.service;

import com.crud.ecommerce.business.util.EntityFinderUtils;
import com.crud.ecommerce.business.util.EntityOperationUtils;
import com.crud.ecommerce.business.validation.ProductValidation;
import com.crud.ecommerce.dto.response.product.ProductResponse;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.business.mapper.ProductMapper;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import com.crud.ecommerce.infrastructure.repository.product.ProductRepository;

import com.crud.ecommerce.infrastructure.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidation productValidation;
    private final ProductMapper productMapper;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(String sortBy) {
        return productMapper.toProductResponseList
                (productRepository.findAll(Sort.by(sortBy).ascending()));
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return productMapper.toProductResponse(findProductById(id));
    }

    @Transactional
    public Response createProduct(ProductCreateInput input) {
        productValidation.validateCreate(input);

        Product product = new Product();

        productMapper.applyCreateInput(product, input);
        return EntityOperationUtils.create(productRepository, product, "Produto criado com sucesso");
    }

    @Transactional
    public Response updateProduct(Long id, ProductUpdateInput input) {
        Product product = findProductById(id);

        productValidation.validateUpdate(input);
        productMapper.applyUpdateInput(product, input);
        return EntityOperationUtils.update(productRepository, product, "Produto atualizado com sucesso");
    }

    @Transactional
    public Response deleteProduct(Long id) {
        Product product = findProductById(id);

        EntityOperationUtils.validateNoLinks(reviewRepository.existsByProductId(id),
                "Produto possui avaliações vinculadas e não pode ser deletado.");
        return EntityOperationUtils.delete(productRepository, product, "Produto deletado com sucesso");
    }

    private Product findProductById(Long id) {
        return EntityFinderUtils.findById(productRepository, id, "Produto não encontrado!");
    }
}