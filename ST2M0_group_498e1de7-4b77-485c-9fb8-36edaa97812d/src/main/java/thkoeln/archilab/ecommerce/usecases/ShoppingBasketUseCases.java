package thkoeln.archilab.ecommerce.usecases;

import java.util.Map;
import java.util.UUID;

/**
 * This interface contains methods needed in the context of use cases handling the shopping basket.
 * The interface is probably incomplete, and will grow over time ...
 */
public interface ShoppingBasketUseCases {
    /**
     * Adds a product to the cart of a customer
     *
     * @param customerEmail
     * @param productId
     * @param quantity
     * @throws ShopException if ...
     *                       - the customer with the given email does not exist,
     *                       - the product does not exist,
     *                       - the quantity is negative,
     *                       - the product is not available in the requested quantity
     */
    public void addProductToShoppingBasket( String customerEmail, UUID productId, int quantity );


    /**
     * Removes a product from the cart of a customer
     *
     * @param customerEmail
     * @param productId
     * @param quantity
     * @throws ShopException if ...
     *                       - the customer with the given email does not exist,
     *                       - the product does not exist
     *                       - the quantity is negative
     *                       - the product is not in the cart in the requested quantity
     */
    public void removeProductFromShoppingBasket( String customerEmail, UUID productId, int quantity );


    /**
     * Returns a map showing which products are in the cart of a customer and how many of each product
     *
     * @param customerEmail
     * @return the cart of the customer (map is empty if the cart is empty)
     * @throws ShopException if the customer with the given email does not exist
     */
    public Map<UUID, Integer> getShoppingBasketAsMap( String customerEmail );


    /**
     * Returns the current value of all products in the cart of a customer
     *
     * @param customerEmail
     * @return the cart of the customer
     * @throws ShopException if the customer with the given email does not exist
     */
    public float getShoppingBasketAsMoneyValue( String customerEmail );



    /**
     * Get the number units of a specific product that are currently reserved in the shopping baskets of all customers
     * @param productId
     * @throws ShopException if the product id does not exist
     */
    public int getReservedInventoryInShoppingBaskets( UUID productId );



    /**
     * Checks out the cart of a customer
     *
     * @param customerEmail
     * @throws ShopException if the customer with the given email does not exist, or if the cart is empty
     */
    public void checkout( String customerEmail );


    /**
     * Returns a map showing which products have been ordered by a customer and how many of each product
     *
     * @param customerEmail
     * @return the order history of the customer (map is empty if the customer has not ordered anything yet)
     * @throws ShopException if the customer with the given email does not exist
     */
    public Map<UUID, Integer> getOrderHistory( String customerEmail );



    /**
     * Deletes all orders and shopping baskets in the system
     */
    public void deleteAllOrders();
}
