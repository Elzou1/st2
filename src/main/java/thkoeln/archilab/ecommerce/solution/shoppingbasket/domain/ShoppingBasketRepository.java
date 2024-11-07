package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import org.springframework.data.repository.CrudRepository;
import thkoeln.archilab.ecommerce.solution.customer.domain.Customer;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingBasketRepository extends CrudRepository<ShoppingBasket,UUID> {

    Optional<ShoppingBasket> findByCustomer(Customer customer);
}
