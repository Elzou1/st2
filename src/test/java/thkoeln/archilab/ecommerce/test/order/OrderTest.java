package thkoeln.archilab.ecommerce.test.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.test.TestHelper;
import thkoeln.archilab.ecommerce.usecases.*;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderTest {

    @Autowired
    private CustomerRegistrationUseCases customerRegistrationUseCases;
    @Autowired
    private ShoppingBasketUseCases shoppingBasketUseCases;
    @Autowired
    private ProductCatalogUseCases productCatalogUseCases;
    @Autowired
    private InventoryManagementUseCases inventoryManagementUseCases;
    @Autowired
    private OrderUseCases orderUseCases;


    private TestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new TestHelper( customerRegistrationUseCases, shoppingBasketUseCases,
                                     productCatalogUseCases, inventoryManagementUseCases );
        orderUseCases.deleteAllOrders();
        shoppingBasketUseCases.emptyAllShoppingBaskets();
        customerRegistrationUseCases.deleteAllCustomers();
        productCatalogUseCases.deleteProductCatalog();
        testHelper.registerAllCustomers();
        testHelper.addAllProducts();
        testHelper.inventoryUpAllProducts();
    }


    @Test
    public void testorderHistory() {
        // given
        UUID productId1 = (UUID) TestHelper.PRODUCT_DATA[1][0];
        UUID productId2 = (UUID) TestHelper.PRODUCT_DATA[2][0];
        String customerEmail = TestHelper.CUSTOMER_DATA[7][1];
        Map<UUID, Integer> orderHistoryBefore = orderUseCases.getOrderHistory( customerEmail );

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId1, 3 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId2, 2 );
        shoppingBasketUseCases.checkout( customerEmail );
        Map<UUID, Integer> orderHistory1 = orderUseCases.getOrderHistory( customerEmail );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId1, 6 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId2, 2 );
        shoppingBasketUseCases.checkout( customerEmail );
        Map<UUID, Integer> orderHistory2 = orderUseCases.getOrderHistory( customerEmail );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId1, 1 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId2, 6 );
        shoppingBasketUseCases.checkout( customerEmail );
        Map<UUID, Integer> orderHistory3 = orderUseCases.getOrderHistory( customerEmail );

        // then
        assertEquals( 0, orderHistoryBefore.size() );
        assertEquals( 2, orderHistory1.size() );
        assertEquals( 2, orderHistory2.size() );
        assertEquals( 2, orderHistory3.size() );
        assertEquals( 3, orderHistory1.get( productId1 ) );
        assertEquals( 2, orderHistory1.get( productId2 ) );
        assertEquals( 9, orderHistory2.get( productId1 ) );
        assertEquals( 4, orderHistory2.get( productId2 ) );
        assertEquals( 10, orderHistory3.get( productId1 ) );
        assertEquals( 10, orderHistory3.get( productId2 ) );
    }


    @Test
    public void testForEmptyOrderHistory() {
        // given
        String customerEmail4 = TestHelper.CUSTOMER_DATA[4][1];

        // when
        // then
        Map<UUID, Integer> orderHistory = orderUseCases.getOrderHistory( customerEmail4 );
        assertEquals( 0, orderHistory.size() );
    }

}
