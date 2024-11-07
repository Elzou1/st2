package thkoeln.archilab.ecommerce.solution.order.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderPartRepository extends CrudRepository<OrderPart, UUID> {
    List<OrderPart> findAllByOrderId(UUID orderId);
    boolean existsByProductId(UUID productId);


}
