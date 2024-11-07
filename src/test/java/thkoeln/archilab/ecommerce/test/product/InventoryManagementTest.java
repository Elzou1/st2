package thkoeln.archilab.ecommerce.test.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.test.TestHelper;
import thkoeln.archilab.ecommerce.usecases.*;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static thkoeln.archilab.ecommerce.test.TestHelper.PRODUCT_DATA;
import static thkoeln.archilab.ecommerce.test.TestHelper.PRODUCT_INVENTORY;

@SpringBootTest
@Transactional
public class InventoryManagementTest {

    @Autowired
    private CustomerRegistrationUseCases customerRegistrationUseCases;
    @Autowired
    private ShoppingBasketUseCases shoppingBasketUseCases;
    @Autowired
    private OrderUseCases orderUseCases;
    @Autowired
    private ProductCatalogUseCases productCatalogUseCases;
    @Autowired
    private InventoryManagementUseCases inventoryManagementUseCases;

    private TestHelper testHelper;


    @BeforeEach
    public void setUp() {
        testHelper = new TestHelper( customerRegistrationUseCases, shoppingBasketUseCases,
                                     productCatalogUseCases, inventoryManagementUseCases );
        orderUseCases.deleteAllOrders();
        shoppingBasketUseCases.emptyAllShoppingBaskets();
        customerRegistrationUseCases.deleteAllCustomers();
        productCatalogUseCases.deleteProductCatalog();
        testHelper.addAllProducts();
    }


    @Test
    public void testAddToInventory() {
        // given
        testHelper.inventoryUpAllProducts();
        UUID productId8 = (UUID) PRODUCT_DATA[8][0];

        // when
        int inventory8before = inventoryManagementUseCases.getAvailableInventory( productId8 );
        assertEquals( PRODUCT_INVENTORY.get( productId8 ), inventory8before );
        inventoryManagementUseCases.addToInventory( productId8, 23 );
        int inventory8after = inventoryManagementUseCases.getAvailableInventory( productId8 );
        inventoryManagementUseCases.addToInventory( productId8, 0 );
        int inventory8after2 = inventoryManagementUseCases.getAvailableInventory( productId8 );

        // then
        assertEquals( inventory8before + 23, inventory8after );
        assertEquals( inventory8after, inventory8after2 );
    }



    @Test
    public void testInvalidAddToInventory() {
        // given
        testHelper.inventoryUpAllProducts();
        UUID nonExistentProductId = UUID.randomUUID();
        UUID productId2 = (UUID) PRODUCT_DATA[2][0];

        // when
        // then
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.addToInventory( nonExistentProductId, 12 ) );
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.addToInventory( productId2, -1 ) );
    }


    @Test
    public void testRemoveFromInventory() {
        // given
        testHelper.inventoryUpAllProducts();
        UUID productId6 = (UUID) PRODUCT_DATA[6][0];
        int inventory6before = PRODUCT_INVENTORY.get( productId6 );
        UUID productId9 = (UUID) PRODUCT_DATA[9][0];
        int inventory9before = PRODUCT_INVENTORY.get( productId9 );
        UUID productId0 = (UUID) PRODUCT_DATA[0][0];

        // when
        inventoryManagementUseCases.removeFromInventory( productId6, 1 );
        int inventory6after = inventoryManagementUseCases.getAvailableInventory( productId6 );
        inventoryManagementUseCases.removeFromInventory( productId0, 0 );
        int inventory0after = inventoryManagementUseCases.getAvailableInventory( productId0 );
        inventoryManagementUseCases.removeFromInventory( productId9, inventory9before );
        int inventory9after = inventoryManagementUseCases.getAvailableInventory( productId0 );

        // then
        assertEquals( inventory6before - 1, inventory6after );
        assertEquals( 0, inventory0after );
        assertEquals( 0, inventory9after );
    }


    @Test
    public void testInvalidRemoveFromInventory() {
        // given
        testHelper.inventoryUpAllProducts();
        UUID nonExistentProductId = UUID.randomUUID();
        UUID productId5 = (UUID) PRODUCT_DATA[5][0];
        int inventory5before = PRODUCT_INVENTORY.get( productId5 );
        UUID productId0 = (UUID) PRODUCT_DATA[0][0];

        // when
        // then
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.removeFromInventory( nonExistentProductId, 12 ) );
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.removeFromInventory( productId5, -1 ) );
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.removeFromInventory( productId5, inventory5before+1 ) );
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.removeFromInventory( productId0, 1 ) );
    }


    @Test
    public void testChangeInventory() {
        // given
        testHelper.inventoryUpAllProducts();
        UUID productId10 = (UUID) PRODUCT_DATA[10][0];

        // when
        inventoryManagementUseCases.changeInventoryTo( productId10, 111 );
        int inventory10after = inventoryManagementUseCases.getAvailableInventory( productId10 );

        // then
        assertEquals( 111, inventory10after );
    }


    @Test
    public void testInvalidChangeInventory() {
        // given
        testHelper.inventoryUpAllProducts();
        UUID nonExistentProductId = UUID.randomUUID();
        UUID productId11 = (UUID) PRODUCT_DATA[11][0];

        // when
        // then
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.changeInventoryTo( nonExistentProductId, 12 ) );
        assertThrows( ShopException.class, () -> inventoryManagementUseCases.changeInventoryTo( productId11, -1 ) );
    }

}
