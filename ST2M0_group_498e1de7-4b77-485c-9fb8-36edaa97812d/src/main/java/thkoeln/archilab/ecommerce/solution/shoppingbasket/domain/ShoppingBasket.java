package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@IdClass(ShoppingBasketId.class)
@Table
public class ShoppingBasket {
    @Id
    @Column()
    private String customerEmail;
    @Id
    @Column()
    private UUID productId;
    @Column()
    private int quantity;

    public ShoppingBasket(String customerEmail, UUID productId, int quantity) {
        this.customerEmail= customerEmail;
        this.productId = productId;
        this.quantity = quantity;
    }

    public ShoppingBasket() {

    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}