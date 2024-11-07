package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import org.springframework.data.repository.CrudRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasket;

import java.util.List;
import java.util.UUID;

public interface ShoppingBasketRepository extends CrudRepository<ShoppingBasket,UUID> {
    ShoppingBasket findShoppingBasketByCustomerEmailAndProductId(String customerEmail, UUID productId);
    boolean existsShoppingBasketByCustomerEmailAndProductId(String customerEmail, UUID productId);

    List<ShoppingBasket> findAllByCustomerEmail(String customerEmail);

    List<ShoppingBasket> findAllByProductId(UUID productId);

    boolean existsByProductId(UUID productId);
}
