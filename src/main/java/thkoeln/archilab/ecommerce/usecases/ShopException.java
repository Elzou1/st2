package thkoeln.archilab.ecommerce.usecases;

import java.util.function.Supplier;

public class ShopException extends RuntimeException implements Supplier<ShopException> {
    public ShopException( String message ) {
        super( message );
    }

    @Override
    public ShopException get() {
        return this;
    }
}
