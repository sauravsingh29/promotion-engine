package org.saurav.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.saurav.constant.DiscountType;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;
import org.saurav.model.PromoCart;
import org.saurav.model.SkuPromotion;
import org.saurav.processor.PromoOfferProcessor;
import org.saurav.processor.impl.CompoundOfferProcessor;
import org.saurav.processor.impl.SingleOfferProcessor;
import org.saurav.request.Item;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.saurav.constant.PromoType.SINGLE;

class CartServiceTest {

    private static ProcessCart processCart;

    @Spy
    private List<PromoOfferProcessor<ProcessCart, ProcessedCart>> promoOfferProcessors = new ArrayList<>();

    @Mock
    private SingleOfferProcessor singleOfferProcessor;

    @Mock
    private CompoundOfferProcessor compoundOfferProcessor;

    @Mock
    private PromotionService promotionService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        final Set<Item> items = new HashSet<>(0);
        items.add(new Item("A", 3));
        processCart = new ProcessCart();
        processCart.setItem(items);
        promoOfferProcessors.add(singleOfferProcessor);
        promoOfferProcessors.add(compoundOfferProcessor);
    }

    @Test
    void processCart() throws Exception {
        when(promotionService.getPromotions()).thenReturn(getPromotions());
        when(singleOfferProcessor.type()).thenReturn(SINGLE);
        ProcessedCart processedCart = new ProcessedCart();
        PromoCart promoCart = new PromoCart();
        promoCart.setQuantity(3);
        promoCart.setSku("A");
        promoCart.setTotal(130.0);
        processedCart.setCart(Collections.singletonList(promoCart));
        processedCart.setTotal(130.0);
        when(singleOfferProcessor.process(any(ProcessCart.class), anyList())).thenReturn(processedCart);
        final ProcessedCart expected = cartService.processCart(processCart);
        assertNotNull(expected);
        assertEquals(processedCart.getTotal(), expected.getTotal());
    }


    private List<Promotion> getPromotions() {
        Promotion promotion = new Promotion();
        promotion.setDiscountType(DiscountType.AMOUNT);
        promotion.setPromoType(PromoType.SINGLE);
        promotion.setName("3 for A's");
        promotion.setValue(130.0);
        promotion.setActive(true);
        promotion.setPromotions(new HashSet<SkuPromotion>() {{
            add(new SkuPromotion("A", 3));
        }});
        Promotion promotion1 = new Promotion();
        promotion1.setDiscountType(DiscountType.AMOUNT);
        promotion1.setPromoType(PromoType.SINGLE);
        promotion1.setName("2 for B's");
        promotion1.setValue(130.0);
        promotion1.setActive(true);
        promotion1.setPromotions(new HashSet<SkuPromotion>() {{
            add(new SkuPromotion("B", 2));
        }});
        return Arrays.asList(promotion, promotion1);
    }
}