package org.saurav.api;

import org.saurav.exception.CartException;
import org.saurav.request.ProcessCart;
import org.saurav.response.ProcessedCart;
import org.saurav.service.impl.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Saurav Singh
 **/
@RestController
@RequestMapping("/cart")
public class CartApi {

    private final CartService cartService;

    public CartApi(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<ProcessedCart> processCart(@RequestBody final ProcessCart processCart) throws CartException {
        final ProcessedCart processedCart = cartService.processCart(processCart);
        return ResponseEntity.ok(processedCart);
    }
}
