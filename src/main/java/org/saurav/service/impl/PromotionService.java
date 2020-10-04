package org.saurav.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.saurav.dto.Promotion;
import org.saurav.exception.PromotionException;
import org.saurav.mapper.impl.AddPromotionMapper;
import org.saurav.request.AddPromotion;
import org.saurav.service.IPromotionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.synchronizedList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Saurav Singh
 **/
@Slf4j
@Service
public class PromotionService implements IPromotionService {

    private static final List<Promotion> PROMOTIONS = synchronizedList(new ArrayList<>(0));

    @Override
    public void addPromotion(@NonNull AddPromotion addPromotion) throws PromotionException {
        log.debug("Adding promotion {}", addPromotion);
        try {
            final Promotion promotion = new AddPromotionMapper().map(addPromotion);
            final Optional<Promotion> optionalPromotion = PROMOTIONS.stream().filter(p -> p.getName().equalsIgnoreCase(promotion.getName())).findFirst();
            if (optionalPromotion.isPresent()) {
                throw new PromotionException("Promotion name exists", "addPromotion", BAD_REQUEST);
            }
            PROMOTIONS.add(promotion);
            log.debug("Added promotion {}", addPromotion);
        } catch (PromotionException e) {
            log.error("Promotion error ", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to add promotion for request {}.", addPromotion, e);
            throw new PromotionException("Failed to add promotion", "addPromotion", e);
        }
    }

    @Override
    public List<Promotion> getPromotions() throws PromotionException {
        log.debug("Getting all promotions from system.");
        try {
            return PROMOTIONS;
        } catch (Exception e) {
            log.error("Failed to get promotions.", e);
            throw new PromotionException("Failed to get promotions", "getPromotions", e);
        }
    }
}
