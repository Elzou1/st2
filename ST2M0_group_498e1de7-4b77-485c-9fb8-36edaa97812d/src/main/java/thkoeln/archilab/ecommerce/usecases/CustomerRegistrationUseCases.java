package thkoeln.archilab.ecommerce.usecases;

/**
 * This interface contains methods needed in the context of use cases concerning the registration of a customer.
 * The interface is probably incomplete, and will grow over time ...
 */
public interface CustomerRegistrationUseCases {
    /**
     * Registers a new customer
     *
     * @param name
     * @param email
     * @param street
     * @param city
     * @param zipCode
     * @throws ShopException if ...
     *      - customer with the given email already exists
     *      - if the data are invalid (name, email, street, city, zipCode empty or null)
     */
    public void register( String name, String email, String street, String city, String zipCode );


    /**
     * Changes the address of a customer
     *
     * @param customerEmail
     * @param street
     * @param city
     * @param zipCode
     * @throws ShopException if ...
     *      - the customer with the given email does not exist,
     *      - the address data are invalid (street, city, zipCode empty or null)
     */
    public void changeAddress( String customerEmail, String street, String city, String zipCode );


    /**
     * Returns the data of a customer as an array of strings (name, email, street, city, zipCode)
     * @param customerEmail
     * @return the customer data
     * @throws ShopException if the customer with the given email does not exist
     */
    public String[] getCustomerData( String customerEmail );



    /**
     * Clears all customers, including all orders and shopping baskets
     */
    public void deleteAllCustomers();
}
