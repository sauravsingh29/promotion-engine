package org.saurav.processor.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saurav.constant.DiscountType;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;
import org.saurav.model.PromoCart;
import org.saurav.model.SkuPromotion;
import org.saurav.request.Item;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompoundOfferProcessorTest {

    private final CompoundOfferProcessor compoundOfferProcessor = new CompoundOfferProcessor();

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
            add(new SkuPromotion("C", 3));
        }});
        promotion.setPromoType(PromoType.COMPOUND);
        promotion.setDiscountType(DiscountType.AMOUNT);
        promotion.setValue(70.0);

    }

    @Test
    void process() throws Exception {
        final List<Promotion> promotions = Collections.singletonList(promotion);
        final ProcessedCart processedCart = compoundOfferProcessor.process(input, promotions);
        assertNotNull(processedCart);
        final PromoCart promoCart = processedCart.getCart().stream().max(Comparator.comparing(PromoCart::getTotal)).orElse(new PromoCart());
        assertEquals(70.0, promoCart.getTotal());
    }
}