package thkoeln.archilab.ecommerce.solution.order.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "orderItem")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;
    @Column()
    private String customerEmail;

    public Order(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Order() {

    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}

