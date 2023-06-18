package za.co.rmb.orderbook.service;

import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.domain.Enum.Side;
import za.co.rmb.orderbook.domain.Order;
import za.co.rmb.orderbook.service.Implementation.OrderServiceImplementation;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceTest {
    private Order order ;
    private Order deleteOrder ;
    private Order modifyOrder ;
    private Order modifyOrder1 ;
    private Order modifyOrder2 ;

    private OrderServiceImplementation orderServiceImplementation;
    @Before
    public void setUp(){
        order = new Order(BigDecimal.valueOf(9),10,Side.BUY);
        deleteOrder = new Order(BigDecimal.valueOf(100),10,Side.SELL);
        modifyOrder = new Order(BigDecimal.valueOf(100),20,Side.SELL);
        modifyOrder1 = new Order(BigDecimal.valueOf(10),50,Side.SELL);
        modifyOrder2 = new Order(BigDecimal.valueOf(50),2,Side.SELL);

       orderServiceImplementation = new OrderServiceImplementation();
    }
    @Test
    public void testAddOrder() {
        long orderId = orderServiceImplementation.addOrder(order);
        List<Order> orders = orderServiceImplementation.getOrdersByPriceAndSide(order.getPrice(),order.getSide());
        assertTrue(orders.contains(order));
        assertEquals(orderId, order.getId());
    }
    @Test
    public void testDeleteOrder() {
        long orderId = orderServiceImplementation.addOrder(deleteOrder);
        orderServiceImplementation.deleteOrder(orderId);
        Order o = orderServiceImplementation.getOrderById(orderId);
        assertNull(o);

    }
    @Test
    public void testModifyOrder() {
        long orderId = orderServiceImplementation.addOrder(modifyOrder);
        orderServiceImplementation.modifyOrder(orderId,50);
        orderServiceImplementation.getOrderById(orderId);

        assertEquals(orderId,modifyOrder.getId());
        assertEquals(50,modifyOrder.getQuantity());
    }
    @Test
    public void testModifyOrder_LostPriority() {
        long orderId = orderServiceImplementation.addOrder(modifyOrder);
        orderServiceImplementation.addOrder(modifyOrder1);
        orderServiceImplementation.addOrder(modifyOrder2);

        orderServiceImplementation.modifyOrder(orderId,50);

        List<Order> sellOrders =orderServiceImplementation.getSellOrders();
        assertEquals(modifyOrder1, sellOrders.get(0));
        assertEquals(modifyOrder2, sellOrders.get(1));
        assertEquals(modifyOrder, sellOrders.get(2));

    }
}
