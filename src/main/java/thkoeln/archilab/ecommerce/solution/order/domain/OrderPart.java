package thkoeln.archilab.ecommerce.solution.order.domain;

import lombok.Getter;
import lombok.Setter;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@Entity
@Table(name ="order_part")
public class OrderPart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderPartId;

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

    public OrderPart() {
    }




}
