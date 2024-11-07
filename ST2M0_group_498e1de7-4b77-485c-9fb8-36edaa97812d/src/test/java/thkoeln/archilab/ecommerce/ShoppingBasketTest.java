package thkoeln.archilab.ecommerce;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.usecases.*;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static thkoeln.archilab.ecommerce.TestHelper.PRODUCT_DATA;
import static thkoeln.archilab.ecommerce.TestHelper.PRODUCT_INVENTORY;

@SpringBootTest
@Transactional
public class ShoppingBasketTest {

    @Autowired
    private CustomerRegistrationUseCases customerRegistrationUseCases;
    @Autowired
    private ShoppingBasketUseCases shoppingBasketUseCases;
    @Autowired
    private ProductCatalogUseCases productCatalogUseCases;
    @Autowired
    private InventoryManagementUseCases inventoryManagementUseCases;

    private TestHelper testHelper;

    @BeforeEach
    public void setUp() {
        testHelper = new TestHelper( customerRegistrationUseCases, shoppingBasketUseCases,
                                     productCatalogUseCases, inventoryManagementUseCases );
        shoppingBasketUseCases.deleteAllOrders();
        customerRegistrationUseCases.deleteAllCustomers();
        productCatalogUseCases.deleteProductCatalog();
        testHelper.registerAllCustomers();
        testHelper.addAllProducts();
        testHelper.inventoryUpAllProducts();
    }


    @Test
    public void testInvalidAddToShoppingBasket() {
        // given
        UUID nonExistentProductId = UUID.randomUUID();
        String nonExistingEmail = "this@no.nonono";
        UUID productId5 = (UUID) PRODUCT_DATA[5][0];
        UUID productId0 = (UUID) PRODUCT_DATA[0][0];
        UUID productId1 = (UUID) PRODUCT_DATA[1][0];
        UUID productId2 = (UUID) PRODUCT_DATA[2][0];
        String customerEmail0 = TestHelper.CUSTOMER_DATA[0][1];

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId2, 6 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId2, 13 );

        // then
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, nonExistentProductId, 12 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.addProductToShoppingBasket( nonExistingEmail, productId5, 12 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId5, -1 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId0, 1 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId1, 11 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId2, 2 ) );
    }



    @Test
    public void testInvalidRemoveFromShoppingBasket() {
        // given
        UUID nonExistentProductId = UUID.randomUUID();
        String nonExistingEmail = "this@no.nonono";
        UUID productId5 = (UUID) PRODUCT_DATA[5][0];
        UUID productId0 = (UUID) PRODUCT_DATA[0][0];
        UUID productId1 = (UUID) PRODUCT_DATA[1][0];
        UUID productId2 = (UUID) PRODUCT_DATA[2][0];
        String customerEmail0 = TestHelper.CUSTOMER_DATA[0][1];
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId1, 5 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail0, productId2, 15 );

        // when
        shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId1, 2 );
        shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId2, 4 );
        shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId2, 7 );

        // then
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, nonExistentProductId, 12 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.removeProductFromShoppingBasket( nonExistingEmail, productId5, 12 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId5, -1 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId0, 1 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId1, 4 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail0, productId2, 5 ) );
    }



    @Test
    public void testAddRemoveProductsFromAndToShoppingBasket() {
        // given
        UUID productId1 = (UUID) TestHelper.PRODUCT_DATA[1][0];
        UUID productId2 = (UUID) TestHelper.PRODUCT_DATA[2][0];
        String customerEmail3 = TestHelper.CUSTOMER_DATA[3][1];
        String customerEmail5 = TestHelper.CUSTOMER_DATA[5][1];

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId1, 2 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId2, 3 );
        shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail3, productId1, 1 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId1, 0 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId2, 6 );
        // customer3 has 1x productId1 and 9x productId2 in cart
        Map<UUID, Integer> cart3 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail3 );

        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail5, productId1, 2 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail5, productId2, 8 );
        shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail5, productId1, 1 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail5, productId2, 2 );
        // customer5 has 1x productId1 and 10x productId2 in cart
        Map<UUID, Integer> cart5 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail5 );
        int reservedInventory1 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId1 );
        int reservedInventory2 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId2 );

        // then
        assertEquals( 2, cart3.size() );
        assertEquals( 1, cart3.get( productId1 ) );
        assertEquals( 9, cart3.get( productId2 ) );

        assertEquals( 2, cart5.size() );
        assertEquals( 1, cart5.get( productId1 ) );
        assertEquals( 10, cart5.get( productId2 ) );

        assertEquals( 2, reservedInventory1 );
        assertEquals( 19, reservedInventory2 );
    }


    @Test
    public void testImpactOfInventoryCorrectionToOneShoppingBasket() {
        // given
        UUID productId1 = (UUID) TestHelper.PRODUCT_DATA[1][0];
        UUID productId2 = (UUID) TestHelper.PRODUCT_DATA[2][0];
        UUID productId3 = (UUID) TestHelper.PRODUCT_DATA[3][0];
        String customerEmail3 = TestHelper.CUSTOMER_DATA[3][1];

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId1, 6 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId2, 15 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId3, 1 );
        inventoryManagementUseCases.changeInventoryTo( productId1, 4 );
        inventoryManagementUseCases.changeInventoryTo( productId2, 16 );
        inventoryManagementUseCases.changeInventoryTo( productId3, 0 );
        Map<UUID, Integer> cart = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail3 );
        int reservedInventory1 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId1 );
        int reservedInventory2 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId2 );
        int reservedInventory3 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId3 );

        // then
        assertEquals( 4, cart.get( productId1 ) );
        assertEquals( 15, cart.get( productId2 ) );
        assertTrue( cart.get( productId3 ) == null || cart.get( productId3 ) == 0 );
        assertEquals( 4, reservedInventory1 );
        assertEquals( 15, reservedInventory2 );
        assertEquals( 0, reservedInventory3 );
    }


    @Test
    public void testImpactOfInventoryCorrectionToSeveralShoppingBaskets() {
        // given
        UUID productId2 = (UUID) TestHelper.PRODUCT_DATA[2][0];
        String customerEmail3 = TestHelper.CUSTOMER_DATA[3][1];
        String customerEmail6 = TestHelper.CUSTOMER_DATA[6][1];
        String customerEmail9 = TestHelper.CUSTOMER_DATA[9][1];

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId2, 3 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail6, productId2, 6 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail9, productId2, 9 );
        inventoryManagementUseCases.removeFromInventory( productId2, 2 );
        Map<UUID, Integer> cart31 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail3 );
        Map<UUID, Integer> cart61 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail6 );
        Map<UUID, Integer> cart91 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail9 );
        int reservedInventory21 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId2 );

        inventoryManagementUseCases.removeFromInventory( productId2, 8 );
        Map<UUID, Integer> cart32 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail3 );
        Map<UUID, Integer> cart62 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail6 );
        Map<UUID, Integer> cart92 = shoppingBasketUseCases.getShoppingBasketAsMap( customerEmail9 );
        int quantity32 = cart32.get( productId2 ) == null ? 0 : cart32.get( productId2 );
        int quantity62 = cart62.get( productId2 ) == null ? 0 : cart62.get( productId2 );
        int quantity92 = cart92.get( productId2 ) == null ? 0 : cart92.get( productId2 );
        int reservedInventory22 = shoppingBasketUseCases.getReservedInventoryInShoppingBaskets( productId2 );

        // then
        assertEquals( 3, cart31.get( productId2 ) );
        assertEquals( 6, cart61.get( productId2 ) );
        assertEquals( 9, cart91.get( productId2 ) );
        assertEquals( 18, reservedInventory21 );
        assertEquals( 10, reservedInventory22 );
        assertEquals( reservedInventory22, quantity32 + quantity62 + quantity92 );
    }


    @Test
    public void testShoppingBasketValue() {
        // given
        UUID productId3 = (UUID) TestHelper.PRODUCT_DATA[3][0];
        UUID productId6 = (UUID) TestHelper.PRODUCT_DATA[6][0];
        UUID productId8 = (UUID) TestHelper.PRODUCT_DATA[8][0];
        Float price3 = (Float) TestHelper.PRODUCT_DATA[3][5];
        Float price6 = (Float) TestHelper.PRODUCT_DATA[6][5];
        Float price8 = (Float) TestHelper.PRODUCT_DATA[8][5];
        String customerEmail3 = TestHelper.CUSTOMER_DATA[3][1];

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId3, 3 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId6, 2 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId8, 5 );
        // customer3 has 3x productId3, 2x productId6 and 5x productId8 in cart
        float cartValue = shoppingBasketUseCases.getShoppingBasketAsMoneyValue( customerEmail3 );

        // then
        assertEquals( 3 * price3 + 2 * price6 + 5 * price8, cartValue, 0.1f );
    }


    @Test
    public void testShoppingBasketValueInvalid() {
        // given
        String nonExistingEmail = "this@no.never";

        // when
        // then
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.getShoppingBasketAsMoneyValue( nonExistingEmail ) );
    }


    @Test
    public void testCheckout() {
        // given
        UUID productId3 = (UUID) TestHelper.PRODUCT_DATA[3][0];
        UUID productId6 = (UUID) TestHelper.PRODUCT_DATA[6][0];
        UUID productId8 = (UUID) TestHelper.PRODUCT_DATA[8][0];
        int inventory3before = PRODUCT_INVENTORY.get( productId3 );
        int inventory6before = PRODUCT_INVENTORY.get( productId6 );
        int inventory8before = PRODUCT_INVENTORY.get( productId8 );
        String customerEmail3 = TestHelper.CUSTOMER_DATA[3][1];
        Map<UUID, Integer> orderHistoryBefore = shoppingBasketUseCases.getOrderHistory( customerEmail3 );

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId3, inventory3before-2 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId6, 5 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId8, inventory8before );
        shoppingBasketUseCases.checkout( customerEmail3 );
        int inventory3after = inventoryManagementUseCases.getAvailableInventory( productId3 );
        int inventory6after = inventoryManagementUseCases.getAvailableInventory( productId6 );
        int inventory8after = inventoryManagementUseCases.getAvailableInventory( productId8 );
        Map<UUID, Integer> orderHistoryAfter = shoppingBasketUseCases.getOrderHistory( customerEmail3 );

        // then
        assertEquals( 0, orderHistoryBefore.size() );
        assertEquals( 2, inventory3after );
        assertEquals( inventory6before-5, inventory6after );
        assertEquals( 0, inventory8after );
        assertEquals( 3, orderHistoryAfter.size() );
        assertEquals( inventory3before-2, orderHistoryAfter.get( productId3 ) );
        assertEquals( 5, orderHistoryAfter.get( productId6 ) );
        assertEquals( inventory8before, orderHistoryAfter.get( productId8 ) );
    }


    @Test
    public void testCheckoutInvalid() {
        // given
        String nonExistingEmail = "this@no.nono";
        String customerEmail3 = TestHelper.CUSTOMER_DATA[3][1];
        String customerEmail5 = TestHelper.CUSTOMER_DATA[5][1];
        UUID productId2 = (UUID) TestHelper.PRODUCT_DATA[2][0];

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail5, productId2, 4 );
        shoppingBasketUseCases.removeProductFromShoppingBasket( customerEmail5, productId2, 4 );

        // then
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.checkout( nonExistingEmail ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.checkout( customerEmail3 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.checkout( customerEmail5 ) );
    }


    @Test
    public void testorderHistory() {
        // given
        UUID productId1 = (UUID) TestHelper.PRODUCT_DATA[1][0];
        UUID productId2 = (UUID) TestHelper.PRODUCT_DATA[2][0];
        String customerEmail = TestHelper.CUSTOMER_DATA[7][1];
        Map<UUID, Integer> orderHistoryBefore = shoppingBasketUseCases.getOrderHistory( customerEmail );

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId1, 3 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId2, 2 );
        shoppingBasketUseCases.checkout( customerEmail );
        Map<UUID, Integer> orderHistory1 = shoppingBasketUseCases.getOrderHistory( customerEmail );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId1, 6 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId2, 2 );
        shoppingBasketUseCases.checkout( customerEmail );
        Map<UUID, Integer> orderHistory2 = shoppingBasketUseCases.getOrderHistory( customerEmail );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId1, 1 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail, productId2, 6 );
        shoppingBasketUseCases.checkout( customerEmail );
        Map<UUID, Integer> orderHistory3 = shoppingBasketUseCases.getOrderHistory( customerEmail );

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


}
