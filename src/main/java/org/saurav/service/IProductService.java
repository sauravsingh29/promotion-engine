package org.saurav.service;

import lombok.NonNull;
import org.saurav.dto.Product;
import org.saurav.exception.ProductException;
import org.saurav.request.AddProduct;
import org.saurav.request.AddPromotion;

import java.util.Set;

/**
 * @author Saurav Singh
 **/
public interface IProductService {

    /**
     * <p>
     * Add new promotion in system.
     * </p>
     *
     * @param addProduct {@link AddPromotion}
     * @throws ProductException can be thrown.
     */
    void addProduct(final @NonNull AddProduct addProduct) throws ProductException;

    /**
     * <p>
     * Get all products in system.
     * </p>
     *
     * @return products {@link Set<Product>}
     * @throws ProductException can be thrown.
     */
    Set<Product> getProducts() throws ProductException;

    /**
     * <p>
     * Validate given set of SKU's from product catalog, if all found then return empty set otherwise
     * return set of skus.
     * </p>
     *
     * @param skus {@link Set<String>}
     * @return empty or set of string {@link Set<String>}
     */
    Set<String> validateSkus(final @NonNull Set<String> skus);

    Double getPrice(final @NonNull String sku);
}
