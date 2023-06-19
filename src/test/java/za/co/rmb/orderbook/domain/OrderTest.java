package za.co.rmb.orderbook.domain;

import org.junit.Test;
import za.co.rmb.orderbook.domain.Enum.Side;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class OrderTest {
    public BigDecimal price =BigDecimal.valueOf(100);
    public int quantity =20;
    Side side = Side.BUY;
    @Test
    public void testOrderConstructor_AndGet(){
     Order order = new Order(price,quantity,side);

        assertEquals(price,order.getPrice());
        assertEquals(quantity,order.getQuantity());
        assertEquals(side,order.getSide());
    }
    @Test
    public void testGenerateUniqueId() {
        Set<Long> idSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            long id = Order.generateUniqueId();
            idSet.add(id);
        }
        assertEquals(1000, idSet.size());
    }
    @Test
    public void testSetQuantity(){
        Order order = new Order(price,quantity,side);
          order.setQuantity(100);
        assertEquals(100,order.getQuantity());
    }
}
