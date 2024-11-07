package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import lombok.Getter;
import lombok.Setter;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@Entity
public class ShoppingBasketPart {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID basketPartId;

    @OneToOne
    @NotNull
    @Getter
    @Setter
    private Product product;

    @Column()
    @PositiveOrZero
    @Getter
    @Setter
    private int quantity;

    public ShoppingBasketPart() {
    }
}
