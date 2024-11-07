package thkoeln.archilab.ecommerce.solution.inventory.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.inventory.domain.InventoryRepository;
import thkoeln.archilab.ecommerce.solution.inventory.domain.Inventory;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.application.ShoppingBasketUseCasesImplementationService;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasket;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketPart;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketPartRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketRepository;
import thkoeln.archilab.ecommerce.usecases.InventoryManagementUseCases;
import thkoeln.archilab.ecommerce.usecases.ShopException;
import thkoeln.archilab.ecommerce.usecases.ShoppingBasketUseCases;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryManagementUseCasesImplementationService implements InventoryManagementUseCases {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShoppingBasketRepository shoppingBasketRepository;
    @Autowired
    ShoppingBasketPartRepository shoppingBasketPartRepository;
    @Autowired
    ShoppingBasketUseCasesImplementationService shoppingBasketUseCasesImplementationService;

    Inventory inventory = new Inventory();

    @Override
    public void addToInventory(UUID productId, int addedQuantity) {
        if (!productRepository.existsById(productId))
            throw new ShopException("Product does not exist");
        if (addedQuantity < 0) {
            throw new ShopException("Quantity cannot be negative");
        }
        Product product = productRepository.findById(productId).get();
        if (!inventory.getProducts().contains(product)) {
            inventory.getProducts().add(product);
        }
        product.setQuantity(addedQuantity + product.getQuantity());
        productRepository.save(product);
    }

    @Override
    public void removeFromInventory(UUID productId, int removedQuantity) {
        if (!productRepository.existsById(productId))
            throw new ShopException("Product does not exist");
        Product product = productRepository.findById(productId).get();
        if (removedQuantity < 0 || removedQuantity > product.getQuantity()) {
            throw new ShopException("Quantity cannot be negative or greater than the available quantity");
        }
        product.setQuantity(product.getQuantity() - removedQuantity);
        int diff = shoppingBasketUseCasesImplementationService.getReservedInventoryInShoppingBaskets(productId)-product.getQuantity();
        if(diff>0){
            List<ShoppingBasket> basketList= (List<ShoppingBasket>) shoppingBasketRepository.findAll();
            int quantityToRemove=removedQuantity;
            boolean checkNextBasket=true;
            for (final ShoppingBasket shoppingBasket : basketList ){
                if(!checkNextBasket)break;
               List<ShoppingBasketPart>partList=shoppingBasket.getShoppingBasketParts();
                Iterator<ShoppingBasketPart> it =partList.iterator();
               while(it.hasNext()){
                   ShoppingBasketPart part=it.next();
                   if(part.getProduct().getProductId()==productId) {
                    if(quantityToRemove<part.getQuantity()){
                        part.setQuantity(part.getQuantity()-quantityToRemove);
                        checkNextBasket=false;
                        shoppingBasketPartRepository.save(part);
                        break;
                    }else{
                        quantityToRemove-=part.getQuantity();
                        it.remove();
                        shoppingBasketPartRepository.delete(part);
                    }
                   }
               }
               shoppingBasketRepository.save(shoppingBasket);
            }
        }
        productRepository.save(product);
    }

    @Override
    public void changeInventoryTo(UUID productId, int newTotalQuantity) {
        if (newTotalQuantity < 0) {
            throw new ShopException("Quantity cannot be negative");
        }
        Product product = productRepository.findById(productId).orElseThrow(new ShopException("product does not exist"));
        product.setQuantity(newTotalQuantity);
        shoppingBasketRepository.findAll().forEach(basket -> {
            basket.getShoppingBasketParts().forEach(part ->{
                if (part.getProduct().getProductId()==productId && product.getQuantity()< part.getQuantity()){
                    part.setQuantity(newTotalQuantity);
                    shoppingBasketPartRepository.save(part);
                }
            });
        });
    }

    @Override
    public int getAvailableInventory(UUID productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(Product::getQuantity).orElse(0);
    }
    }



