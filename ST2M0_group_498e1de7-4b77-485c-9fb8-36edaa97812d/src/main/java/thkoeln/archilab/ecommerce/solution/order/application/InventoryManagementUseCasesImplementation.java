package thkoeln.archilab.ecommerce.solution.order.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.order.domain.InventoryRepository;
import thkoeln.archilab.ecommerce.solution.order.domain.Inventory;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductCheck;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasket;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketRepository;
import thkoeln.archilab.ecommerce.usecases.InventoryManagementUseCases;
import thkoeln.archilab.ecommerce.usecases.ShopException;
import thkoeln.archilab.ecommerce.usecases.ShoppingBasketUseCases;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryManagementUseCasesImplementation implements InventoryManagementUseCases {
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShoppingBasketRepository shoppingBasketRepository;
    @Autowired
    ShoppingBasketUseCases shoppingBasketUseCases;
    @Override
    public void addToInventory(UUID productId, int addedQuantity) {
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("Product does not exist");
        try {
            if(CheckIfProductExistsInInventory(productId)){
                ProductCheck.VALIDATE_ADDED_REMOVED_QUANTITY(addedQuantity);
                Inventory inventoryProduct = inventoryRepository.findInventoryProductByProductId(productId);
                inventoryProduct.setAddedQuantity(inventoryProduct.getAddedQuantity() + addedQuantity);
                inventoryRepository.save(inventoryProduct);
            }else {
                ProductCheck.VALIDATE_ADDED_REMOVED_QUANTITY(addedQuantity);
                Inventory inventoryProduct = new Inventory(productId, addedQuantity);
                inventoryRepository.save(inventoryProduct);
            }
        }catch (Exception e){
            throw new ShopException("Cannot add Product to inventory: " + e.getMessage());
        }
    }

    @Override
    public void removeFromInventory(UUID productId, int removedQuantity) {
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("Product does not exist");

        try{
            ProductCheck.VALIDATE_ADDED_REMOVED_QUANTITY(removedQuantity);
            int reservedQuantity = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets(productId);
            int availableQuantity = this.getAvailableInventory(productId);

            if(removedQuantity > reservedQuantity + availableQuantity)
                throw new ShopException("Cannot remove Product quantity more than exists");

            if(CheckIfProductExistsInInventory(productId)){
                int newTotalQuantity = reservedQuantity + availableQuantity - removedQuantity;
                if (newTotalQuantity < reservedQuantity){
                    var basketsWithReservedProdukt = shoppingBasketRepository.findAllByProductId(productId);
                    this.adjustQuantity(basketsWithReservedProdukt, newTotalQuantity);
                }
                Inventory inventoryProdukt = inventoryRepository.findInventoryProductByProductId(productId);
                inventoryProdukt.setAddedQuantity(inventoryProdukt.getAddedQuantity() - removedQuantity);
                inventoryRepository.save(inventoryProdukt);
            }else {
                throw new ShopException("Product does not exist in inventory");
            }
        }catch (Exception e){
            throw new ShopException("Cannot remove Product from inventory: " + e.getMessage());
        }
    }

    @Override
    public void changeInventoryTo(UUID productId, int newTotalQuantity) {
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("Product does not exist");

        try{
            ProductCheck.VALIDATE_ADDED_REMOVED_QUANTITY(newTotalQuantity);
            int reservedQuantity = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets(productId);


            if(CheckIfProductExistsInInventory(productId)){
                if (newTotalQuantity < reservedQuantity){
                    var basketsWithReservedProdukt = shoppingBasketRepository.findAllByProductId(productId);
                    this.adjustQuantity(basketsWithReservedProdukt, newTotalQuantity);
                }
                Inventory inventoryProdukt = inventoryRepository.findInventoryProductByProductId(productId);
                inventoryProdukt.setAddedQuantity(newTotalQuantity);
                inventoryRepository.save(inventoryProdukt);
            }else {
                throw new ShopException("Product does not exist in inventory");
            }
        }catch (Exception e){
            throw new ShopException("Cannot remove Product from inventory: " + e.getMessage());
        }
    }

    @Override
    public int getAvailableInventory(UUID productId) {
        if(!CheckIfProductExistsInCatalog(productId))
            throw new ShopException("Product does not exist");
        Inventory inventoryProduct = inventoryRepository.findInventoryProductByProductId(productId);
        return inventoryProduct.getAddedQuantity();
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
    private void adjustQuantity(List<ShoppingBasket> shoppingBasketList, int newTotalQuantity) {
        int currentTotalQuantity = shoppingBasketList.stream()
                .mapToInt(ShoppingBasket::getQuantity)
                .sum();

        if (newTotalQuantity >= currentTotalQuantity) {
            return;
        }

        int remainingQuantity = currentTotalQuantity - newTotalQuantity;
        int currentIndex = 0;

        for (ShoppingBasket basket : shoppingBasketList) {
            if (currentIndex >= remainingQuantity) {
                break;
            }

            int quantityAdjustment = Math.min(basket.getQuantity(), remainingQuantity - currentIndex);
            basket.setQuantity(basket.getQuantity() - quantityAdjustment);
            shoppingBasketRepository.save(basket);
            currentIndex += quantityAdjustment;
        }
    }

}

