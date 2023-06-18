package za.co.rmb.orderbook.service.Implementation;

import za.co.rmb.orderbook.domain.Enum.Side;
import za.co.rmb.orderbook.domain.Order;
import za.co.rmb.orderbook.service.OrderService;

import java.math.BigDecimal;
import java.util.*;

public class OrderServiceImplementation implements OrderService {
    private final Map<BigDecimal, Map<Long, Order>> bidOrders;
    private final Map<BigDecimal, Map<Long, Order>> askOrders;

    public OrderServiceImplementation() {

        bidOrders = new TreeMap<>(Collections.reverseOrder());
        askOrders = new TreeMap<>();

    }
    @Override
    public long addOrder(Order order) {
        Map<Long, Order> ordersAtPrice;
        BigDecimal price = order.getPrice();
        Side side = order.getSide();

        ordersAtPrice = (side == Side.BUY) ? bidOrders.computeIfAbsent(price, k -> new LinkedHashMap<>())
                : askOrders.computeIfAbsent(price, k -> new LinkedHashMap<>());
        ordersAtPrice.put(order.getId(), order);

        return order.getId();
    }
    @Override
    public void deleteOrder(long orderId) {
        Order order = getOrderById(orderId);
        if(order != null) {
            Map<Long, Order> orderAtPrice = (order.getSide() == Side.BUY) ? bidOrders.get(order.getPrice()) : askOrders.get(order.getPrice());
            if (orderAtPrice.remove(orderId) != null) {
                return;
            }
        }
    }
    @Override
    public void modifyOrder(long orderId, int newQuantity) {
     Order order = getOrderById(orderId);
      if(order !=null) {
        deleteOrder(orderId);
        order.setQuantity(newQuantity);
        addOrder(order);
      }
    }

    public List<Order> getSellOrders() {
        List<Order> sellOrders = new ArrayList<>();
        for (Map<Long, Order> ordersAtPrice : askOrders.values()) {
            sellOrders.addAll(ordersAtPrice.values());
        }
        return sellOrders;
    }
    @Override
    public Order getOrderById(long orderId) {
        for (Map<Long, Order> ordersAtPrice : bidOrders.values()) {
            Order order = ordersAtPrice.get(orderId);
            if (order != null) {
                return order;
            }
        }
        for (Map<Long, Order> ordersAtPrice : askOrders.values()) {
            Order order = ordersAtPrice.get(orderId);
            if (order != null) {
                return order;
            }
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByPriceAndSide(BigDecimal price , Side side) {
        List<Order>allOrders = new ArrayList<>();
        Map<Long, Order> ordersAtPrice = (side == Side.BUY) ? bidOrders.get(price):askOrders.get(price);
            if (ordersAtPrice != null) {
                allOrders.addAll(ordersAtPrice.values());
            }
        return allOrders;
    }
}
