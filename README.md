# Algorithmic Trading Assessment - Samuel de Bruyn

# PART I : Limit Order Book (LOB) Implementation

## Overview

This bojective of this first part of the assessment was to implemented the basic framework for an LOB. The basic functions include the ability add, delete and modify orders, as well as creating the underlying framework (the models) for the order book. Each order has a priority based on its timestamp of creation. Orders are placed in a FIFO (First-In, First-Out) queue. A key specification that was met in the modify order function is that the priority of each order should be reset to lowest priority if an order is modified. The orderbook was implemented with efficiency in mind, ensuring the functions are performed optimally.

To run the demontration code, compile the project and run the src/com/orderbook/Main.java file.

## Project Structure

- **`com.orderbook.model`**: This contains the classes that represent the structure of the order book (Order, OrderSide and OrderType).
- **`com.orderbook.service`**: This contains the class that manages the order book processes and methods (OrderBook).
- **`com.orderbook.test`**: This contains the order book test suite, ensuring that all methods in the orderbook are functionally correct.
- **`com.orderbook`**: This contains the Main class which can be run to demonstrate the order books functionality (and tests it). 

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
- **Add Order**: Adds an order to the specified side of the order book.
- **Modify Order**: Adjusts the quantity of an existing order, causing it to reset its priority.
- **Delete Order**: Removes an order from the orderbook book chosen by its ID.
- **Get Orders**: Retrieves a list of all orders for the specified side of the order book (BUY or SELL).

### OrderBookTest

The OrderBookTest class has all the requisite methods methods to manually test the functionality of the order book:
1. **testAddOrder**: Validates that orders are correctly added to the order book.
2. **testModifyOrder**: Validates that modifying an order correctly updates its quantity and resets its priority.
3. **testDeleteOrder**: Validates that orders are correctly deleted from the order book.
4. **testOrderPriority**: Validates that modifying an order correctly updates its priority, placing it at the end of the queue at its price level.

## c. Data Structures

As mentioned earlier, the two primary data structures used in the LOB are the **TreeMap** and the **Doubly LinkedList**. The TreeMap is used to store orders by their price level, automatically sorting the prices on the BUY and SELL side. TreeMap enables an efficiency of O(log P) for order insertion and retrieval, with P being the number of price levels. Within each price level is a Doubly Linked List which is used to manage the orders, it allows for O(1) complexity for insertion, deletion and re-ordering of orders. The combination of these two data types ensures optimal handling of all functionality in the orderbook to maintain a robust, highly efficient order book.

## Conclusion

This project implements an efficient and simple framework for a LOB which is maintainable and expandable (think market orders, stop orders and integration with matching engines). The chosen data structures ensure the system is highly performant under stress. The unit tests written ensure that expansion the system will not affect its desired functionality and the code-base will retain its integrity and maintainability.
