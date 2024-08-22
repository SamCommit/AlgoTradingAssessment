# Algorithmic Trading Assessment - Samuel de Bruyn

# Introduction

This assessment consisted of two parts with two objectives. The initial objective was to implement a simple framework for a Limit Order Book (LOB). The second objective was to design and create a simple matching engine to match orders on the buy and sell side of the orderbook. To run the a full demonstration of the assessment. One can compile and run the *src\com\orderbook\Main.java* main method. To test the order book functionality one can compile and run the *src\com\orderbook\test\OrderBookTest.java* main method. Finally to test the matching engine functionality one can compile and run the *src\com\orderbook\test\MatchingEngineTest.java* main method.

## Project Structure

- **`com.orderbook.model`**: This contains the classes that represent the structure of the order book (Order, OrderSide and OrderType).
- **`com.orderbook.service`**: This contains the OrderBook class that manages the order book processes and methods (OrderBook) and now also controls the matching engine class which observes the LOB and takes trade execution actions if they exist (MatchingEngine).
- **`com.orderbook.test`**: This contains the order book and matching engine test suite, ensuring that all methods in the orderbook and matching engine are functionally correct.
- **`com.orderbook`**: This contains the Main class which can be run to demonstrate the order book and matching engine functionality (and tests it).

# PART I : Limit Order Book (LOB) Implementation

## Overview

This ojective of this first part of the assessment was to implemented the basic framework for an LOB. The basic functions include the ability add, delete and modify orders, as well as creating the underlying framework (the models) for the order book. Each order has a priority based on its timestamp of creation. Orders are placed in a FIFO (First-In, First-Out) queue. A key specification that was met in the modify order function is that the priority of each order should be reset to lowest priority if an order is modified. The orderbook was implemented with efficiency in mind, ensuring the functions are performed optimally.

To run the demontration code, compile the project and run the *src/com/orderbook/Main.java* main method. To run only the order book tests, compile and run only the *src\com\orderbook\test\OrderBookTest.java* main method.

## a. Efficiency Mechanisms

### Data Structures

The efficiency mechanisms were chosen for this project based on the high workload of all functions, adding, modifying and deleting (deletion specifically given the context of algorithmic trading).

- The first key mechanisms chosen for efficiency is the **TreeMap**, as it efficiently ensures price-level sorting is done on each insert and delete, unlike a HashMap or PriorityQueue. Despite a HashMap providing O(1) time complexity for insertions and lookups (O(log(n) for TreeMap), it lacks the ability to efficiently retrieve the sorted data making it less suited for orderbooks where best prices need to be frequently accessed. Modification of an order involved the deletion and re-insertion of an order from the queue, to which the TreeMap is well suited due to it's O(log P) time complexity for both operations, P being the number of elements at that price-level. 
- The second key mechanism is the **Doubly LinkedList** which has an O(1) time complexity for insertions and deletions. It was chosen over a single linked list, despite the higher overhead, because a doubly linked list is more efficient for deletions and modifications (which happen frequently in the orderbook) as it does not need to traverse the entire linked list as a singly linked list does.

## b. Solution approach

### Solution Approach

1. **Design**: The order books design is modular where each component (Order, OrderSide, OrderType and OrderBook) is defined uniquely for expansion on the project and ease of maintenance.
2. **Priority Management**: Priority is managed using timestamps on the Order object level, ensuring that orders with the same price are executed in the order they were added and that order book integrity is maintained. Modifications to orders reset their timestamps at the order Object level, ensuring they lose priority.
3. **Efficiency**: The use of TreeMap and LinkedList are the key efficiency mechanisms in this project

### Components:

### Order

The Order object describes a single order in the LOB. Each order has the following attributes:

- **ID**: The unique identifier per order, generated using a UUID.
- **Side**: The side of the order (BUY or SELL).
- **Price**: The price at which the order is placed.
- **Quantity**: The quantity of the asset in the order, when it's modified the timestamp of the order is reset.
- **Timestamp**: A timestamp representing when the order was added or modified, which determines the orders priority.

### OrderBook

The OrderBook class is the heart of the order book which contains the functionality necessary for it to operate.

- **Buy Orders**: These are stored in a TreeMap and sorted by price from highest to lowest.
- **Sell Orders**: These are also stored in a TreeMap and sorted by price from lowest to highest.

The order book supports this functionality:
- **addOrder**: Adds an order to the specified side of the order book.
- **modifyOrder**: Adjusts the quantity of an existing order, causing it to reset its priority.
- **deleteOrder**: Removes an order from the orderbook book chosen by its ID.
- **getOrders**: Retrieves a list of all orders for the specified side of the order book (BUY or SELL).

### OrderBookTest

The OrderBookTest class has all the requisite methods methods to manually test the functionality of the order book:
1. **testAddOrder**: Validates that orders are correctly added to the order book.
2. **testModifyOrder**: Validates that modifying an order correctly updates its quantity and resets its priority.
3. **testDeleteOrder**: Validates that orders are correctly deleted from the order book.
4. **testOrderPriority**: Validates that modifying an order correctly updates its priority, placing it at the end of the queue at its price level.

## c. Data Structures

As mentioned earlier, the two primary data structures used in the LOB are the **TreeMap** and the **Doubly LinkedList**. The TreeMap is used to store orders by their price level, automatically sorting the prices on the BUY and SELL side. TreeMap enables an efficiency of O(log P) for order insertion and retrieval, with P being the number of price levels. Within each price level is a Doubly Linked List which is used to manage the orders, it allows for O(1) complexity for insertion, deletion and re-ordering of orders. The combination of these two data types ensures optimal handling of all functionality in the orderbook to maintain a robust, highly efficient order book.

## Part 1 Conclusion

This part implements an efficient and simple framework for a LOB which is maintainable and expandable (think market orders, stop orders and integration with matching engines). The chosen data structures ensure the system is highly performant under stress. The unit tests written ensure that expansion the system will not affect its desired functionality and the code-base will retain its integrity and maintainability.

# PART II : Matching Engine Implementation

## Overview

This ojective of this second part of the assessment was to design and create a simple matching engine to match orders on the buy and sell side of the orderbook. The basic functions include matching buy orders, matching sell orders and partially matching orders on both sides of the order book. The matching engine observes the LOB and takes any trade execution actions if they exist. One of the key specifications met is that orders should be executed in the correct priority.

To run the demontration code, compile the project and run the *src/com/orderbook/Main.java* main method. To run only the matching engine tests, compile and run only the *src\com\orderbook\test\MatchingEngineTest.java* main method.

## a. Efficiency Mechanisms

### Data Structures

Once again, the efficiency mechanisms chosen for Part II of this project are based on the forecasted high workload of all matching functions.

- Again, a crucial mechanisms chosen for efficiency is the **TreeMap<Double, LinkedList<Order>>** for storing orders at different price levels, due to its efficient assurance of price-level sorting, unlike a HashMap or PriorityQueue which would require the complexity of additional sorting. This is critical for the matching engine functionality as the price level-sorting allows us to terminate matching iteration when the price falls outside of the prices possible for matching. It also ensures that the matching is always done on the correct orders on the opposite side of the orderbook.

- The second key mechanism is the **LinkedList** which allows for efficient insertion and removal of orders which is important for partially or fully filled orders requiring frequent updates. Orders are also then processed in a FIFO manner which aligns with the order book rules.

- The third mechanism is efficient matching. The *matchOrder* method iterates through the *TreeMap* at each price level, and then iterates through the list of orders at that price level. The matchOrder method is efficient because is directly accesses the most favorable price levels first dues to the TreeMap data structure and processes the orders linearly at each price level. Termination then occures as soon as matching is no longer possible to ensure no wasted overhead iteration.

## b. Solution approach

### Solution Approach

1. **Design**: The matching engine design is efficient thanks to the *TreeMap* and *LinkedList* data structures which ensure sorting and efficient, linear access respectively.
2. **Time complexity**: The TreeMap of the price-levels is O(logP) efficient for inserting new orders and the LinkedList is O(1) efficient for adding, with an overall complexity of O(logP) for adding an order using the *processOrder* method. The *matchOrder* method then has a complexity of O(log(P+N)), P being the price-level and N being the number of orders at that price level. However because the order book is already sorted the efficiency will almost always be near O(1) complexity for execution.
3. **Efficiency**: The use of TreeMap and LinkedList are once again key efficiency mechanisms in this matching engine. The efficient design and early termination of the *matchOrder* method itself is other key efficiency mechanism.

### Components:

### MatchingEngine

The MatchingEngine class contains all the methods necessary to match orders and fill or partially fill them from the given orderbook in question.

- **processOrder**: Processes an incoming order and attempts to match it to orders in the opposite side of the order book
- **matchOrder**: Is the key method which matches incoming orders with the orders in the opposite side of the orderbook by iterating through the orderbook and computing the matching of the appropriate orders efficiently for fully filled and partially filled matches.

### MatchingEngineTest

The MatchingEngineTest class has all the requisite methods methods to manually test the functionality of the matching engine:
1. **testSellOrderMatching**: Validates whether the MatchingEngine correctly processes a sell order.
2. **testBuyOrderMatching**: Validates whether the MatchingEngine correctly processes a buy order.
3. **testPartialFill**: Validates whether the MatchingEngine correctly handles a partial fill.
4. **testUnmatchedOrder**: Validates whether the MatchingEngine correctly adds an unmatched order to the order book.

## c. Data Structures

As mentioned, the two primary data structures once again are the **TreeMap** and the **LinkedList**. The TreeMaps automatic sorting makes it an optimal solution for an order book with high volumes. Within each price level is a LinkedList which allows for efficient insetion, removal and modification which is critical for efficient partial and full matching of orders.

## Part II Conclusion

This part implements an efficient and simple framework for a matching engine which is also expandable to other order types. The chosen data structures ensure the system will perform optimally under heavy order loading. Once again the unit tests written ensure that expansion the system will not affect its desired functionality and the code-base will retain its integrity and maintainability.
