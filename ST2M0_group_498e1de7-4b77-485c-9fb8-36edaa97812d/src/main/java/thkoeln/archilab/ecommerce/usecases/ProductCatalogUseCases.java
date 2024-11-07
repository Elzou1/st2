package thkoeln.archilab.ecommerce.usecases;

import java.util.UUID;


/**
 * This interface contains methods needed in the context of use cases concerning the product catalog.
 * The interface is probably incomplete, and will grow over time ...
 */

public interface ProductCatalogUseCases {
    /**
     * Adds a new product to the shop catalog
     * @param productId
     * @param name
     * @param description
     * @param size
     * @param purchasePrice
     * @param salesPrice
     * @throws ShopException if ...
     *         - the product id already exists,
     *         - name or description are null or empty,
     *         - the size is <= 0 (but can be null!),
     *         - the purchase price is null or <= 0,
     *         - the sales price is null or <= 0,
     *         - the sales price is lower than the purchase price
     */
    public void addProductToCatalog( UUID productId, String name, String description, Float size,
                                     Float purchasePrice, Float salesPrice );


    /**
     * Removes a product from the shop catalog
     * @param productId
     * @throws ShopException if
     *      - the product id does not exist
     *      - the product is still in inventory
     *      - the product is still reserved in a shopping basket, or part of a completed order
     */
    public void removeProductFromCatalog( UUID productId );


    /**
     * Get the sales price of a given product
     * @param productId
     * @return the sales price
     * @throws ShopException if the product id does not exist
     */
    public Float getSalesPrice( UUID productId );


    /**
     * Clears the product catalog, i.e. removes all products from the catalog, including all the inventory,
     * all the reservations and all the orders.
     */
    public void deleteProductCatalog();

}
