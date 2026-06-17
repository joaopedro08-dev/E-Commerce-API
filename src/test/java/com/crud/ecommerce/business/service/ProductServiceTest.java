package com.crud.ecommerce.business.service;

import com.crud.ecommerce.business.mapper.ProductMapper;
import com.crud.ecommerce.business.validation.ProductValidation;
import com.crud.ecommerce.dto.response.Response;
import com.crud.ecommerce.dto.response.product.ProductResponse;
import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.exception.ConflictException;
import com.crud.ecommerce.exception.NotFoundException;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import com.crud.ecommerce.infrastructure.repository.product.ProductRepository;
import com.crud.ecommerce.infrastructure.repository.review.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidation productValidation;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ProductService productService;

    private Product validProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setDescription("Descrição válida com mais de vinte caracteres aqui.");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStock(10);
        return product;
    }

    private ProductCreateInput validCreateInput() {
        return new ProductCreateInput(
                "Produto Teste",
                "Descrição válida com mais de vinte caracteres aqui.",
                BigDecimal.valueOf(100),
                10
        );
    }

    // ==================== getAllProducts ====================

    @Test
    void shouldReturnAllProductsSorted() {
        List<Product> products = List.of(validProduct());
        List<ProductResponse> expectedResponses = List.of(
                new ProductResponse(1L, "Produto Teste", "Descrição válida com mais de vinte caracteres aqui.",
                        BigDecimal.valueOf(100), 10, null, null)
        );

        when(productRepository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(products);
        when(productMapper.toProductResponseList(products)).thenReturn(expectedResponses);

        List<ProductResponse> result = productService.getAllProducts("name");

        assertEquals(expectedResponses, result);
        verify(productRepository).findAll(any(org.springframework.data.domain.Sort.class));
    }

    // ==================== getProductById ====================

    @Test
    void shouldReturnProductByIdWhenFound() {
        Product product = validProduct();
        ProductResponse expectedResponse = new ProductResponse(1L, "Produto Teste",
                "Descrição válida com mais de vinte caracteres aqui.", BigDecimal.valueOf(100), 10, null, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(expectedResponse);

        ProductResponse result = productService.getProductById(1L);

        assertEquals(expectedResponse, result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(99L));
    }

    // ==================== createProduct ====================

    @Test
    void shouldCreateProductSuccessfully() {
        ProductCreateInput input = validCreateInput();

        doNothing().when(productValidation).validateCreate(input);
        when(productRepository.save(any(Product.class))).thenReturn(validProduct());

        Response response = productService.createProduct(input);

        assertTrue(response.success());
        assertEquals("Produto criado com sucesso", response.message());
        verify(productValidation).validateCreate(input);
        verify(productMapper).applyCreateInput(any(Product.class), eq(input));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldNotSaveProductWhenValidationFails() {
        ProductCreateInput input = validCreateInput();

        doThrow(new com.crud.ecommerce.exception.BadRequestException("Dados inválidos"))
                .when(productValidation).validateCreate(input);

        assertThrows(com.crud.ecommerce.exception.BadRequestException.class,
                () -> productService.createProduct(input));

        verify(productRepository, never()).save(any(Product.class));
    }

    // ==================== updateProduct ====================

    @Test
    void shouldUpdateProductSuccessfully() {
        Product existingProduct = validProduct();
        ProductUpdateInput input = new ProductUpdateInput("Nome Atualizado", null, null, null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Response response = productService.updateProduct(1L, input);

        assertTrue(response.success());
        assertEquals("Produto atualizado com sucesso", response.message());
        verify(productValidation).validateUpdate(input);
        verify(productMapper).applyUpdateInput(existingProduct, input);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentProduct() {
        ProductUpdateInput input = new ProductUpdateInput("Nome Atualizado", null, null, null);

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(99L, input));
        verify(productRepository, never()).save(any(Product.class));
    }

    // ==================== deleteProduct ====================

    @Test
    void shouldDeleteProductSuccessfully() {
        Product product = validProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByProductId(1L)).thenReturn(false);

        Response response = productService.deleteProduct(1L);

        assertTrue(response.success());
        assertEquals("Produto deletado com sucesso", response.message());
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentProduct() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(99L));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenProductHasLinkedReviews() {
        Product product = validProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.existsByProductId(1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any(Product.class));
    }
}