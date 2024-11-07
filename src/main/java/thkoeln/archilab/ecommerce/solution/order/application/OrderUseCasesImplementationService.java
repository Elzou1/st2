package thkoeln.archilab.ecommerce.solution.order.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.customer.domain.Customer;
import thkoeln.archilab.ecommerce.solution.customer.domain.CustomerRepository;
import thkoeln.archilab.ecommerce.solution.order.domain.Order;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderPart;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderRepository;
import thkoeln.archilab.ecommerce.solution.product.application.ProductInOrder;
import thkoeln.archilab.ecommerce.usecases.OrderUseCases;
import thkoeln.archilab.ecommerce.usecases.ShopException;

import java.util.*;


@Service
public class OrderUseCasesImplementationService implements OrderUseCases {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Map<UUID, Integer> getOrderHistory(String customerEmail) {
        if (!customerRepository.existsByEmail(customerEmail))
            throw new ShopException("  customer does not exist");

        Optional<Customer> customerrOptional = customerRepository.findById(customerEmail);
        if (customerrOptional.isEmpty()) {
            throw new ShopException("Customer not found");
        }
        List<Order> customerOrders = orderRepository.findAllByCustomerEmail(customerEmail);
        Map<UUID, Integer> orderHistory = new HashMap<>();
        for (Order order : customerOrders) {
            for (OrderPart part : order.getOrderParts()) {
                UUID productId = part.getProduct().getProductId();
                int quantity = part.getQuantity();
                orderHistory.merge(productId,quantity,Integer::sum);
            }
        }

        return orderHistory;
    }

    @Override
    public void deleteAllOrders() {
        orderRepository.deleteAll();

    }


}





