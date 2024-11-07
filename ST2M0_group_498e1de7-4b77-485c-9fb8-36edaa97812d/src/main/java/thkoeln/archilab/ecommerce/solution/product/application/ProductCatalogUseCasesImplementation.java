package thkoeln.archilab.ecommerce.solution.product.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.order.domain.InventoryRepository;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderPartRepository;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductCheck;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductRepository;
import thkoeln.archilab.ecommerce.solution.shoppingbasket.domain.ShoppingBasketRepository;
import thkoeln.archilab.ecommerce.usecases.ShopException;

import java.util.*;

@Service
public class ProductCatalogUseCasesImplementation implements thkoeln.archilab.ecommerce.usecases.ProductCatalogUseCases {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    OrderPartRepository orderPositionRepository;
    @Override
    public void addProductToCatalog(UUID productId, String name, String description, Float size, Float purchasePrice, Float salesPrice) {
        Product product = new Product(productId, name, description, size,purchasePrice, salesPrice);
        try {
            ProductCheck.CheckProduct(product);

            if (CheckIfProductExists(productId))
                throw new ShopException("Failed to add already existing Product");

            productRepository.save(product);
        } catch (Exception e) {
            throw new ShopException("Failed to add Product to catalog: " + e.getMessage());
        }
    }

    @Override
    public void removeProductFromCatalog(UUID productId) {
        if(!CheckIfProductExists(productId))
            throw new ShopException("product already does not exist");

        if(CheckIfProductExistsInInventory(productId))
            throw new ShopException("Cannot remove product exists in inventory");

        if(CheckIfProductExistsInShoppingBasket(productId))
            throw new ShopException("Cannot remove product reserved in shopping cart");

        if(CheckIfProductExistsInOrderPart(productId))
            throw new ShopException("Cannot remove product which is position of an order");

        productRepository.deleteById(productId);
    }

    @Override
    public Float getSalesPrice(UUID productId) {
        if(CheckIfProductExists(productId)){
            Product product = productRepository.findProductCatalogByProductId(productId);
            return product.getSalesPrice();
        }
        throw new ShopException("product does not exist");
    }

    @Override
    public void deleteProductCatalog() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    private boolean CheckIfProductExists(UUID product){
        if (productRepository.existsById(product))
            return true;
        return false;
    }

    private boolean CheckIfProductExistsInInventory(UUID goodId){
        if (inventoryRepository.existsById(goodId))
            return true;
        return false;
    }

    private boolean CheckIfProductExistsInShoppingBasket(UUID productId){
        if (shoppingBasketRepository.existsByProductId(productId))
            return true;
        return false;
    }

    private boolean CheckIfProductExistsInOrderPart(UUID productId){
        if (orderPositionRepository.existsByProductId(productId))
            return true;
        return false;
    }


}
