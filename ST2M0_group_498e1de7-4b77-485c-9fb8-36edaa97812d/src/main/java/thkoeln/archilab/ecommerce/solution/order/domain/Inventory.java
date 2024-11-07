package thkoeln.archilab.ecommerce.solution.order.domain;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class Inventory {
    @Id
    @Column(unique=true)
    private UUID productId;
    @Column
    private int addedQuantity;

    public Inventory(UUID productId, int addedQuantity) {
        this.productId = productId;
        this.addedQuantity = addedQuantity;
    }

    public Inventory() {

    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID goodId) {
        this.productId = goodId;
    }

    public int getAddedQuantity() {
        return addedQuantity;
    }

    public void setAddedQuantity(int addedQuantity) {
        this.addedQuantity = addedQuantity;
    }
}