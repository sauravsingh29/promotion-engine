package org.saurav.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.saurav.dto.Product;
import org.saurav.exception.ProductException;
import org.saurav.mapper.impl.AddProductMapper;
import org.saurav.request.AddProduct;
import org.saurav.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.synchronizedSet;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author Saurav Singh
 **/
@Service
@Slf4j
public class ProductService implements IProductService {

    private static final Set<Product> PRODUCTS = synchronizedSet(new HashSet<>());

    @Override
    public void addProduct(@NonNull AddProduct addProduct) throws ProductException {
        log.debug("Adding product {}.", addProduct);
        try {
            final Product product = new AddProductMapper().map(addProduct);
            if (PRODUCTS.contains(product)) {
                throw new ProductException("Product already exists", "addProduct", CONFLICT);
            }
            PRODUCTS.add(product);
            log.debug("Added product {}.", addProduct);
        } catch (ProductException e) {
            log.error("Product issue ", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to add product {}.", addProduct, e);
            throw new ProductException("Failed to add product.", "addProduct", INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Set<Product> getProducts() throws ProductException {
        try {
            return PRODUCTS;
        } catch (Exception e) {
            log.error("Failed to get products.", e);
            throw new ProductException("Failed to get products", "getProducts", e);
        }
    }

    @Override
    public Set<String> validateSkus(@NonNull Set<String> skus) {
        return skus.stream().filter(s -> PRODUCTS.stream().noneMatch(p -> p.getSku().equalsIgnoreCase(s))).collect(toSet());
    }

    @Override
    public Double getPrice(@NonNull String sku) {
        final Optional<Product> first = PRODUCTS.stream().filter(p -> p.getSku().equalsIgnoreCase(sku)).findFirst();
        return first.map(Product::getPrice).orElse(null);
    }
}
