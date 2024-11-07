package thkoeln.archilab.ecommerce.solution.product.domain;

import java.util.Objects;

public class ProductCheck {
    private ProductCheck() {
        throw new RuntimeException("Cannot instantiate good validator");
    }

    public final static void CheckProduct(Product product) {
        Objects.requireNonNull(product);

        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }

        if (product.getSize() != null && product.getSize() <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0.");
        }

        if (product.getPurchasePrice() == null || product.getPurchasePrice() <= 0) {
            throw new IllegalArgumentException("Purchase price cannot be negative or null.");
        }

        if (product.getSalesPrice() == null || product.getSalesPrice() <= 0) {
            throw new IllegalArgumentException("Sell price cannot be negative or null.");
        }

        if (product.getSalesPrice() != null && product.getPurchasePrice() != null && product.getSalesPrice() < product.getPurchasePrice()) {
            throw new IllegalArgumentException("Sell price cannot be lower than purchase price.");
        }
    }

    public final static void VALIDATE_ADDED_REMOVED_QUANTITY(int addedQuantity) {

        if (addedQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

}


