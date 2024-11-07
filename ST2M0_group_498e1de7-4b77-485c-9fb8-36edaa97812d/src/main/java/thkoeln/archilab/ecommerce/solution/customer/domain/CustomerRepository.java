package thkoeln.archilab.ecommerce.solution.customer.domain;

import org.springframework.data.repository.CrudRepository;
import thkoeln.archilab.ecommerce.solution.customer.domain.Customer;
import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    List<Customer> findAll();
    boolean existsByEmail(String email);

    Customer findCustomerByEmail(String email);

}
