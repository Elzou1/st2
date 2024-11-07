package thkoeln.archilab.ecommerce.solution.order.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InventoryRepository extends CrudRepository<Inventory, UUID> {
    Inventory findInventoryProductByProductId(UUID productId);
}
