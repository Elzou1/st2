# Praktikum Softwaretechnik 2 (ST2) im SoSe 2024

Sofortiges Feedback zu Ihres Lösung finden Sie wie immer auf Ihrer 
[individuellen Testseite](http://students.pages.archi-lab.io/st2/ss24/m0/test/ST2M0_tests_group_498e1de7-4b77-485c-9fb8-36edaa97812d).


## Milestone 0 (M0) - A simple shop system

_Da wir aus der Aufgabenbeschreibung direkt Coding-Aufgaben ableiten, ist die komplette Beschreibung in Englisch
gehalten._

Your software development company has been contracted to delevop an eCommerce system for a small online shop. The shop sells
a number of products, which are not perishable and not fragile. A product has a name, description, and size. In addition, it
has a purchase price, and a sales price. 

The shop sells products to customers. A customer has a name, a email, and a address (with street, city, 
and zip code). There is no username, i.e. a customer can only be identified by his email. 
A customer can make a order for one or more products. Therefore, a order consists of one or several order parts. 
Order parts references a product, and contains the quantity of the product that the customer wants to buy. 

For the moment, the shop has only one fulfillment center, which stores all products. For each product, the inventory quantity is stored. For some
products, the inventory quantity is zero. In that case, the product is out of inventory, and not available for sale at moment.

Customers can buy only products that are in inventory, i.e. have inventory quantity > 0. (For simplicity reasons, customer returns
will not be handled by the system for now, but outside the system via email. So this is not your concern.) 
Shop admins can manually change the inventory quantity for a product, add new products to the catalog, or remove 
products from the catalog.

When a customer buys products, he/she puts them in the shopping basket first. By that action, the products are reserved for
the customer, and cannot be ordered by other customers. There is (as of now) no time limit for the reservation.
The customer can remove products from the shopping basket, or check out. 

_(This is what you need to model in this first milestone. Additions come later.)_

### Glossary

To make things easier, here is an English-German glossary of the terms used in the description above. Remember that
in your code, you need to use the **English** terms.

| English          | German                                          |
|------------------|-------------------------------------------------|
| product         | zu verkaufende Ware                                  |
| perishable       | verderblich                                     |
| fragile          | zerbrechlich                                    |
| customer         | Kunde                                  |
| order          | Bestellung                                   |
| order part      | Bestellposition                               |
| fulfillment center      | Auslieferungszentrum                               |
| inventory        | Warenbestand                                 |
| shopping basket         | Warenkorb                                  |
| purchase price   | Einkaufspreis                                   |
| sales price       | Verkaufspreis                                |
| email   | Email des Kunden                            |
| zip code            | Postleitzahl                                     |
| to check out     | zur Kasse gehen                                 |  
| delivery         | Auslieferung                                    |
| payment provider | Bezahl-Dienstleister (so etwas wie z.B. Paypal) |




## Tasks in M0

### E1: Specify a Logical Data Model (Logisches Datenmodell, LDM)

![Bloom-Level](./images/4-filled-32.png)
[Level 4 (Analyse) in Bloom's Taxonomy](https://www.archi-lab.io/infopages/material/blooms_taxonomy.html#level4)

Create a logical data model (LDM) for the store system. The LDM should map the entities, value objects and relationships described 
in the text above.

Store your solution in the [`exercises`](src/main/resources/exercises) directory.
There is already a template called [`E01solution.uxf`](src/main/resources/exercises/E02solution.uxf). 

**IMPORTANT**: You must adhere to a few rules, so that the test can check your solution:

* Keep the file name the same, otherwise the test will not find your diagram. Please also do not create
  variants, such as `E02_corrected.uxf` or similar.
* You do not have to upload a diagram this time - unless you have questions about your diagram.



### E2: Implement a minimal version (MVP) for the store system

![Bloom-Level](./images/4-filled-32.png) 
[Level 4 (Analyse) in Bloom's Taxonomy](https://www.archi-lab.io/infopages/material/blooms_taxonomy.html#level4)


Implement a prototype for the eCommerce system. The functionality of the prototype are defined by
**four interfaces**. You will find them under [`thkoeln.archilab.ecommerce.usecases`](src/main/java/thkoeln/archilab/ecommerce/usecases) 
You need to implement the methods of these interfaces.

The best way to do this is to implement one or more Spring Boot Services, e.g. like this:

```java
@Service
public class MyFantasticShopSystem implements CustomerRegistrationUseCases {
    //...
}
```

How you implement the use cases is up to you in M0. However, you should implement the domain entities and value objects
from the LDM. In addition, please use Spring JPA to persist the data. We are not testing this yet,
but will do so in the coming milestones.

**IMPORTANT**: Please follow these rules so that the test can check your implementation:

1. The implementation code must located within the package `thkoeln.archilab.ecommerce.solution`. You can create any number of
   sub-packages and classes as you like. 
   - In subsequent milestones, we will test for adherence to the DDD package structure guidelines that we use in ST2.
   - If you want to be prepared, follow the guidelines from the beginning.
   - In short: the package structure should reflect the domain model. One main package for each aggregate root (= "main business entity"), 
     and below that sub-packages `domain` and `application` for the respective layers. 
   - In the `domain` package, you have entities, value objects, and repositories. 
   - In the `application` package, you have application services, DTOs, and controllers.
2. The implementation must implement **all** interfaces from `thkoeln.archilab.ecommerce.usecases`.
3. You all the tests in this milestone visible to you. You can therefore test locally.
4. The best starting point for your implementation is to follow the interfaces methods, and not necessarily look at the tests
   (they will be confusing and hard to understand at this point). A suitable start point is e.g. `CustomerRegistrationUseCases`.
   Pick the easiest interface (with least dependencies) first, and then go on from there.
5. Write your own tests, in addition to the ones provided. This will actually help you getting your tasks done quicker. 
   And we will check on your own tests in subsequent milestones.

