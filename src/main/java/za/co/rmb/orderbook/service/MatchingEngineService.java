package za.co.rmb.orderbook.service;

import za.co.rmb.orderbook.domain.Order;
import za.co.rmb.orderbook.service.Implementation.Exceptions.OrderNotFoundException;

public interface MatchingEngineService {
    void match(Order order) throws Exception;
    void matchSellOrder(Order sellOrder) throws Exception;
    void matchBuyOrder(Order buyOrder) throws OrderNotFoundException;
}
