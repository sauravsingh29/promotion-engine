package org.saurav.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.saurav.constant.PromoType;
import org.saurav.dto.Promotion;
import org.saurav.exception.CartException;
import org.saurav.exception.PromotionException;
import org.saurav.model.PromoCart;
import org.saurav.processor.PromoOfferProcessor;
import org.saurav.request.Item;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;
import org.saurav.service.ICartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.saurav.constant.PromoType.COMPOUND;
import static org.saurav.constant.PromoType.SINGLE;

/**
 * <p>
 * Main class to process the customer cart item.
 * </p>
 *
 * @author Saurav Singh
 **/
@Service
@Slf4j
public class CartService implements ICartService<ProcessCart> {

    private final List<PromoOfferProcessor<ProcessCart, ProcessedCart>> promoOfferProcessors;

    private final PromotionService promotionService;

    private final ProductService productService;

    public CartService(List<PromoOfferProcessor<ProcessCart, ProcessedCart>> promoOfferProcessors,
                       PromotionService promotionService, ProductService productService) {
        this.promoOfferProcessors = promoOfferProcessors;
        this.promotionService = promotionService;
        this.productService = productService;
    }

    @Override
    public @NonNull ProcessedCart processCart(@NonNull ProcessCart input) throws CartException {
        @NonNull ProcessedCart result;
        log.info("Processing cart with info {}.", input);
        try {
            final List<Promotion> singlePromos = getPromos(SINGLE);
            final List<ProcessedCart> processedCarts = new ArrayList<>(0);
            if (!singlePromos.isEmpty()) {
                final Optional<PromoOfferProcessor<ProcessCart, ProcessedCart>> oSingleProcessor = promoOfferProcessors.stream()
                        .filter(s -> s.type().equals(SINGLE)).findFirst();
                oSingleProcessor.ifPresent(cpp -> processedCarts.add(cpp.process(input, singlePromos)));
            }
            final List<Promotion> compoundPromos = getPromos(COMPOUND);
            if (!compoundPromos.isEmpty()) {
                final Optional<PromoOfferProcessor<ProcessCart, ProcessedCart>> oCompoundProcessor = promoOfferProcessors.stream()
                        .filter(s -> s.type().equals(COMPOUND)).findFirst();
                oCompoundProcessor.ifPresent(spp -> processedCarts.add(spp.process(input, compoundPromos)));
            }
            log.info("After Promo Processing cart  {}.", input);
            final Set<Item> items = input.getItem();
            if (!items.isEmpty()) {
                processedCarts.add(noPromoItems(items));
            }
            final List<PromoCart> promoCarts = new ArrayList<>(0);
            processedCarts.stream().flatMap(p -> p.getCart().stream()).collect(groupingBy(PromoCart::getSku))
                    .forEach((k, v) -> {
                        int q = 0;
                        double price = 0.0;
                        for (PromoCart pc : v) {
                            q += pc.getQuantity();
                            price += pc.getTotal();
                        }
                        final PromoCart promoCart = new PromoCart();
                        promoCart.setSku(k);
                        promoCart.setTotal(price);
                        promoCart.setQuantity(q);
                        promoCarts.add(promoCart);
                    });
            final ProcessedCart processedCart = new ProcessedCart();
            final Double sumTotal = promoCarts.stream().map(PromoCart::getTotal).reduce(0.0, Double::sum);
            processedCart.setCart(promoCarts);
            processedCart.setTotal(sumTotal);
            result = processedCart;
        } catch (PromotionException e) {
            log.error("Unable to get the promotions, Error ", e);
            throw new CartException("Unable to determine promotions", "processCart", e);
        }catch (Exception e) {
            log.error("Failed to process cart {}. Error ", input, e);
            throw new CartException("Failed to process cart", "processCart", e);
        }
        return result;
    }

    /**
     * <p>
     * Process item without promo.
     * </p>
     *
     * @param items input items with quantity.
     * @return ProcessedCart
     */
    public ProcessedCart noPromoItems(final @NonNull Set<Item> items) throws CartException {
        log.info("Processing items without promotion, {}", items);
        final List<PromoCart> promoCarts = items.stream().map(i -> {
            final PromoCart promoCart = new PromoCart();
            final String sku = i.getSku();
            promoCart.setSku(sku);
            final Double price = productService.getPrice(sku);
            final Integer quantity = i.getQuantity();
            promoCart.setQuantity(quantity);
            promoCart.setTotal(quantity * price);
            return promoCart;
        }).collect(toList());
        final ProcessedCart processedCart = new ProcessedCart();
        processedCart.setCart(promoCarts);
        log.info("Finished Processing items without promotion, {}", items);
        return processedCart;
    }

    private List<Promotion> getPromos(final PromoType promoType) throws PromotionException {
        return promotionService.getPromotions().stream().filter(p -> p.getPromoType().equals(promoType)).collect(toList());
    }
}
