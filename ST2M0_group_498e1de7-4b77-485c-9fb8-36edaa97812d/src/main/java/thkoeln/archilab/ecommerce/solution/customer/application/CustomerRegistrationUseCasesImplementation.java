package thkoeln.archilab.ecommerce.solution.customer.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.customer.domain.CustomerCheck;
import thkoeln.archilab.ecommerce.solution.customer.domain.CustomerRepository;
import thkoeln.archilab.ecommerce.solution.customer.domain.Address;
import thkoeln.archilab.ecommerce.solution.customer.domain.Customer;
import thkoeln.archilab.ecommerce.usecases.CustomerRegistrationUseCases;
import thkoeln.archilab.ecommerce.usecases.ShopException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class CustomerRegistrationUseCasesImplementation implements CustomerRegistrationUseCases {
    @Autowired
   CustomerRepository customerRepository;
    @Override
    public void register(@NotNull @NotEmpty String name, String email, String street, String city, String zipCode) {
        try {
            Customer newCustomer = new Customer(name, email, street, city, zipCode);
            CustomerCheck.validateCustomer(newCustomer);

            if (customerRepository.existsByEmail(email))
                throw new ShopException("Failed to register customer with already existing email");

            customerRepository.save(newCustomer);
        } catch (Exception e) {
            throw new ShopException("Failed to register customer: " + e.getMessage());
        }
    }

    @Override
    public void changeAddress( String customerEmail, String street, String city, String zipCode) {
        try {
            Customer newCustomer = customerRepository.findCustomerByEmail(customerEmail);
            Address newAddress = new Address(street, city, zipCode);
            newCustomer.setAddress(newAddress);
            CustomerCheck.validateCustomer(newCustomer);

            if (!customerRepository.existsByEmail(customerEmail))
                throw new ShopException("Failed to change address for non existent customer");

            customerRepository.save(newCustomer);
        } catch (Exception e) {
            throw new ShopException("Failed to register customer: " + e.getMessage());
        }
    }
    @Override
    public String[] getCustomerData( String customerEmail) {
        if (!customerRepository.existsByEmail(customerEmail))
            throw new ShopException("Failed to get data for non existent customer");

        List<Customer> customerRepo = customerRepository.findAll();
        for (Customer customer : customerRepo) {
            if (customer.getEmail().equals(customerEmail)) {
                String[] customerData = new String[5];
                customerData[0] = customer.getName();
                customerData[1] = customer.getEmail();
                customerData[2] = customer.getAddress().getStreet();
                customerData[3] = customer.getAddress().getCity();
                customerData[4] = customer.getAddress().getZipCode();
                return customerData;
            }
        }
        return null;
    }

    @Override
    public void deleteAllCustomers() {
        customerRepository.deleteAll();

    }
}
