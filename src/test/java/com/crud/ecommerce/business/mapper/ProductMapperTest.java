package com.crud.ecommerce.business.mapper;

import com.crud.ecommerce.dto.response.product.ProductResponse;
import com.crud.ecommerce.dto.resquest.product.ProductCreateInput;
import com.crud.ecommerce.dto.resquest.product.ProductUpdateInput;
import com.crud.ecommerce.infrastructure.entity.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    // ==================== toProductResponse ====================

    @Test
    void shouldMapProductToProductResponse() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setDescription("Descrição válida com mais de vinte caracteres aqui.");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStock(10);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        ProductResponse response = productMapper.toProductResponse(product);

        assertEquals(product.getId(), response.id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getStock(), response.stock());
    }

    // ==================== toProductResponseList ====================

    @Test
    void shouldMapProductListToProductResponseList() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Produto Um");
        product1.setDescription("Descrição válida com mais de vinte caracteres aqui.");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setStock(10);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Produto Dois");
        product2.setDescription("Descrição válida com mais de vinte caracteres aqui.");
        product2.setPrice(BigDecimal.valueOf(200));
        product2.setStock(20);

        List<ProductResponse> responses = productMapper.toProductResponseList(List.of(product1, product2));

        assertEquals(2, responses.size());
        assertEquals("Produto Um", responses.get(0).name());
        assertEquals("Produto Dois", responses.get(1).name());
    }

    @Test
    void shouldReturnEmptyListWhenProductListIsEmpty() {
        List<ProductResponse> responses = productMapper.toProductResponseList(List.of());

        assertTrue(responses.isEmpty());
    }

    // ==================== applyCreateInput ====================

    @Test
    void shouldApplyCreateInputToProduct() {
        Product product = new Product();
        ProductCreateInput input = new ProductCreateInput(
                "Produto Novo",
                "Descrição válida com mais de vinte caracteres aqui.",
                BigDecimal.valueOf(150),
                30
        );

        productMapper.applyCreateInput(product, input);

        assertEquals("Produto Novo", product.getName());
        assertEquals("Descrição válida com mais de vinte caracteres aqui.", product.getDescription());
        assertEquals(BigDecimal.valueOf(150), product.getPrice());
        assertEquals(30, product.getStock());
    }

    // ==================== applyUpdateInput ====================

    @Test
    void shouldApplyUpdateInputOnlyToProvidedFields() {
        Product product = new Product();
        product.setName("Nome Antigo");
        product.setDescription("Descrição antiga com mais de vinte caracteres aqui.");
        product.setPrice(BigDecimal.valueOf(50));
        product.setStock(5);

        ProductUpdateInput input = new ProductUpdateInput(
                "Nome Novo", null, null, null
        );

        productMapper.applyUpdateInput(product, input);

        assertEquals("Nome Novo", product.getName());
        assertEquals("Descrição antiga com mais de vinte caracteres aqui.", product.getDescription());
        assertEquals(BigDecimal.valueOf(50), product.getPrice());
        assertEquals(5, product.getStock());
    }

    @Test
    void shouldNotChangeProductWhenAllUpdateFieldsAreNull() {
        Product product = new Product();
        product.setName("Nome Original");
        product.setDescription("Descrição original com mais de vinte caracteres aqui.");
        product.setPrice(BigDecimal.valueOf(80));
        product.setStock(15);

        ProductUpdateInput input = new ProductUpdateInput(null, null, null, null);

        productMapper.applyUpdateInput(product, input);

        assertEquals("Nome Original", product.getName());
        assertEquals(BigDecimal.valueOf(80), product.getPrice());
        assertEquals(15, product.getStock());
    }

    @Test
    void shouldApplyAllUpdateFieldsWhenAllAreProvided() {
        Product product = new Product();
        product.setName("Nome Antigo");
        product.setDescription("Descrição antiga com mais de vinte caracteres aqui.");
        product.setPrice(BigDecimal.valueOf(50));
        product.setStock(5);

        ProductUpdateInput input = new ProductUpdateInput(
                "Nome Atualizado",
                "Descrição atualizada com mais de vinte caracteres aqui.",
                BigDecimal.valueOf(200),
                40
        );

        productMapper.applyUpdateInput(product, input);

        assertEquals("Nome Atualizado", product.getName());
        assertEquals("Descrição atualizada com mais de vinte caracteres aqui.", product.getDescription());
        assertEquals(BigDecimal.valueOf(200), product.getPrice());
        assertEquals(40, product.getStock());
    }
}