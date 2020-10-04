package org.saurav.facade;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.saurav.dto.Promotion;
import org.saurav.exception.PromotionException;
import org.saurav.model.SkuPromotion;
import org.saurav.request.AddPromotion;
import org.saurav.service.impl.ProductService;
import org.saurav.service.impl.PromotionService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * <p>
 * Facade layer for promotion to validate the SKUs.
 * </p>
 *
 * @author Saurav Singh
 **/
@Component
@Slf4j
public class PromotionServiceFacade {

    private final ProductService productService;

    private final PromotionService promotionService;

    public PromotionServiceFacade(ProductService productService, PromotionService promotionService) {
        this.productService = productService;
        this.promotionService = promotionService;
    }

    public void addPromotion(final @NonNull AddPromotion addPromotion) throws PromotionException {
        final Set<String> skus = addPromotion.getPromotions().stream().map(SkuPromotion::getSku).collect(toSet());
        final Set<String> invalidSkus = productService.validateSkus(skus);
        if (null == invalidSkus || invalidSkus.isEmpty()) {
            promotionService.addPromotion(addPromotion);
        } else {
            throw new PromotionException("One or more sku's are invalid.", "Invalid SKUs: " + invalidSkus, BAD_REQUEST);
        }
    }

    public List<Promotion> getPromotions() throws PromotionException {
        return promotionService.getPromotions();
    }
}
