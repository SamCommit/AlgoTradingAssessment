package com.orderbook;

import com.orderbook.model.Order;
import com.orderbook.model.OrderSide;
import com.orderbook.service.OrderBook;
import com.orderbook.test.OrderBookTest;
import com.orderbook.service.MatchingEngine;
import com.orderbook.test.MatchingEngineTest;

/**
 * Main class to test the functionality of the OrderBook Class, the MatchingEngine Class and their dependencies
 * This class tests and shows how to create orders, add them to the order book, modify them, and delete them.
 * This class also demonstrated the matching engines ability to match orders on both sides of the order book, including partial fills.
 */
public class Main {
    public static void main(String[] args) {
    	
        System.out.println("=====================");
        System.out.println(" __      __ ___     ");
        System.out.println("|__) /\\ |__) |    /|");
        System.out.println("|   /--\\| \\  |     |");
        System.out.println("                    ");
        System.out.println("=====================");
    	
        // Make an OrderBook instance
        OrderBook orderBook = new OrderBook();

        // Make some buy and sell orders
        Order order1 = new Order(OrderSide.BUY, 90.0, 10);   // Buy order at R100 for 10 units
        Order order2 = new Order(OrderSide.SELL, 111.0, 5);   // Sell order at R101 for 5 units
        Order order3 = new Order(OrderSide.BUY, 89.0, 20);    // Buy order at R99 for 20 units
        Order order4 = new Order(OrderSide.SELL, 112.0, 15);  // Sell order at R102 for 15 units
        Order order5 = new Order(OrderSide.BUY, 89.5, 40);    // Buy order at R99.5 for 40 units
        Order order6 = new Order(OrderSide.SELL, 113.0, 12);  // Sell order at R103 for 12 units
        Order order7 = new Order(OrderSide.BUY, 89.3, 29);    // Buy order at R99.3 for 29 units
        Order order8 = new Order(OrderSide.SELL, 111.2, 14);  // Sell order at R101.2 for 14 units
        Order order9 = new Order(OrderSide.BUY, 89, 12);      // Buy order at R99 for 12 units
        Order order10 = new Order(OrderSide.SELL, 110.8, 50); // Sell order at R100.8 for 50 units

        // Add the orders to the order book
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        orderBook.addOrder(order5);
        orderBook.addOrder(order6);
        orderBook.addOrder(order7);
        orderBook.addOrder(order8);
        orderBook.addOrder(order9);
        orderBook.addOrder(order10);
        
        // Run the add orders test
        OrderBookTest.testAddOrder(orderBook);
        
        // Print the order book current order book
        System.out.println("Order book after adding orders:");
        System.out.println(orderBook);
        
        // Run the modify order test
        OrderBookTest.testModifyOrder(orderBook, order1);
        
        // Change the quantity of the first order (order1)
        orderBook.modifyOrder(order1.getId(), 15);

        // Print the order book after modification, order1 should have a new timestamp
        System.out.println("Order book after modifying order1 (Buy order at R90 for 10 units) (new quantity: 15):");
        System.out.println(orderBook);
        
        // Run the delete order test
        OrderBookTest.testDeleteOrder(orderBook, order4);
        
        // Delete the second order (order2)
        orderBook.deleteOrder(order2.getId());
        
        // Print the order book after deleting order2
        System.out.println("Order book after deleting order2 (Sell order at R111 for 5 units):");
        System.out.println(orderBook);

        // Add another order and demonstrate that the priority is lower than the other R99 order 
        Order order11 = new Order(OrderSide.BUY, 89.0, 25);
        orderBook.addOrder(order11);
        
        // Print the order book after adding a new order
        System.out.println("Order book after adding order11 (Buy order at R89 for 25 units):");
        System.out.println(orderBook);
        
        // Run the order priority test
        OrderBookTest.testOrderPriority(orderBook, order3);
        
        // Change the quantity of the third order (order3)
        orderBook.modifyOrder(order3.getId(), order3.getQuantity() + 20);

        // Print the order book after modification, order3 should now have a lower priority than order9
        System.out.println("Order book after modifying order3 (new quantity: 50), priority should be lower:");
        System.out.println(orderBook);
        

        System.out.println("=======================");
        System.out.println(" __      __ ___   __ ");
        System.out.println("|__) /\\ |__) |    __|");
        System.out.println("|   /--\\| \\  |   |__ ");
        System.out.println("                     ");
        System.out.println("=======================");
        System.out.println("                     ");
        

        // Create a MatchingEngine instance
        MatchingEngine matchingEngine = new MatchingEngine(orderBook);

        // Run the sell order matching test
        System.out.println("=============================================================================================");
        System.out.println("Order book before Sell Order Matching Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testSellOrderMatching(orderBook, matchingEngine);
        System.out.println("Test: Add two buy orders (99.9, qty: 10 and 99.7, qty: 6), then process a sell order (99.9, qty: 8).\n");
        System.out.println("Order book after Sell Order Matching Test:");
        System.out.println(orderBook);

        // Run the buy order matching test
        System.out.println("=============================================================================================");
        System.out.println("\nOrder book before Buy Order Matching Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testBuyOrderMatching(orderBook, matchingEngine);
        System.out.println("Test: Add two sell orders (102, qty: 5 and 101, qty: 10), then process a buy order (102, qty: 8).\n");
        System.out.println("Order book after Buy Order Matching Test:");
        System.out.println(orderBook);

        // Run the partial fill test
        System.out.println("=============================================================================================");
        System.out.println("\nOrder book before Partial Fill Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testPartialFill(orderBook, matchingEngine);
        System.out.println("Test: Add two buy orders (100, qty: 10 and 99.8, qty: 20), then process a sell order (100, qty: 25).\n");
        System.out.println("Order book after Partial Fill Test:");
        System.out.println(orderBook);

        // Run the unmatched order test
        System.out.println("=============================================================================================");
        System.out.println("\nOrder book before Unmatched Order Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testUnmatchedOrder(orderBook, matchingEngine);
        System.out.println("Test: Process one buy order (99.95, qty: 15).\n");
        System.out.println("Order book after Unmatched Order Test:");
        System.out.println(orderBook);

    }
}
