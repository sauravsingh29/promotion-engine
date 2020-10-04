package org.saurav.processor.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;
import org.saurav.model.PromoCart;
import org.saurav.model.SkuPromotion;
import org.saurav.processor.PromoOfferProcessor;
import org.saurav.request.Item;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static org.saurav.constant.PromoType.SINGLE;

/**
 * @author Saurav Singh
 **/
@Component
@Slf4j
public class SingleOfferProcessor implements PromoOfferProcessor<ProcessCart, ProcessedCart> {

    @Override
    public ProcessedCart process(@NonNull ProcessCart input, @NonNull List<Promotion> promotions) {
        final List<PromoCart> promoCarts = new ArrayList<>(0);
        final Set<Item> items = new HashSet<>(input.getItem());
        promotions.stream().filter(Promotion::getActive).forEach(p -> {
            final Map<String, Integer> promos = p.getPromotions().stream().collect(groupingBy(SkuPromotion::getSku, summingInt(SkuPromotion::getQuantity)));
            input.getItem().stream().filter(i -> {
                final Integer quantity = promos.get(i.getSku());
                if (null == quantity) {
                    return false;
                }
                return i.getQuantity() >= quantity && quantity > 0;
            }).forEach(i -> {
                final String sku = i.getSku();
                final Integer quantity = i.getQuantity();
                final Integer promoQuantity = promos.get(sku);
                final int remain = quantity % promoQuantity;
                final int applied = quantity / promoQuantity;
                final PromoCart promoCart = new PromoCart();
                promoCart.setSku(sku);
                promoCart.setQuantity(quantity - remain);
                promoCart.setTotal(p.getValue() * applied);
                promoCarts.add(promoCart);
                items.removeIf(d -> d.getSku().equalsIgnoreCase(sku));
                if (remain > 0) {
                    final Item leftItem = new Item();
                    leftItem.setSku(sku);
                    leftItem.setQuantity(remain);
                    items.add(leftItem);
                }
            });
        });
        final ProcessedCart processedCart = new ProcessedCart();
        processedCart.setCart(promoCarts);
        input.setItem(items);
        return processedCart;
    }

    @Override
    public PromoType type() {
        return SINGLE;
    }
}
