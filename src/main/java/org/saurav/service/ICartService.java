package org.saurav.service;

import lombok.NonNull;
import org.saurav.exception.CartException;
import org.saurav.response.ProcessedCart;

/**
 * @author Saurav Singh
 **/
public interface ICartService<I> {

    @NonNull ProcessedCart processCart(final @NonNull I input) throws CartException;
}
