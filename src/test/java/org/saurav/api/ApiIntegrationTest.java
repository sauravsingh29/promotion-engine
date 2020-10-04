package org.saurav.api;

import org.junit.jupiter.api.Test;
import org.saurav.constant.DiscountType;
import org.saurav.exception.model.ApiError;
import org.saurav.model.SkuPromotion;
import org.saurav.request.AddPromotion;
import org.saurav.request.Item;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void processCartTest() {
        ProcessCart processCart = new ProcessCart();
        final Set<Item> items = new HashSet<>(0);
        items.add(new Item("A", 1));
        items.add(new Item("B", 1));
        items.add(new Item("C", 1));
        processCart.setItem(items);
        String url = String.format("http://localhost:%d/cart", port);
        ResponseEntity<ProcessedCart> response = restTemplate.postForEntity(url, processCart, ProcessedCart.class);

        assertEquals(OK, response.getStatusCode());
        final ProcessedCart body = response.getBody();
        assertNotNull(body);
        assertEquals(100, body.getTotal());
    }

    @Test
    void addPromotionTest() {
        final AddPromotion promotion = new AddPromotion();
        promotion.setName("3 of C's");
        promotion.setActive(true);
        promotion.setValue(130.0);
        promotion.setDiscountType(DiscountType.AMOUNT);
        final Set<SkuPromotion> aSet = new HashSet<>(0);
        aSet.add(new SkuPromotion("C", 3));
        promotion.setPromotions(aSet);
        String url = String.format("http://localhost:%d/promotion", port);
        ResponseEntity<Void> response = restTemplate.postForEntity(url, promotion, Void.class);

        assertEquals(CREATED, response.getStatusCode());
    }

    @Test
    void existsPromotionTest() {
        final AddPromotion promotion = new AddPromotion();
        promotion.setName("3 of C's");
        promotion.setActive(true);
        promotion.setValue(130.0);
        promotion.setDiscountType(DiscountType.AMOUNT);
        final Set<SkuPromotion> aSet = new HashSet<>(0);
        aSet.add(new SkuPromotion("C", 3));
        promotion.setPromotions(aSet);
        String url = String.format("http://localhost:%d/promotion", port);
        ResponseEntity<ApiError> response = restTemplate.postForEntity(url, promotion, ApiError.class);

        assertEquals(BAD_REQUEST, response.getStatusCode());
    }
}