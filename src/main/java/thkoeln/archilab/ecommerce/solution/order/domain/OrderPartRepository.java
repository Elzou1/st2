package thkoeln.archilab.ecommerce.solution.order.domain;

import org.springframework.data.repository.CrudRepository;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;

import java.util.List;
import java.util.UUID;

public interface OrderPartRepository extends CrudRepository<OrderPart, UUID> {


    boolean existsByProduct(Product product);
}
