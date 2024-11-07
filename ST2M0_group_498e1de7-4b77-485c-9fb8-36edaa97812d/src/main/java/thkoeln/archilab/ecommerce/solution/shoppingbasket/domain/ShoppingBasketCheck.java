package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

public class ShoppingBasketCheck {
    private ShoppingBasketCheck() {
        throw new RuntimeException("Cannot instantiate good validator");
    }

    public final static void VALIDATE_REQUESTED_QUANTITY_TO_BASKET(int requestedQuantity, int currentQuantity) {

        if (requestedQuantity > currentQuantity) {
            throw new IllegalArgumentException("Requested quantity are not available in the inventory");
        }
    }

    public final static void VALIDATE_REQUESTED_QUANTITY_TO_REMOVE(int requestedQuantity, int currentQuantity) {

        if (requestedQuantity > currentQuantity) {
            throw new IllegalArgumentException("Requested quantity to remove are more than already exist");
        }
    }
}
