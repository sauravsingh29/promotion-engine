package org.saurav.api;

import io.swagger.annotations.ApiOperation;
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
public class CartProcessingApi {

    private final CartService cartService;

    public CartProcessingApi(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @ApiOperation(value = "User cart processor", notes = "Process user's cart; apply promotion if any applicable and return total amount.")
    public ResponseEntity<ProcessedCart> processCart(@RequestBody final ProcessCart processCart) throws CartException {
        final ProcessedCart processedCart = cartService.processCart(processCart);
        return ResponseEntity.ok(processedCart);
    }
}
