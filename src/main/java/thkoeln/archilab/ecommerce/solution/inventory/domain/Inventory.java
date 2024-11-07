package thkoeln.archilab.ecommerce.solution.inventory.domain;


import lombok.Getter;
import org.springframework.data.util.Pair;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID inventoryId;

    @OneToMany
    @Getter
    private List<Product> products =new ArrayList<Product>();

    private boolean isProduktInInventory(Product product) {
        if (products.contains(product))
            return products.get(products.indexOf(product)).getQuantity()>0 ;
        return false;
    }
    public Inventory() {

    }


}