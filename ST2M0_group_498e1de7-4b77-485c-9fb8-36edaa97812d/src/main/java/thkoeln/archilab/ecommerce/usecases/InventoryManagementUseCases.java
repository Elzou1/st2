package thkoeln.archilab.ecommerce.usecases;

import java.util.UUID;


/**
 * This interface contains methods needed in the context of use cases handling the shop inventory,
 * i.e. adding and removing products in the fulfillment center. The interface is probably incomplete, and
 * will grow over time.
 */
public interface InventoryManagementUseCases {
    /**
     * Adds a certain quantity of a given product to the inventory
     * @param productId
     * @param addedQuantity
     * @throws ShopException if the product id does not exist, or if the added quantity is negative
     */
    public void addToInventory( UUID productId, int addedQuantity );


    /**
     * Removes a certain quantity of a given product from the inventory.
     * If the new total quantity is lower than the currently reserved products, some of currently reserved products
     * (in the customers' shopping baskets) are removed. This means that some of the reserved products are lost for
     * the customer. (This is necessary because there probably was a mistake in the inventory management, a mis-counting,
     * or some of the products were stolen from the fulfillment center, are broken, etc.)
     * @param productId
     * @param removedQuantity
     * @throws ShopException if ...
     *      - the product id does not exist
     *      - if the removed quantity is negative
     *      - if the removed quantity is greater than the current inventory and the currently reserved products together
     */
    public void removeFromInventory( UUID productId, int removedQuantity );


    /**
     * Changes the total quantity of a given product in the inventory.
     * If the new total quantity is lower than the currently reserved products, some of currently reserved products
     * (in the customers' shopping baskets) are removed. This means that some of the reserved products are lost for
     * the customer. (This is necessary because there probably was a mistake in the inventory management, a mis-counting,
     * or some of the products were stolen from the fulfillment center, are broken, etc.)
     * @param productId
     * @param newTotalQuantity
     * @throws ShopException if ...
     *      - the product id does not exist
     *      - if the new total quantity is negative
     */
    public void changeInventoryTo( UUID productId, int newTotalQuantity );


    /**
     * Get the current total inventory of a given product, including the currently reserved products
     * @param productId
     * @throws ShopException if the product id does not exist
     */
    public int getAvailableInventory( UUID productId );


}
