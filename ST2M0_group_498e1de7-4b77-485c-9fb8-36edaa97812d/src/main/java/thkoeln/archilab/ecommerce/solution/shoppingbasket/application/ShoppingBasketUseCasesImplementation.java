package thkoeln.archilab.ecommerce.solution.shoppingbasket.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.customer.domain.CustomerRepository;
import thkoeln.archilab.ecommerce.solution.order.domain.*;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductCheck;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasket;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketCheck;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketRepository;
import thkoeln.archilab.ecommerce.usecases.ShopException;
import thkoeln.archilab.ecommerce.usecases.ShoppingBasketUseCases;

import java.util.*;

@Service
public class ShoppingBasketUseCasesImplementation implements ShoppingBasketUseCases {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    OrderRepository ordersRepository;

    @Autowired
    OrderPartRepository orderPositionRepository;

    @Override
    public void addProductToShoppingBasket(String customerEmail, UUID productId, int quantity) {
        if(!CheckIfCustomerExists(customerEmail))
            throw new ShopException("Customer does not exist");
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("product does not exist");
        if(!CheckIfProductExistsInInventory(productId))
            throw new ShopException("product is out of stock");
        try{
            ProductCheck.VALIDATE_ADDED_REMOVED_QUANTITY(quantity);
            Inventory inventoryProduct = inventoryRepository.findInventoryProductByProductId(productId);
            int currentQuantity = inventoryProduct.getAddedQuantity();
            ShoppingBasketCheck.VALIDATE_REQUESTED_QUANTITY_TO_BASKET(quantity, currentQuantity);

            if(CheckIfBasketExists(customerEmail, productId)){
                ShoppingBasket basket = shoppingBasketRepository.findShoppingBasketByCustomerEmailAndProductId(customerEmail, productId);
                int totalRequestedQuantity = quantity + basket.getQuantity();
                basket.setQuantity(totalRequestedQuantity);
                shoppingBasketRepository.save(basket);
                inventoryProduct.setAddedQuantity(inventoryProduct.getAddedQuantity() - quantity);
                inventoryRepository.save(inventoryProduct);

            }else {
                ShoppingBasket basket = new ShoppingBasket(customerEmail, productId, quantity);
                inventoryProduct.setAddedQuantity(inventoryProduct.getAddedQuantity() - quantity);
                inventoryRepository.save(inventoryProduct);
                shoppingBasketRepository.save(basket);
            }
        }catch (Exception e){
            throw new ShopException("Cannot add Product to shopping basket: " + e.getMessage());
        }

    }

    @Override
    public void removeProductFromShoppingBasket(String customerEmail, UUID productId, int quantity) {
        if(!CheckIfCustomerExists(customerEmail))
            throw new ShopException("Customer does not exist");
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("Product does not exist");
        try{
            if(CheckIfBasketExists(customerEmail, productId)){
                ShoppingBasket basket = shoppingBasketRepository.findShoppingBasketByCustomerEmailAndProductId(customerEmail, productId);
                ShoppingBasketCheck.VALIDATE_REQUESTED_QUANTITY_TO_REMOVE(quantity, basket.getQuantity());
                int updatedQuantity = basket.getQuantity() - quantity;
                basket.setQuantity(updatedQuantity);
                System.out.println(updatedQuantity);
                shoppingBasketRepository.save(basket);

            }else {
                throw new ShopException("Cannot remove Product from non existent shopping basket");
            }
            Inventory inventoryProduct;
            if(CheckIfProductExistsInInventory(productId)) {
                inventoryProduct = inventoryRepository.findInventoryProductByProductId(productId);
                inventoryProduct.setAddedQuantity(inventoryProduct.getAddedQuantity() + quantity);
            }
            else {
                inventoryProduct = new Inventory(productId, quantity);
            }
            inventoryRepository.save(inventoryProduct);
        }catch (Exception e){
            throw new ShopException("Cannot add Product to shopping basket: " + e.getMessage());
        }
    }

    @Override
    public Map<UUID, Integer> getShoppingBasketAsMap(String customerEmail) {
        if(!CheckIfCustomerExists(customerEmail))
            throw new ShopException("Customer does not exist");

        var basket = shoppingBasketRepository.findAllByCustomerEmail(customerEmail);
        if(basket.isEmpty())
            return new HashMap<UUID, Integer>();

        var cartMapping = new HashMap<UUID, Integer>();
        for (var item: basket) {
            cartMapping.put(item.getProductId(), item.getQuantity());
        }
        return cartMapping;
    }

    @Override
    public float getShoppingBasketAsMoneyValue(String customerEmail) {
        if(!CheckIfCustomerExists(customerEmail))
            throw new ShopException("customer does not exist");

        var basket = shoppingBasketRepository.findAllByCustomerEmail(customerEmail);
        if(basket.isEmpty())
            return 0;

        float totalPrice = 0;
        for (var item: basket) {
            var product = productRepository.findProductCatalogByProductId(item.getProductId());
            totalPrice += (product.getSalesPrice() * item.getQuantity());
        }

        return totalPrice;
    }

    @Override
    public int getReservedInventoryInShoppingBaskets(UUID productId) {
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("Product does not exist");

        var reservedInventory = shoppingBasketRepository.findAllByProductId(productId);
        if(reservedInventory.isEmpty())
            return 0;

        int totalNumberOfReservations = 0;
        for (var item: reservedInventory) {
            totalNumberOfReservations += item.getQuantity();
        }
        return totalNumberOfReservations;
    }

    @Override
    public void checkout(String customerEmail) {
        if(!CheckIfCustomerExists(customerEmail))
            throw new ShopException("Customer does not exist");

        var basket = shoppingBasketRepository.findAllByCustomerEmail(customerEmail);
        if(basket.isEmpty())
            throw new ShopException("Cannot checkout empty basket");

        Order newOrder = new Order(customerEmail);
        ordersRepository.save(newOrder);

        for (var produkt: basket) {
            if(produkt.getQuantity() == 0)
                throw new ShopException("Cannot checkout 0 item");
            var orderPart= new OrderPart(newOrder.getOrderId(), produkt.getProductId(), produkt.getQuantity());
            orderPositionRepository.save(orderPart);
            shoppingBasketRepository.delete(produkt);
        }

    }

    @Override
    public Map<UUID, Integer> getOrderHistory(String customerEmail) {
        if(!CheckIfCustomerExists(customerEmail))
            throw new ShopException("Customer does not exist");

        var allOrders = ordersRepository.findAllByCustomerEmail(customerEmail);
        if(allOrders.isEmpty() || allOrders.equals(null))
            return  new HashMap<UUID, Integer>();

        var orderHistory = new HashMap<UUID, Integer>();
        for(var order: allOrders){
            var orderParts = orderPositionRepository.findAllByOrderId(order.getOrderId());
            orderParts.forEach((part) -> {
                UUID productId = part.getProductId();
                int quantity = part.getQuantity();
                orderHistory.compute(productId, (key, value) -> (value == null) ? quantity : value + quantity);
            });
        }
        return orderHistory;
    }

    @Override
    public void deleteAllOrders() {
        orderPositionRepository.deleteAll();
        ordersRepository.deleteAll();
        shoppingBasketRepository.deleteAll();
    }

    private boolean CheckIfProductExistsInCatalog(UUID productId){
        if (productRepository.existsById(productId))
            return true;
        return false;
    }

    private boolean CheckIfProductExistsInInventory(UUID productId){
        if (inventoryRepository.existsById(productId))
            return true;
        return false;
    }

    private boolean CheckIfCustomerExists(String email){
        if (customerRepository.existsByEmail(email))
            return true;
        return false;
    }

    private boolean CheckIfBasketExists(String email, UUID productId){
        if (shoppingBasketRepository.existsShoppingBasketByCustomerEmailAndProductId(email, productId))
            return true;
        return false;
    }
}
