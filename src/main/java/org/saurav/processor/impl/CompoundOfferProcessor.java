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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toSet;
import static org.saurav.constant.PromoType.COMPOUND;

/**
 * <p>
 * Buy Multiple products, check if offer configured  for any combination in in the cart.
 * </p>
 *
 * @author Saurav Singh
 **/
@Component
@Slf4j
public class CompoundOfferProcessor implements PromoOfferProcessor<ProcessCart, ProcessedCart> {

    @Override
    public ProcessedCart process(@NonNull ProcessCart input, final @NonNull List<Promotion> promotions) {
        final List<PromoCart> promoCarts = new ArrayList<>(0);
        final Set<Item> items = new HashSet<>(input.getItem());
        promotions.stream().filter(Promotion::getActive).forEachOrdered(p -> {
            final Set<SkuPromotion> skuPromotionSet = p.getPromotions();
            final Set<String> compoundItems = skuPromotionSet.stream().map(SkuPromotion::getSku).collect(toSet());
            final Set<String> filteredSet = compoundItems.stream().filter(cs -> items.stream().anyMatch(it -> it.getSku().equalsIgnoreCase(cs))).collect(toSet());
            if (!filteredSet.isEmpty() && filteredSet.size() == compoundItems.size() && filteredSet.containsAll(compoundItems)) {
                log.trace("Found promotion for SKUs {}.", compoundItems);
                final Double promoValue = p.getValue();
                final AtomicBoolean processedFirst = new AtomicBoolean(false);
                final Map<String, Integer> matchItem = items.stream().filter(i -> compoundItems.contains(i.getSku())).collect(groupingBy(Item::getSku, summingInt(Item::getQuantity)));
                final Optional<Integer> oMin = matchItem.values().stream().min(Integer::compareTo);
                promoProcessing(promoCarts, items, promoValue, processedFirst, matchItem, oMin);
            }
        });
        final ProcessedCart processedCart = new ProcessedCart();
        processedCart.setCart(promoCarts);
        input.setItem(items);
        return processedCart;
    }

    private void promoProcessing(List<PromoCart> promoCarts, Set<Item> items, Double promoValue,
                                 AtomicBoolean processedFirst, Map<String, Integer> matchItem, Optional<Integer> oMin) {
        if (oMin.isPresent()) {
            final int min = oMin.get();
            matchItem.forEach((k, v) -> {
                final PromoCart promoCart = new PromoCart();
                promoCart.setSku(k);
                promoCart.setQuantity(min);
                if (processedFirst.get()) {
                    promoCart.setTotal(0.0);
                } else {
                    promoCart.setTotal(promoValue * min);
                    processedFirst.set(true);
                }
                promoCarts.add(promoCart);
                if (min == v) {
                    items.removeIf(i -> i.getSku().equalsIgnoreCase(k));
                } else {
                    items.stream().filter(i -> i.getSku().equalsIgnoreCase(k)).forEach(i -> i.setQuantity(v - min));
                }
            });
        }
    }

    @Override
    public PromoType type() {
        return COMPOUND;
    }
}
