package thkoeln.archilab.ecommerce.solution.shoppingbasket.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.customer.domain.Customer;
import thkoeln.archilab.ecommerce.solution.customer.domain.CustomerRepository;
import thkoeln.archilab.ecommerce.solution.order.domain.*;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasket;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketPart;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketPartRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketRepository;
import thkoeln.archilab.ecommerce.usecases.ShopException;
import thkoeln.archilab.ecommerce.usecases.ShoppingBasketUseCases;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShoppingBasketUseCasesImplementationService implements ShoppingBasketUseCases {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShoppingBasketPartRepository shoppingBasketPartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ShoppingBasketRepository shoppingBasketRepository;
    @Autowired
    private OrderPartRepository orderPartRepository;

    @Override
    public void addProductToShoppingBasket(String customerEmail, UUID productId, int quantity) {
        if (!customerRepository.existsByEmail(customerEmail))
            throw new ShopException("  customer does not exist");
        if (quantity < 0) {
            throw new ShopException("Quantity cannot be negative");
        }
        if(productRepository.findById(productId).isEmpty()){
            throw new ShopException("product does not exist");
        };
        Product product= productRepository.findById(productId).get();
        if (product.getQuantity() < quantity) {
            throw new ShopException("Product is not available in the requested quantity");
        }
       if(product.getQuantity()-getReservedInventoryInShoppingBaskets(productId)<quantity){
           throw new ShopException("RESERVED QUANTITY NOT ENOUGH");
       }

        Optional<Customer> customer= customerRepository.findById(customerEmail);
          if (customer.isPresent()){
            Optional<ShoppingBasket> shoppingBasket=shoppingBasketRepository.findByCustomer(customer.get());
            ShoppingBasket foundBasket =new ShoppingBasket();
            if(shoppingBasket.isEmpty()){
              foundBasket.setCustomer(customer.get());
            }else {
                foundBasket=shoppingBasket.get();
            }
                Optional<ShoppingBasketPart> part =foundBasket.getShoppingBasketParts().stream().filter(p ->p.getProduct().getProductId()==productId).findFirst();
                ShoppingBasketPart tempPart=new ShoppingBasketPart();
                if(part.isPresent()){
                    tempPart=part.get();
                    tempPart.setQuantity(quantity+tempPart.getQuantity());
                }else {
                    tempPart.setProduct(product);
                    tempPart.setQuantity(quantity);
                    foundBasket.getShoppingBasketParts().add(tempPart);
                }
                shoppingBasketPartRepository.save(tempPart);
                shoppingBasketRepository.save(foundBasket);
                productRepository.save(product);
          };
    }
    @Override
    public void removeProductFromShoppingBasket(String customerEmail, UUID productId, int quantity) {
        if (quantity < 0) {
            throw new ShopException("Quantity cannot be negative");
        }
        Product product= productRepository.findById(productId).orElseThrow(new ShopException("Product does not exist"));

        Customer customer= customerRepository.findById(customerEmail).orElseThrow(new ShopException("Customer does not exist"));
        ShoppingBasket foundBasket =shoppingBasketRepository.findByCustomer(customer).orElseThrow(new ShopException("basket does not exist"));
        ShoppingBasketPart shoppingBasketPart= foundBasket.getShoppingBasketParts().stream().filter(part->
                    part.getProduct().getProductId()==productId).findFirst().orElseThrow(new ShopException("product is not in basket"));
        if (shoppingBasketPart.getQuantity() < quantity) {
            throw new ShopException("Product is not available in the requested quantity");
        }
        shoppingBasketPart.setQuantity(shoppingBasketPart.getQuantity()-quantity);
        if(shoppingBasketPart.getQuantity()==0){
            foundBasket.getShoppingBasketParts().remove(shoppingBasketPart);
            shoppingBasketPartRepository.delete(shoppingBasketPart);
        }
        shoppingBasketRepository.save(foundBasket);
        productRepository.save(product);
    }
    @Override
    public Map<UUID, Integer> getShoppingBasketAsMap(String customerEmail) {
       Customer customer= customerRepository.findById(customerEmail).orElseThrow(
           new ShopException("customer with email " + customerEmail + " not found"));
        Map<UUID, Integer> shoppingBasketMap;
        ShoppingBasket shoppingBasket=shoppingBasketRepository.findByCustomer(customer).orElse(new ShoppingBasket());
        shoppingBasketRepository.save(shoppingBasket);
        shoppingBasketMap = shoppingBasket.getShoppingBasketParts().stream().collect(Collectors.toMap(part ->
                part.getProduct().getProductId(), ShoppingBasketPart::getQuantity, (a, b) -> b));
        return shoppingBasketMap;
    }

    @Override
    public float getShoppingBasketAsMoneyValue(String customerEmail) {
        Customer customer=customerRepository.findById(customerEmail).orElseThrow(
             new ShopException("Customer with email " + customerEmail + " not found"));
        ShoppingBasket shoppingBasket=shoppingBasketRepository.findByCustomer(customer).orElse(new ShoppingBasket());
        return (float) shoppingBasket.getShoppingBasketParts().stream().mapToDouble(part ->part.getProduct().getSalesPrice()*part.getQuantity()).sum();

    }

    @Override
    public int getReservedInventoryInShoppingBaskets(UUID productId) {

       Optional<Product> product= productRepository.findById(productId);
       if(product.isEmpty()){
           throw new ShopException("product does not exist");
       }
       AtomicInteger reservedInventory= new AtomicInteger();
       shoppingBasketRepository.findAll().forEach(shoppingBasket -> {
            shoppingBasket.getShoppingBasketParts().forEach(part ->{
              if (part.getProduct().getProductId()==productId){
                  reservedInventory.addAndGet(part.getQuantity());
              }
          });

       });
       return reservedInventory.get();
    }


    @Override
    public boolean isEmpty(String customerEmail) {
        if (customerRepository.findById(customerEmail).isEmpty()) {
            throw new ShopException("Customer with email " + customerEmail + " not found");
        }
        Customer customer=customerRepository.findById(customerEmail).get();
        ShoppingBasket foundBasket=new ShoppingBasket();
        Optional<ShoppingBasket> shoppingBasket=shoppingBasketRepository.findByCustomer(customer);
        if(shoppingBasket.isPresent())
            foundBasket=shoppingBasket.get();
        return foundBasket.getShoppingBasketParts().isEmpty();
    }




    @Override
    public void checkout(String customerEmail) {
       Customer customer=customerRepository.findById(customerEmail).orElseThrow(new ShopException("customer does not exist"));
       ShoppingBasket shoppingBasket=shoppingBasketRepository.findByCustomer(customer).orElseThrow(new ShopException("basket is empty"));
       if(shoppingBasket.getShoppingBasketParts().isEmpty())
           throw new ShopException("basket is empty");
       Order order =new Order();
       order.setCustomerEmail(customerEmail);
       for(ShoppingBasketPart part :shoppingBasket.getShoppingBasketParts()){
            OrderPart orderPart = new OrderPart();
            Product product=part.getProduct();
            orderPart.setProduct(product);
            orderPart.setQuantity(part.getQuantity());
            product.setQuantity(product.getQuantity()-part.getQuantity());
            order.getOrderParts().add(orderPart);
            orderPartRepository.save(orderPart);
            productRepository.save(product);
       }
       orderRepository.save(order);
       shoppingBasket.getShoppingBasketParts().clear();
       shoppingBasketRepository.save(shoppingBasket);
    }
    @Override
    public void emptyAllShoppingBaskets() {
        shoppingBasketRepository.findAll().forEach(ShoppingBasket::empty);

    }










}
