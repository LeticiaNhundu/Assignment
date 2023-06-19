package za.co.rmb.orderbook.Exceptions;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(long orderId) {
        super("Order with ID " + orderId + " not found.");
    }

}
