package za.co.rmb.orderbook.service.Implementation;

import za.co.rmb.orderbook.domain.Enum.Side;
import za.co.rmb.orderbook.domain.Order;
import za.co.rmb.orderbook.Exceptions.OrderNotFoundException;
import za.co.rmb.orderbook.service.MatchingEngineService;
import za.co.rmb.orderbook.service.OrderService;

import java.util.List;

public class MatchingEngineImplementation implements MatchingEngineService {
    private final OrderService _orderService;
    public MatchingEngineImplementation(OrderService orderService){
        _orderService =orderService;
    }
    @Override
    public void match(Order order) throws Exception {
        if (order.getSide() == Side.BUY) {
           matchBuyOrder(order);
        } else {
            matchSellOrder(order);
        }
    }
    @Override
    public void matchSellOrder(Order sellOrder) throws Exception {
        List<Order> buyOrders = _orderService.getOrdersByPriceAndSide(sellOrder.getPrice(),Side.BUY);
        for (Order buyOrder:buyOrders) {
            int remainingQuantity = sellOrder.getQuantity();
            if (remainingQuantity == 0) {
                return;

            } else if (buyOrder.getQuantity() <= remainingQuantity) {
                sellOrder.setQuantity(remainingQuantity - buyOrder.getQuantity());
                buyOrder.setQuantity(0);
                _orderService.deleteOrder(buyOrder.getId());
            } else  {
                _orderService.modifyOrder(buyOrder.getId(), (buyOrder.getQuantity() - remainingQuantity));
                sellOrder.setQuantity(0);
            }
        }
        if(sellOrder.getQuantity() >0){
            _orderService.addOrder((sellOrder));
        }
    }
    @Override
    public void matchBuyOrder(Order buyOrder) throws OrderNotFoundException {
        List<Order> sellOrders =_orderService.getOrdersByPriceAndSide(buyOrder.getPrice(),Side.SELL);
        for(Order sellOrder:sellOrders) {
                int remainingQuantity = buyOrder.getQuantity();

                if (remainingQuantity == 0) {
                    return;
                } else if (sellOrder.getQuantity() <= remainingQuantity) {
                    buyOrder.setQuantity(remainingQuantity - sellOrder.getQuantity());
                    sellOrder.setQuantity(0);
                    _orderService.deleteOrder(sellOrder.getId());
                } else  {
                    _orderService.modifyOrder(sellOrder.getId(), (sellOrder.getQuantity() - remainingQuantity));
                    buyOrder.setQuantity(0);
                }
            }
        if (buyOrder.getQuantity() > 0) {
            _orderService.addOrder(buyOrder);
        }
    }
}
