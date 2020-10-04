package org.saurav.mapper.impl;

import lombok.NonNull;
import org.saurav.dto.Product;
import org.saurav.mapper.Mapper;
import org.saurav.request.AddProduct;

/**
 * @author Saurav Singh
 **/
public class AddProductMapper implements Mapper<AddProduct, Product> {

    @Override
    public @NonNull Product map(@NonNull AddProduct addProduct) {
        final Product product = new Product();
        product.setSku(addProduct.getSku());
        product.setPrice(addProduct.getPrice());
        return product;
    }
}
