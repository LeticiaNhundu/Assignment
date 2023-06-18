package za.co.rmb.orderbook.service;

import org.junit.Before;
import org.junit.Test;
import za.co.rmb.orderbook.domain.Enum.Side;
import za.co.rmb.orderbook.domain.Order;
import za.co.rmb.orderbook.service.Implementation.MatchingEngineImplementation;
import za.co.rmb.orderbook.service.Implementation.OrderServiceImplementation;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MatchingEngineServiceTest {
    private Order buyOrder;
    private Order sellOrder1;
    private Order sellOrder2;
    private Order sellOrder3;
    private Order sellOrder4;
    private Order sellOrder5;
    private Order sellOrder;
    private Order buyOrder1;
    private Order buyOrder2;
    private Order buyOrder3;
    private Order buyOrder4;
    private Order buyOrder5;
    private MatchingEngineImplementation matchingEngineImplementation;

    private OrderServiceImplementation orderServiceImplementation;
    @Before
    public void setUp() {
        buyOrder = new Order(BigDecimal.valueOf(100), 100, Side.BUY);
        sellOrder1 = new Order(BigDecimal.valueOf(100), 40, Side.SELL);
        sellOrder2 = new Order(BigDecimal.valueOf(100), 50, Side.SELL);
        sellOrder3 = new Order(BigDecimal.valueOf(100), 20, Side.SELL);
        sellOrder4 = new Order(BigDecimal.valueOf(100), 30, Side.SELL);
        sellOrder5 = new Order(BigDecimal.valueOf(100), 100, Side.SELL);

        sellOrder = new Order(BigDecimal.valueOf(50), 100, Side.SELL);
        buyOrder1 = new Order(BigDecimal.valueOf(50), 40, Side.BUY);
        buyOrder2 = new Order(BigDecimal.valueOf(50), 80, Side.BUY);
        buyOrder3 = new Order(BigDecimal.valueOf(50), 20, Side.BUY);
        buyOrder4 = new Order(BigDecimal.valueOf(50), 100, Side.BUY);
        buyOrder5 = new Order(BigDecimal.valueOf(50), 100, Side.BUY);
        orderServiceImplementation = mock(OrderServiceImplementation.class);

        matchingEngineImplementation = new MatchingEngineImplementation(orderServiceImplementation);
    }

    @Test
    public void testMatchBuyOrder_NoMatchingSellOrders() {

        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(100)), eq(Side.SELL)))
                .thenReturn(Collections.emptyList());

        matchingEngineImplementation.matchBuyOrder(buyOrder);

        // verify that the addOrder method was called with the buy order
        verify(orderServiceImplementation, times(1)).addOrder(eq(buyOrder));

        // verify that the buy order quantity was not modified
        assertEquals(100, buyOrder.getQuantity());
    }
    @Test
    public void testMatchBuyOrder_SingleMatchingSellOrder_FullyFillsBuyOrder() {
        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(100)), eq(Side.SELL)))
                .thenReturn(Collections.singletonList(sellOrder5));

        matchingEngineImplementation.matchBuyOrder(buyOrder);

        // verify that the deleteOrder method was called with the sell order
        verify(orderServiceImplementation, times(1)).deleteOrder(eq(sellOrder5.getId()));

        // verify that the buy order quantity was fully filled
        assertEquals(0, buyOrder.getQuantity());

        // verify that the sell order quantity was fully filled
        assertEquals(0, sellOrder5.getQuantity());
    }

    @Test
    public void testMatchBuyOrder_SingleMatchingSellOrder_PartiallyFillsBuyOrder() {

        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(100)), eq(Side.SELL)))
                .thenReturn(Collections.singletonList(sellOrder1));

        matchingEngineImplementation.match(buyOrder);

        // Verify that the sell order quantity was partially filled
        assertEquals(0, sellOrder1.getQuantity());

        // Verify that the buy order quantity was partially filled
        assertEquals(60, buyOrder.getQuantity());

    }
    @Test
    public void testMatchBuyOrder_MultipleMatchingSellOrders_FullyOrPartiallyFillBuyOrder() {
        List<Order> sellOrders = Arrays.asList(sellOrder1, sellOrder2,sellOrder3,sellOrder4);
        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(100)), eq(Side.SELL)))
                .thenReturn(sellOrders);
        matchingEngineImplementation.match(buyOrder);

        // Verify that the sell orders were fully or partially filled
        assertEquals(0, sellOrder1.getQuantity());
        assertEquals(0, sellOrder2.getQuantity());
        assertEquals(30, sellOrder4.getQuantity());
        // Verify that the buy order was fully or partially filled
        assertEquals(0, buyOrder.getQuantity());

    }

    @Test
    public void testMatchSellOrder_NoMatchingBuyOrders() {
        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(50)), eq(Side.BUY)))
                .thenReturn(Collections.emptyList());

        matchingEngineImplementation.match(sellOrder);

        // verify that the addOrder method was called with the buy order
        verify(orderServiceImplementation, times(1)).addOrder(eq(sellOrder));

        // verify that the buy order quantity was not modified
        assertEquals(100, sellOrder.getQuantity());
    }
    @Test
    public void testMatchSellOrder_SingleMatchingBuyOrder_FullyFillsBuyOrder() {
        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(50)), eq(Side.BUY)))
                .thenReturn(Collections.singletonList(buyOrder5));

        matchingEngineImplementation.match(sellOrder);

        // verify that the deleteOrder method was called with the buy order
        verify(orderServiceImplementation, times(1)).deleteOrder(eq(buyOrder5.getId()));

        // verify that the sell order quantity was fully filled
        assertEquals(0, sellOrder.getQuantity());

        // verify that the buy order quantity was fully filled
        assertEquals(0, buyOrder5.getQuantity());
    }

    @Test
    public void testMatchSellOrder_SingleMatchingBuyOrder_PartiallyFillsBuyOrder() {

        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(50)), eq(Side.BUY)))
                .thenReturn(Collections.singletonList(buyOrder1));

        matchingEngineImplementation.match(sellOrder);

        // Verify that the buy order quantity was partially filled
        assertEquals(0, buyOrder1.getQuantity());

        // Verify that the sell order quantity was partially filled
        assertEquals(60, sellOrder.getQuantity());

    }
    @Test
    public void testMatchSellOrder_MultipleMatchingBuyOrders_FullyOrPartiallyFillBuyOrder() {
        List<Order> buyOrders = Arrays.asList(buyOrder1, buyOrder2,buyOrder3,buyOrder4);
        when(orderServiceImplementation.getOrdersByPriceAndSide(eq(BigDecimal.valueOf(50)), eq(Side.BUY)))
                .thenReturn(buyOrders);
        matchingEngineImplementation.match(sellOrder);

        // Verify that the buy orders were fully or partially filled
        assertEquals(0, buyOrder1.getQuantity());
        assertEquals(20, buyOrder3.getQuantity());
        assertEquals(100, buyOrder4.getQuantity());
        // Verify that the sell order was fully or partially filled
        assertEquals(0, sellOrder.getQuantity());

    }
}