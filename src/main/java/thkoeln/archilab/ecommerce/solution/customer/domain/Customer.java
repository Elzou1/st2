package thkoeln.archilab.ecommerce.solution.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dom4j.tree.AbstractEntity;
import org.springframework.validation.annotation.Validated;
import thkoeln.archilab.ecommerce.solution.customer.domain.Address;

import javax.persistence.*;

@Entity
@Table
@Validated
public class Customer {
    @Id
    @Column(unique=true)
    private String email;

    @Column
    private String name;
    @Embedded
    private Address address;

    public Customer(String name, String email, String street, String city, String zipcode) {
        this.email = email;
        this.name = name;
        this.address = new Address(street, city, zipcode);
    }

    public Customer() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
