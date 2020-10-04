package org.saurav.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.saurav.constant.DiscountType;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;
import org.saurav.exception.PromotionException;
import org.saurav.model.SkuPromotion;
import org.saurav.request.AddPromotion;
import org.saurav.service.impl.ProductService;
import org.saurav.service.impl.PromotionService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PromotionServiceFacadeTest {

    @Mock
    private ProductService productService;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionServiceFacade promotionServiceFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addPromotion() throws Exception {
        Set<String> skus = new HashSet<>(0);
        when(productService.validateSkus(anySet())).thenReturn(skus);
        promotionServiceFacade.addPromotion(buildInput());
        verify(promotionService, times(1)).addPromotion(any(AddPromotion.class));
    }

    @Test
    void addInvalidPromotion() throws Exception {
        Set<String> skus = new HashSet<>(0);
        skus.add("E");
        when(productService.validateSkus(anySet())).thenReturn(skus);
        PromotionException promotionException = assertThrows(PromotionException.class, () -> {
            promotionServiceFacade.addPromotion(buildInput());
        });
        assertTrue(promotionException.getMessage().equalsIgnoreCase("One or more sku's are invalid."));
    }

    private AddPromotion buildInput() {
        final AddPromotion promotion = new AddPromotion();
        promotion.setName("3 of C's");
        promotion.setActive(true);
        promotion.setValue(130.0);
        promotion.setDiscountType(DiscountType.AMOUNT);
        final Set<SkuPromotion> aSet = new HashSet<>(0);
        aSet.add(new SkuPromotion("C", 3));
        promotion.setPromotions(aSet);
        return promotion;
    }

    @Test
    void getPromotions() throws Exception  {
        Promotion promotion = new Promotion();
        final String name = "3 for A's";
        promotion.setName(name);
        promotion.setPromotions(new HashSet<SkuPromotion>() {{ add(new SkuPromotion("A", 3));}});
        promotion.setPromoType(PromoType.SINGLE);
        promotion.setDiscountType(DiscountType.AMOUNT);
        promotion.setValue(130.0);
        final List<Promotion> promotions = Collections.singletonList(promotion);
        when(promotionService.getPromotions()).thenReturn(promotions);
        final List<Promotion> actual = promotionServiceFacade.getPromotions();
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(name, actual.get(0).getName());
    }
}