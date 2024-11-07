package thkoeln.archilab.ecommerce.solution.product.domain;

import java.util.Objects;

public class ProductCheck {
    public final static void CheckProduct(Product product) {
        Objects.requireNonNull(product);


        if (product.getSalesPrice() != null && product.getPurchasePrice() != null && product.getSalesPrice() < product.getPurchasePrice()) {
            throw new IllegalArgumentException("Sell price cannot be lower than purchase price.");
        }
    }



}


