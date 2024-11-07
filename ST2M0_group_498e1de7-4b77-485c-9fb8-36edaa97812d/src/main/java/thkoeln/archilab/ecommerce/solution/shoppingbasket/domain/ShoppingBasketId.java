package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class ShoppingBasketId implements Serializable {
    private String customerEmail;
    private UUID productId;

    public ShoppingBasketId(String customerEmail, UUID productId) {
        this.customerEmail = customerEmail;
        this.productId = productId;
    }

    public ShoppingBasketId(){}

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
