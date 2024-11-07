package thkoeln.archilab.ecommerce.test.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import thkoeln.archilab.ecommerce.test.TestHelper;
import thkoeln.archilab.ecommerce.usecases.*;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static thkoeln.archilab.ecommerce.test.TestHelper.CUSTOMER_DATA;
import static thkoeln.archilab.ecommerce.test.TestHelper.InvalidReason.*;
import static thkoeln.archilab.ecommerce.test.TestHelper.PRODUCT_DATA;

@SpringBootTest
@Transactional
public class ProductCatalogTest {

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
        productCatalogUseCases.deleteProductCatalog();
    }



    @Test
    public void testAddProductToCatalog() {
        // given ...
        testHelper.addAllProducts();

        // when
        Float salesPrice = productCatalogUseCases.getSalesPrice( (UUID) PRODUCT_DATA[4][0] );

        // then
        assertEquals( PRODUCT_DATA[4][5], salesPrice );
    }


    @Test
    public void testAddProductWithInvalidData() {
        // given
        Object[][] invalidProductData = {
                testHelper.getProductDataInvalidAtIndex( 0, NULL ),     // id
                testHelper.getProductDataInvalidAtIndex( 1, NULL ),     // name
                testHelper.getProductDataInvalidAtIndex( 1, EMPTY ),    // name
                testHelper.getProductDataInvalidAtIndex( 2, NULL ),     // description
                testHelper.getProductDataInvalidAtIndex( 2, EMPTY ),    // description
                testHelper.getProductDataInvalidAtIndex( 4, NULL ),     // purchasePrice
                testHelper.getProductDataInvalidAtIndex( 4, ZERO ),     // purchasePrice
                testHelper.getProductDataInvalidAtIndex( 5, NULL ),     // salesPrice
                testHelper.getProductDataInvalidAtIndex( 5, ZERO ),     // purchasePrice
                testHelper.getProductDataInvalidAtIndex( 5, TOO_LOW )   // salesPrice
        };

        // when
        // then
        for ( Object[] invalidProductDataItem : invalidProductData ) {
            StringBuffer invalidDataString = new StringBuffer().append( invalidProductDataItem[0] )
                .append( ", " ).append( invalidProductDataItem[1] ).append( ", " ).append( invalidProductDataItem[2] )
                .append( ", " ).append( invalidProductDataItem[3] ).append( ", " ).append( invalidProductDataItem[4] )
                .append( ", " ).append( invalidProductDataItem[5] );
            assertThrows( ShopException.class, () -> testHelper.addProductDataToCatalog( invalidProductDataItem ),
                          "Invalid data: " + invalidDataString.toString() );
        }
    }


    @Test
    public void testRemoveProductFromCatalog() {
        // given
        testHelper.addAllProducts();
        UUID productId = (UUID) PRODUCT_DATA[4][0];

        // when
        assertDoesNotThrow( () -> productCatalogUseCases.getSalesPrice( productId ) );
        productCatalogUseCases.removeProductFromCatalog( productId );

        // then
        assertThrows( ShopException.class, () -> productCatalogUseCases.getSalesPrice( productId ) );
    }



    @Test
    public void testRemoveNonExistentProduct() {
        // given
        testHelper.addAllProducts();
        UUID nonExistentProductId = UUID.randomUUID();

        // when
        // then
        assertThrows( ShopException.class, () -> productCatalogUseCases.removeProductFromCatalog( nonExistentProductId ) );
    }



    @Test
    public void testRemoveProductThatIsInInventory() {
        // given
        testHelper.addAllProducts();
        UUID productId = (UUID) PRODUCT_DATA[4][0];
        inventoryManagementUseCases.addToInventory( productId, 3 );

        // when
        // then
        assertThrows( ShopException.class, () -> productCatalogUseCases.removeProductFromCatalog( productId ) );
    }


    @Test
    public void testRemoveProductThatIsInShoppingBasketOrOrder() {
        // given
        testHelper.addAllProducts();
        testHelper.registerAllCustomers();
        UUID productId3 = (UUID) PRODUCT_DATA[3][0];
        UUID productId4 = (UUID) PRODUCT_DATA[4][0];
        String customerEmail3 = CUSTOMER_DATA[3][1];
        String customerEmail4 = CUSTOMER_DATA[4][1];
        inventoryManagementUseCases.addToInventory( productId3, 3 );
        inventoryManagementUseCases.addToInventory( productId4, 4 );

        // when
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail3, productId3, 3 );
        shoppingBasketUseCases.addProductToShoppingBasket( customerEmail4, productId4, 4 );
        shoppingBasketUseCases.checkout( customerEmail4 );

        // then
        assertThrows( ShopException.class, () -> productCatalogUseCases.removeProductFromCatalog( productId3 ) );
        assertThrows( ShopException.class, () -> productCatalogUseCases.removeProductFromCatalog( productId4 ) );
    }


    @Test
    public void testClearProductCatalog() {
        // given
        testHelper.addAllProducts();

        // when
        productCatalogUseCases.deleteProductCatalog();

        // then
        assertThrows( ShopException.class, () -> productCatalogUseCases.getSalesPrice( (UUID) PRODUCT_DATA[4][0] ) );
    }

}
