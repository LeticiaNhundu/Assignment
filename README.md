# Assignment
## **Order Class**

The Order class has private fields for id, price, quantity, and side, as well as public getter and setter methods for each field. The generateUniqueId method generates a unique ID for each order using the Random class.
The use of BigDecimal for the price field is appropriate, as it provides high precision for decimal values that are commonly used in financial systems.

However, one area where this code could be improved for efficiency is in the generateUniqueId method. Generating a random long value using Random may not be the most efficient approach, as it may result in collisions that would require retrying to generate a unique ID. A more efficient approach would be to use a counter or atomic variable to generate unique IDs sequentially.

## **Order Service Implementation**

Map<BigDecimal, Map<Long, Order>> bidOrders = new TreeMap<>(Collections.reverseOrder())
Map<BigDecimal, Map<Long, Order>> askOrders = new TreeMap

One way to improve the efficiency of code is to use data structures that are optimized for the task at hand. For example, using a LinkedHashMap and TreeMap to store orders by their ID or price can provide fast retrieval and deletion of orders.
Bid and ask orders are separated into two different data structures for easier and faster sorting and processing of orders. bidOrders are stored a TreeMap with reverseOrder comparator, which is used to sort the keys in descending order with the highest bid price at the top. 
AskOrders are stored in a TreeMap which sorts the keys in ascending order with the lowest ask price at the top.TreeMap provides O(log n) access time for operations like insertion, deletion, and search, which allows for fast and efficient access to specific orders.
 
LinkedHashMap was used to store orders associated with specific prices since insertion order was important and items were required to be retrieved following the order they were inserted. LinkedHashMap provides O(1) access time for operations such as insertion, deletion, and search when accessed by key, allowing for fast and efficient access to specific orders. PriorityQueue could have been a better option if the insertion order did not matter, as it has a time complexity of O(n log n) and LinkedHashMap has O(n) on worst-case scenarios for insertion, deletion, and search.

 ArrayList was used to store the orders at a specific price and side of the market because it provides dynamic size, efficient iteration, and is easy to use.

The implementation of the addOrder method is efficient as it uses the computeIfAbsent method to create a new map for orders at a specific price if one does not already exist.
The implementation of the deleteOrder method is efficient as it removes the order from the map using the remove method, which has a time complexity of O(1).
The implementation of the getOrdersByPriceAndSide method is efficient as it retrieves the map of orders at a specific price based on the given side, and then returns all the orders in that map. This has a time complexity of O(1) for retrieving the map and O(n) for iterating over the orders in the map, where n is the number of orders at the specific price.

The getOrderById method retrieves the order directly from the ordersById map with a time complexity of O(1), making it very efficient.
 
Error handling was partially implemented  to  improve the performance of a system by enabling it to recover quickly from errors. When an error occurs, proper error handling can help the system detect and diagnose the error quickly and return an appropriate response.


## **Matching Engine**

The matchSellOrder method retrieves the buy orders with the same price as the given sell order, and iterates over them to match the sell order with the appropriate buy orders. The method modifies the remaining quantity of the sell order and the matched buy orders, and deletes the buy orders that have been completely filled. If there is any remaining quantity for the sell order, it is added back to the order book using the addOrder method.

The matchBuyOrder method retrieves the sell orders with the same price as the given buy order, and iterates over them to match the buy order with the appropriate sell orders. The method modifies the remaining quantity of the buy order and the matched sell orders, and deletes the sell orders that have been completely filled. If there is any remaining quantity for the buy order, it is added back to the order book using the addOrder method.

getOrdersByPriceAndSide method is used  to retrieve the relevant orders quickly.

Another way to improve performance is to minimize the number of unnecessary operations that are performed. For example, in the matchSellOrder and matchBuyOrder methods, we exit the loop early if the order has been completely filled ,  rather than continuing to iterate over the remaining orders.

