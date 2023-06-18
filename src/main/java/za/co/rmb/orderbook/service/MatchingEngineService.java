package za.co.rmb.orderbook.service;

import za.co.rmb.orderbook.domain.Order;

public interface MatchingEngineService {
    void match(Order order);
    void matchSellOrder(Order sellOrder);
    void matchBuyOrder(Order buyOrder);
}
