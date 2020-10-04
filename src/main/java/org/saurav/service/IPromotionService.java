package org.saurav.service;

import lombok.NonNull;
import org.saurav.dto.Promotion;
import org.saurav.exception.PromotionException;
import org.saurav.request.AddPromotion;

import java.util.List;

/**
 * @author Saurav Singh
 **/
public interface IPromotionService {

    /**
     * <p>
     * Add new promotion in system.
     * </p>
     *
     * @param addPromotion {@link AddPromotion}
     * @throws PromotionException can be thrown.
     */
    void addPromotion(final @NonNull AddPromotion addPromotion) throws PromotionException;

    /**
     * <p>
     * Get all promotions in system.
     * </p>
     *
     * @return promotions {@link List<Promotion>}
     * @throws PromotionException can be thrown.
     */
    List<Promotion> getPromotions() throws PromotionException;
}
