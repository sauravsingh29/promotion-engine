package org.saurav.api;

import org.saurav.constant.DiscountType;
import org.saurav.dto.Promotion;
import org.saurav.exception.PromotionException;
import org.saurav.facade.PromotionServiceFacade;
import org.saurav.model.SkuPromotion;
import org.saurav.request.AddPromotion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author Saurav Singh
 **/
@RestController
@RequestMapping("/promotion")
public class PromotionApi {

    private final PromotionServiceFacade promotionServiceFacade;

    public PromotionApi(PromotionServiceFacade promotionServiceFacade) {
        this.promotionServiceFacade = promotionServiceFacade;
    }

    @PostConstruct
    public void init() throws PromotionException {
        final AddPromotion a = new AddPromotion();
        a.setName("3 of A's");
        a.setActive(true);
        a.setValue(130.0);
        a.setDiscountType(DiscountType.AMOUNT);
        final Set<SkuPromotion> aSet = new HashSet<>(0);
        aSet.add(new SkuPromotion("A", 3));
        a.setPromotions(aSet);
        promotionServiceFacade.addPromotion(a);
        final AddPromotion b = new AddPromotion();
        b.setActive(true);
        b.setName("2 of B's");
        b.setValue(45.0);
        b.setDiscountType(DiscountType.AMOUNT);
        final Set<SkuPromotion> bSet = new HashSet<>(0);
        bSet.add(new SkuPromotion("B", 2));
        b.setPromotions(bSet);
        promotionServiceFacade.addPromotion(b);
        final AddPromotion cd = new AddPromotion();
        cd.setActive(true);
        cd.setName("C & D");
        cd.setValue(30.0);
        cd.setDiscountType(DiscountType.AMOUNT);
        final Set<SkuPromotion> cdSet = new HashSet<>(0);
        cdSet.add(new SkuPromotion("C", 1));
        cdSet.add(new SkuPromotion("D", 1));
        cd.setPromotions(cdSet);
        promotionServiceFacade.addPromotion(cd);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addPromotion(final @RequestBody @Valid AddPromotion addPromotion) throws PromotionException {
        promotionServiceFacade.addPromotion(addPromotion);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getPromotions() throws PromotionException {
        final List<Promotion> promotions = promotionServiceFacade.getPromotions();
        return ResponseEntity.ok().body(promotions);
    }
}
