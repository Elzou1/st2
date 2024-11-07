package thkoeln.archilab.ecommerce.solution.customer.domain;

import lombok.Getter;
import javax.persistence.Embeddable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Embeddable
@Validated
public class Address {
    @Getter
    @NotEmpty(message = "")
    @NotNull(message = "")
    private String street;
    @NotEmpty(message = "")
    @NotNull(message = "")
    private String city;
    @NotEmpty(message = "")
    @NotNull(message = "")
    private String zipCode;

    public Address(String street, String city, String zipcode) {

        this.street = street;
        this.city = city;
        this.zipCode = zipcode;
    }

    public Address() {

    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
