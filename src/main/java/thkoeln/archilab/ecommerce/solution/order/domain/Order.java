package thkoeln.archilab.ecommerce.solution.order.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="Orderr")
public class Order {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;

    @OneToMany
    @Getter
    @Setter
    private List<OrderPart> orderParts=new ArrayList<>();

    @Column()
    @Getter
    @Setter
    private String customerEmail;

    public Order() {
    }
}

