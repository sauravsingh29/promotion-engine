package org.saurav.api;

import org.saurav.dto.Product;
import org.saurav.exception.ProductException;
import org.saurav.request.AddProduct;
import org.saurav.service.impl.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author Saurav Singh
 **/
@RestController
@RequestMapping("/products")
public class ProductApi {

    private final ProductService productService;

    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Initial setup
     *
     * @throws ProductException can be thrown.
     */
    @PostConstruct
    public void init() throws ProductException {
        List<AddProduct> addProducts = Arrays.asList(new AddProduct("A", 50.0), new AddProduct("B", 30.0),
                new AddProduct("C", 20.0), new AddProduct("D", 15.0));
        for (AddProduct ap : addProducts) {
            productService.addProduct(ap);
        }
    }


    @PostMapping
    public ResponseEntity<HttpStatus> addProduct(final @RequestBody @Valid List<AddProduct> addProducts) throws ProductException {
        for (AddProduct ap : addProducts) {
            productService.addProduct(ap);
        }
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Set<Product>> getProducts() throws ProductException {
        final Set<Product> products = productService.getProducts();
        return ResponseEntity.ok().body(products);
    }
}
