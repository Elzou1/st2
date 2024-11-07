package thkoeln.archilab.ecommerce.solution.product.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.order.application.ProductInOrderService;
import thkoeln.archilab.ecommerce.solution.product.domain.Product;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductCheck;
import thkoeln.archilab.ecommerce.solution.product.domain.ProductRepository;
import thkoeln.archilab.ecommerce.usecases.ShopException;
import java.util.*;

@Service
public class ProductCatalogUseCasesImplementationService implements thkoeln.archilab.ecommerce.usecases.ProductCatalogUseCases {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInOrder productInOrder;

    @Override
    public void addProductToCatalog(UUID productId, String name, String description, Float size, Float purchasePrice, Float salesPrice) {
        if (productId == null || productRepository.findById(productId).isPresent()) {
            throw new ShopException("The product ID already exists");
        }
        if (name == null || description == null || name.isEmpty() || description.isEmpty()) {
            throw new ShopException("Name or description cannot be null or empty");
        }
        if (purchasePrice == null || purchasePrice <= 0) {
            throw new ShopException("Purchase price must be positive");
        }
        if (salesPrice == null || salesPrice <= 0) {
            throw new ShopException("Sales price must be positive");
        }
        Product product = new Product();
        product.setName(name);
        product.setSize(size);
        product.setDescription(description);
        product.setPurchasePrice(purchasePrice);
        product.setSalesPrice(salesPrice);
        product.setProductId(productId);
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
        Product product=productRepository.findById(productId).orElseThrow(new ShopException("the product id does not exist")) ;
        if(product.getQuantity()>0)
            throw new ShopException("product exists in inventory");
        if(productInOrder.productExistsInOrder(productId))throw new ShopException("product exists in orders");
        productRepository.deleteById(productId);
    }


    @Override
    public Float getSalesPrice(UUID productId) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new ShopException("the item id does not exist");
        }
        return productRepository.findById(productId).get().getSalesPrice();
    }

    @Override
    public void deleteProductCatalog() {
        productRepository.deleteAll();
    }

    private boolean CheckIfProductExists(UUID product){
        return productRepository.existsById(product);
    }








}
