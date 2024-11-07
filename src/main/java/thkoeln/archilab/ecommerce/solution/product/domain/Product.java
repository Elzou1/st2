package thkoeln.archilab.ecommerce.solution.product.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@Getter
@Entity
@Table
public class Product {
    @Id
    @Setter
    @Getter
    private UUID productId;

    @Column
    @Setter
    @Getter
    @NotNull
    @NotEmpty
    private String name;

    @Column
    @Setter
    @Getter
    private String description;

    @Positive
    @Column
    @Setter
    @Getter
    private Float size;

    @Getter
    @NotNull
    @Positive
    @Column
    @Setter
    private Float purchasePrice;

    @Getter
    @NotNull
    @Positive
    @Column
    @Setter
    private Float salesPrice;

    @Column
    @Getter
    @Setter
    @PositiveOrZero
    private int quantity ;

    public Product() {
    }

}


