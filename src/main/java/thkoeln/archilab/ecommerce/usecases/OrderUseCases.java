package thkoeln.archilab.ecommerce.usecases;

import java.util.Map;
import java.util.UUID;

/**
 * This interface contains methods needed in the context of the order history of a customer.
 */
public interface OrderUseCases {
    /**
     * Returns a map showing which products have been ordered by a customer and how many of each product
     *
     * @param customerEmail
     * @return the order history of the customer (map is empty if the customer has not ordered anything yet)
     * @throws ShopException if the customer with the given email does not exist
     */
    public Map<UUID, Integer> getOrderHistory( String customerEmail );



    /**
     * Deletes all orders in the system
     */
    public void deleteAllOrders();
}
