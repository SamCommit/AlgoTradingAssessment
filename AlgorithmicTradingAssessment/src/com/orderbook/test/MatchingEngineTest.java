package com.orderbook.test;

import com.orderbook.model.Order;
import com.orderbook.model.OrderSide;
import com.orderbook.service.MatchingEngine;
import com.orderbook.service.OrderBook;

/**
 * The MatchingEngineTest class has manual methods to validate the functionality of the matching engine.
 * Each method prints "PASSED" or "FAILED" depending to indicate outcome of the test.
 * JUnit would've been used for the unit tests but that would not have complied with the assessment instructions.
 */
public class MatchingEngineTest {

    /**
     * This function validates whether the MatchingEngine correctly processes a sell order.
     * 
     * 1. Add some buy orders to the order book.
     * 2. Process a sell order that should match with the buy orders.
     * 3. Assert that the order book is updated correctly.
     * 
     * @param orderBook The order book to test with.
     * @param matchingEngine The matching engine to use.
     */
    public static void testSellOrderMatching(OrderBook orderBook, MatchingEngine matchingEngine) {
        // Add some buy orders, one that will get filled and one that won't
        orderBook.addOrder(new Order(OrderSide.BUY, 99.9, 10));   // Buy order at 100 for 10 units
        orderBook.addOrder(new Order(OrderSide.BUY, 99.7, 6));    // Buy order at 99 for 20 units

        // Process a new sell order
        Order newSellOrder = new Order(OrderSide.SELL, 99.9, 8); // Sell order at 100 for 15 units
        matchingEngine.processOrder(newSellOrder);

        // Check the order book state after matching
        if (orderBook.getOrders(OrderSide.BUY).get(0).getQuantity() == 2) {
        	
        	System.out.println("\n===================================");
        	System.out.println("1. Sell Order Matching Test PASSED");
        	System.out.println("===================================\n");
        } else {
        	System.out.println("\n===================================");
        	System.out.println("1. Sell Order Matching Test FAILED");
        	System.out.println("===================================\n");
        }
    }

    /**
     * This function validates whether the MatchingEngine correctly processes a buy order.
     * 
     * 1. Add some sell orders to the order book.
     * 2. Process a buy order that should match with the sell orders.
     * 3. Assert that the order book is updated correctly.
     * 
     * @param orderBook The order book to test with.
     * @param matchingEngine The matching engine to use.
     */
    public static void testBuyOrderMatching(OrderBook orderBook, MatchingEngine matchingEngine) {
        // Add some initial sell orders
        orderBook.addOrder(new Order(OrderSide.SELL, 102.0, 5));   // Sell order at 105 for 5 units
        orderBook.addOrder(new Order(OrderSide.SELL, 101.0, 10));  // Sell order at 104 for 10 units

        // Process a new buy order
        Order newBuyOrder = new Order(OrderSide.BUY, 102.0, 8); // Buy order at 105 for 8 units
        matchingEngine.processOrder(newBuyOrder);

        // Check the order book state after matching
        if (orderBook.getOrders(OrderSide.SELL).get(0).getQuantity() == 2) {
            System.out.println("\n===================================");
            System.out.println("2. Buy Order Matching Test PASSED");
            System.out.println("===================================\n");
        } else {
            System.out.println("\n===================================");
            System.out.println("2. Buy Order Matching Test FAILED");
            System.out.println("===================================\n");
        }
    }

    /**
     * This function validates whether the MatchingEngine correctly handles a partial fill.
     * 
     * 1. Add a few orders to the order book.
     * 2. Process a new order that partially matches existing orders.
     * 3. Assert that the remaining quantity is added back to the order book.
     * 
     * @param orderBook The order book to test with.
     * @param matchingEngine The matching engine to use.
     */
    public static void testPartialFill(OrderBook orderBook, MatchingEngine matchingEngine) {
        // Add two buy orders, one that will get filled (100) and one that won't (99.8)
        orderBook.addOrder(new Order(OrderSide.BUY, 100.0, 10));   // Buy order at 100 for 10 units
        orderBook.addOrder(new Order(OrderSide.BUY, 99.8, 20));    // Buy order at 99.8 for 20 units

        // Process a new sell order that can only partially fill the first order
        Order newSellOrder = new Order(OrderSide.SELL, 100.0, 25); // Sell order at 100 for 25 units
        matchingEngine.processOrder(newSellOrder);

        // Check the order book state after matching
        if (orderBook.getOrders(OrderSide.SELL).get(0).getQuantity() == 15) {
            System.out.println("\n=================================");
            System.out.println("3. Partial Fill Test PASSED");
            System.out.println("=================================\n");
        } else {
            System.out.println("\n=================================");
            System.out.println("3. Partial Fill Test FAILED");
            System.out.println("=============================\n");
        }
    }

    /**
     * This function checks whether the MatchingEngine correctly adds an unmatched order to the order book.
     * 
     * 1. Add a buy order that has no match on the sell order side.
     * 2. Process the order.
     * 3. Assert that the full order is added to the order book.
     * 
     * @param orderBook The order book to test with.
     * @param matchingEngine The matching engine to use.
     */
    public static void testUnmatchedOrder(OrderBook orderBook, MatchingEngine matchingEngine) {
        // Process a buy order with no sell orders to match it
        Order newBuyOrder = new Order(OrderSide.BUY, 99.95, 15); // Buy order at 102 for 15 units
        matchingEngine.processOrder(newBuyOrder);
        
        // Check if the order was added to the order book
        if (orderBook.getOrders(OrderSide.BUY).get(0).getQuantity() == 15) {
            System.out.println("\n=================================");
            System.out.println("4. Unmatched Order Test PASSED");
            System.out.println("=================================\n");
        } else {
            System.out.println("\n=================================");
            System.out.println("4. Unmatched Order Test FAILED");
            System.out.println("=================================\n");
        }
    }

    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        MatchingEngine matchingEngine = new MatchingEngine(orderBook);

        // Run MatchingEngine tests and output the results
        System.out.println("\n===============================");
        System.out.println("Running MatchingEngine Tests...");
        System.out.println("===============================\n");

        // Run the sell order matching test
        System.out.println("Order book before Sell Order Matching Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testSellOrderMatching(orderBook, matchingEngine);
        System.out.println("Order book after Sell Order Matching Test:");
        System.out.println(orderBook);

        // Run the buy order matching test
        System.out.println("\nOrder book before Buy Order Matching Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testBuyOrderMatching(orderBook, matchingEngine);
        System.out.println("Order book after Buy Order Matching Test:");
        System.out.println(orderBook);

        // Run the partial fill test
        System.out.println("\nOrder book before Partial Fill Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testPartialFill(orderBook, matchingEngine);
        System.out.println("Order book after Partial Fill Test:");
        System.out.println(orderBook);

        // Run the unmatched order test
        System.out.println("\nOrder book before Unmatched Order Test:");
        System.out.println(orderBook);
        MatchingEngineTest.testUnmatchedOrder(orderBook, matchingEngine);
        System.out.println("Order book after Unmatched Order Test:");
        System.out.println(orderBook);
    }
}
