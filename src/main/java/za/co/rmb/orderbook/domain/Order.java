package za.co.rmb.orderbook.domain;

import za.co.rmb.orderbook.domain.Enum.Side;

import java.math.BigDecimal;
import java.util.Random;

public class Order {
    private final long id;
    private final BigDecimal price;
    private int quantity;
    private final Side side;

    public Order( BigDecimal price, int quantity, Side side){
        this.id = generateUniqueId();
        this.price = price;
        this.quantity = quantity;
        this.side =side;
    }
    public static long generateUniqueId() {
        Random random = new Random();
        return random.nextLong();
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Side getSide() {
        return side;
    }

}
