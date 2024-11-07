package thkoeln.archilab.ecommerce.solution.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource( exported = false)
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByCustomerEmail(String customerEmail);
}

