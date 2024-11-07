package thkoeln.archilab.ecommerce.solution.order.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.product.application.ProductInOrder;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderPart;
import thkoeln.archilab.ecommerce.solution.order.domain.OrderPartRepository;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Service
public class ProductInOrderService implements ProductInOrder {
    @Autowired
    private OrderPartRepository orderPartRepository ;

    @Override
    public boolean productExistsInOrder(UUID productId) {
        List<OrderPart>parts = (List<OrderPart>) orderPartRepository.findAll();
        for(OrderPart part:parts){
            if(part.getProduct().getProductId()==productId) return true ;
        }
        return false ;
    }
}
