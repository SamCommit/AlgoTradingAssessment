package com.orderbook;

import com.orderbook.model.Order;
import com.orderbook.model.OrderSide;
import com.orderbook.service.OrderBook;

/**
 * Main class to test the functionality of the OrderBook Class and its dependencies
 * This class tests and shows how to create orders, add them to the order book, modify them, and delete them.
 */
public class Main {
    public static void main(String[] args) {
        // Make an OrderBook instance
        OrderBook orderBook = new OrderBook();

        // Make some buy and sell orders
        Order order1 = new Order(OrderSide.BUY, 100.0, 10);   // Buy order at R100 for 10 units
        Order order2 = new Order(OrderSide.SELL, 101.0, 5);   // Sell order at R101 for 5 units
        Order order3 = new Order(OrderSide.BUY, 99.0, 20);    // Buy order at R99 for 20 units
        Order order4 = new Order(OrderSide.SELL, 102.0, 15);  // Sell order at R102 for 15 units
        Order order5 = new Order(OrderSide.BUY, 99.5, 40);    // Buy order at R99.5 for 40 units
        Order order6 = new Order(OrderSide.SELL, 103.0, 12);  // Sell order at R103 for 12 units
        Order order7 = new Order(OrderSide.BUY, 99.3, 29);    // Buy order at R99.3 for 29 units
        Order order8 = new Order(OrderSide.SELL, 101.2, 14);  // Sell order at R101.2 for 14 units

        // Add the orders to the order book
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        orderBook.addOrder(order5);
        orderBook.addOrder(order6);
        orderBook.addOrder(order7);
        orderBook.addOrder(order8);

        // Print the order book current order book
        System.out.println("Order book after adding orders:");
        System.out.println(orderBook);

        // Change the quantity and price of the first order (order1)
        orderBook.modifyOrder(order1.getId(), 15, 98.0);

        // Print the order book after modification, order1 should have a new timestamp
        System.out.println("Order book after modifying order1 (new quantity: 15, new price: R98.0):");
        System.out.println(orderBook);

        // Delete the second order (order2)
        orderBook.deleteOrder(order2.getId());

        // Print the order book after deleting order2
        System.out.println("Order book after deleting order2 (Sell order at R101 for 5 units):");
        System.out.println(orderBook);

        // Add another order and demonstrate that the priority is lower than the other R99 order 
        Order order9 = new Order(OrderSide.BUY, 99.0, 25);
        orderBook.addOrder(order9);
        
        // Print the order book after adding a new order
        System.out.println("Order book after adding order9 (Buy order at R99 for 25 units):");
        System.out.println(orderBook);
        
        // Change the quantity and price of the second order (order3)
        orderBook.modifyOrder(order3.getId(), 30, 99.0);

        // Print the order book after modification, order3 should now have a lower priority than order9
        System.out.println("Order book after modifying order3 (new quantity: 30, same price: 99.0), priority should be lower:");
        System.out.println(orderBook);
    }
}
