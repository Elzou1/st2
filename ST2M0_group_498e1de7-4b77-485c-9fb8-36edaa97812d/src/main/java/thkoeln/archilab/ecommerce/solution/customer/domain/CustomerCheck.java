package thkoeln.archilab.ecommerce.solution.customer.domain;

import thkoeln.archilab.ecommerce.usecases.ShopException;

import java.util.Objects;
import java.util.stream.Stream;

public class CustomerCheck {
    private CustomerCheck() {
        throw new RuntimeException("Cannot instantiate customer  check");
    }


    public final static void validateCustomer(Customer customer) {
        Objects.requireNonNull(customer);
        Stream.of(customer.getName(), customer.getEmail(), customer.getAddress().getStreet(), customer.getAddress().getZipCode(), customer.getAddress().getCity())
                .map(s -> {
                    if (s.length() == 0)
                        throw new ShopException("the attributes of customer cannot be set to null");
                    return s;
                })
                .peek(s -> {
                    if (s.equals(customer.getEmail())) {
                        String emailRegex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
                        if (!s.matches(emailRegex)) {
                            throw new RuntimeException("Invalid email format");
                        }
                    }
                })
                .forEach(Objects::requireNonNull);
    }}
