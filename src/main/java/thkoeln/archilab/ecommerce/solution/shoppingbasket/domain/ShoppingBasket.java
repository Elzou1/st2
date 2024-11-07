package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import lombok.Getter;
import lombok.Setter;
import thkoeln.archilab.ecommerce.solution.customer.domain.Customer;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "shopping_basket")
public class ShoppingBasket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private UUID shoppingBasketId;

    @OneToOne
    @Getter
    @Setter
    private Customer customer;

    @OneToMany
    @Getter
    @Setter
    private List<ShoppingBasketPart> shoppingBasketParts =new ArrayList<>();


    public ShoppingBasket() {

    }

    public void empty() {
        shoppingBasketParts.clear();
    }
}




