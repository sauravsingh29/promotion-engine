package org.saurav.processor.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saurav.constant.DiscountType;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;
import org.saurav.model.SkuPromotion;
import org.saurav.request.Item;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SingleOfferProcessorTest {

    private final SingleOfferProcessor singleOfferProcessor = new SingleOfferProcessor();

    private ProcessCart input;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        input = new ProcessCart();
        final Set<Item> items = new HashSet<>(0);
        items.add(new Item("A", 1));
        items.add(new Item("B", 1));
        items.add(new Item("C", 1));
        input.setItem(items);
        promotion = new Promotion();
        final String name = "3 for A's";
        promotion.setName(name);
        promotion.setActive(true);
        promotion.setPromotions(new HashSet<SkuPromotion>() {{
            add(new SkuPromotion("A", 3));
        }});
        promotion.setPromoType(PromoType.SINGLE);
        promotion.setDiscountType(DiscountType.AMOUNT);
        promotion.setValue(70.0);

    }

    @Test
    void processWithoutPromo() throws Exception {
        final List<Promotion> promotions = Collections.singletonList(promotion);
        final ProcessedCart processedCart = singleOfferProcessor.process(input, promotions);
        assertNotNull(processedCart);
        assertEquals(0, processedCart.getCart().size());
    }

    @Test
    void process() throws Exception {
        Promotion promotionA = new Promotion();
        final String name = "1 for A's";
        promotionA.setName(name);
        promotionA.setActive(true);
        promotionA.setPromoType(PromoType.SINGLE);
        promotionA.setDiscountType(DiscountType.AMOUNT);
        promotionA.setPromotions(new HashSet<SkuPromotion>() {{
            add(new SkuPromotion("A", 1));
        }});
        promotionA.setValue(30.0);
        final List<Promotion> promotions = Arrays.asList(promotion, promotionA);
        final ProcessedCart processedCart = singleOfferProcessor.process(input, promotions);
        assertNotNull(processedCart);
        assertEquals(1, processedCart.getCart().size());
    }
}