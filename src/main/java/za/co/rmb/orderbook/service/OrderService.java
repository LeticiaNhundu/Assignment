package za.co.rmb.orderbook.service;

import za.co.rmb.orderbook.domain.Enum.Side;
import za.co.rmb.orderbook.domain.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    long addOrder(Order order);
    void deleteOrder(long orderId);
    void modifyOrder(long orderId,int newQuantity);
    Order getOrderById(long orderId);
    List<Order> getSellOrders();
    List<Order> getOrdersByPriceAndSide(BigDecimal price, Side side);
}
