package thkoeln.archilab.ecommerce.solution.order.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class OrderPart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderPartId;

    @Column
    private UUID orderId;

    @Column()
    private UUID productId;

    @Column()
    private int quantity;

    public OrderPart(UUID orderId, UUID productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderPart() {

    }

    public UUID getOrderPartId() {
        return orderPartId;
    }

    public void setOrderPartId(UUID orderPartId) {
        this.orderPartId = orderPartId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
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
