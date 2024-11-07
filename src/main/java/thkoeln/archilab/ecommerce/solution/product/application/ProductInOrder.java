package thkoeln.archilab.ecommerce.solution.product.application;

import java.util.UUID;

public interface ProductInOrder {
   public boolean productExistsInOrder(UUID productId);
}
