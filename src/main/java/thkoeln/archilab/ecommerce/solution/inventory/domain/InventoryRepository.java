package thkoeln.archilab.ecommerce.solution.inventory.domain;

import org.springframework.data.repository.CrudRepository;
import thkoeln.archilab.ecommerce.solution.inventory.domain.Inventory;

import java.util.UUID;

public interface InventoryRepository extends CrudRepository<Inventory, UUID> {
}
