package za.co.rmb.orderbook.service.Implementation;

import za.co.rmb.orderbook.domain.Enum.Side;
import za.co.rmb.orderbook.domain.Order;
import za.co.rmb.orderbook.Exceptions.OrderNotFoundException;
import za.co.rmb.orderbook.service.OrderService;

import java.math.BigDecimal;
import java.util.*;
public class OrderServiceImplementation implements OrderService {
    private final Map<BigDecimal, Map<Long, Order>> bidOrders;
    private final Map<BigDecimal, Map<Long, Order>> askOrders;
    private final Map<Long, Order> ordersById;

    public OrderServiceImplementation() {
        bidOrders = new TreeMap<>(Collections.reverseOrder());
        askOrders = new TreeMap<>();
        ordersById = new HashMap<>();
    }

    @Override
    public long addOrder(Order order) {
        Map<Long, Order> ordersAtPrice;
        BigDecimal price = order.getPrice();
        Side side = order.getSide();

        ordersAtPrice = (side == Side.BUY) ? bidOrders.computeIfAbsent(price, k -> new LinkedHashMap<>())
                : askOrders.computeIfAbsent(price, k -> new LinkedHashMap<>());
        ordersAtPrice.put(order.getId(), order);

        ordersById.put(order.getId(), order);

        return order.getId();
    }

    @Override
    public void deleteOrder(long orderId) throws OrderNotFoundException {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        Map<Long, Order> orderAtPrice = (order.getSide() == Side.BUY) ? bidOrders.get(order.getPrice()) : askOrders.get(order.getPrice());
        if (orderAtPrice.remove(orderId) == null) {
            throw new RuntimeException("Failed to remove order with ID " + orderId);
        }

        ordersById.remove(orderId);
    }

    @Override
    public void modifyOrder(long orderId, int newQuantity) throws OrderNotFoundException {
        Order order = getOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        order.setQuantity(newQuantity);
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
        return ordersById.get(orderId);
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