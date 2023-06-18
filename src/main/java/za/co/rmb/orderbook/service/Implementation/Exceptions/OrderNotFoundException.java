package za.co.rmb.orderbook.service.Implementation.Exceptions;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
