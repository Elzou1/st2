package thkoeln.archilab.ecommerce.solution.shoppingbasket.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingBasketPartRepository extends CrudRepository<ShoppingBasketPart, UUID> {

    Optional<ShoppingBasketPart> findByBasketPartId(ShoppingBasketPart shoppingBasketPart);

}
