package thkoeln.archilab.ecommerce.solution.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import thkoeln.archilab.ecommerce.solution.order.domain.Inventory;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class Product {
    @Id
    @Column(unique=true)
    private UUID productId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Float size;
    @Column
    private Float purchasePrice;
    @Column
    private Float salesPrice;

    public Product(UUID productId, String name, String description, Float size, Float purchasePrice, Float salesPrice) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.size = size;
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
    }

    public Product() {

    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Float getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Float salesPrice) {
        this.salesPrice = salesPrice;
    }
}


