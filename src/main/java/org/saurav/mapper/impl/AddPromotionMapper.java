package org.saurav.mapper.impl;

import lombok.NonNull;
import org.saurav.dto.Promotion;
import org.saurav.mapper.Mapper;
import org.saurav.model.SkuPromotion;
import org.saurav.request.AddPromotion;

import java.util.Set;

import static org.saurav.constant.PromoType.COMPOUND;
import static org.saurav.constant.PromoType.SINGLE;

/**
 * <p>
 * Request to DTO mapper.
 * </p>
 *
 * @author Saurav Singh
 **/
public class AddPromotionMapper implements Mapper<AddPromotion, Promotion> {

    @Override
    public @NonNull Promotion map(@NonNull AddPromotion request) {
        final Promotion promotion = new Promotion();
        promotion.setName(request.getName());
        final Set<SkuPromotion> promotions = request.getPromotions();
        promotion.setPromotions(promotions);
        promotion.setDiscountType(request.getDiscountType());
        promotion.setValue(request.getValue());
        promotion.setActive(request.getActive());
        promotion.setPromoType(promotions.size() > 1 ? COMPOUND : SINGLE);
        return promotion;
    }
}
