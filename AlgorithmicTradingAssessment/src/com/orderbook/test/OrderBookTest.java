package com.orderbook.test;

import java.util.LinkedList;
import java.util.stream.Collectors;
import com.orderbook.model.*;
import com.orderbook.service.*;

/**
 * The OrderBookTest class contains manual methods to validate the functionality of the order book.
 * Each method prints "PASSED" or "FAILED" depending on the outcome of the test.
 * JUnit would've been used for the unit tests but that would not have complied with the assessment instructions.
 */
public class OrderBookTest {

	/**
	 * This function validates whether the order has been added correctly or not.
	 * 
	 * 1. Get the size of the buy and sell order queues
	 * 2. Add a buy order
	 * 3. Add a sell order
	 * 4. Check that the buy and sell order sizes are what they were + 1
	 * 
	 * @param orderBook
	 */
    public static void testAddOrder(OrderBook orderBook) {
        // Get initial sizes of the buy and sell order queues
        int initialBuySize = orderBook.getOrders(OrderSide.BUY).size();
        int initialSellSize = orderBook.getOrders(OrderSide.SELL).size();

        // Add a new buy order and sell order
        Order newBuyOrder = new Order(OrderSide.BUY, 99.1, 18);
        Order newSellOrder = new Order(OrderSide.SELL, 105.0, 5);
        orderBook.addOrder(newBuyOrder);
        orderBook.addOrder(newSellOrder);
        
        // Get the sizes after adding an order
        int finalBuySize = orderBook.getOrders(OrderSide.BUY).size();
        int finalSellSize = orderBook.getOrders(OrderSide.SELL).size();
        
        // Verify that orders have been added correctly
        if (finalBuySize == initialBuySize + 1 && finalSellSize == initialSellSize + 1) {
            System.out.println("\n=======================");
            System.out.println("1. Add Orders Test PASSED");
            System.out.println("=======================\n");
        } else {
        	System.out.println("\n=======================");
            System.out.println("1. Add Orders Test FAILED");
            System.out.println("=======================\n");
        }
    }
    
	/**
	 * This function validates whether the order has been correctly modified or not.
	 * 
	 * 1. Check the orders initial quantity
	 * 2. Modify the quantity
	 * 3. Assert that the quantity has been updated
	 * 
	 * @param orderBook
	 * @param order
	 */
    public static void testModifyOrder(OrderBook orderBook, Order order) {
        // Check the initial quantity of the order
        double initialQuantity = order.getQuantity();

        // Modify the quantity
        double newQuantity = initialQuantity + 10;  // Example of increasing the quantity by 10
        orderBook.modifyOrder(order.getId(), newQuantity);

        // Assert that the order's quantity has been updated
        if (orderBook.getOrders(order.getSide()).stream()
                .anyMatch(o -> o.getId().equals(order.getId()) && o.getQuantity() == newQuantity)) {
            System.out.println("\n=========================");
            System.out.println("2. Modify order test PASSED");
            System.out.println("=========================\n");
        } else {
            System.out.println("\n=========================");
            System.out.println("2. Modify order test FAILED");
            System.out.println("=========================\n");
        }
    }
    
    /**
     * This function validates whether the order has been correctly deleted or not.
     * 
     * @param orderBook
     * @param order
     */
    public static void testDeleteOrder(OrderBook orderBook, Order order) {
        // Verify that the order has been deleted
        if (orderBook.getOrders(OrderSide.SELL).stream().noneMatch(o -> o.getId().equals(order.getId()))) {
            System.out.println("\n=========================");
            System.out.println("3. Delete order test PASSED");
            System.out.println("=========================\n");
        } else {
            System.out.println("\n=========================");
            System.out.println("3. Delete order test FAILED");
            System.out.println("=========================\n");
        }
    }

    /**
     * This function validates whether the order has the correct priority or not:
     * 
     * 1. Get the price level of the order before modification
     * 2. Get a list of orders at that price level
     * 3. Ensure the priority isn't already last.
     * 4. Modify the orders quantity to trigger a priority reset
     * 5. Retrieve the updated list of orders at that price level
     * 6. Assert that the modified order is now lowest priority 
     * 
     * @param orderBook
     * @param order
     */
    public static void testOrderPriority(OrderBook orderBook, Order order) {
        // Get the price level of the order
        double priceLevel = order.getPrice();

        // Retrieve the list of orders at that price level
        LinkedList<Order> ordersAtPriceLevel = orderBook.getOrders(OrderSide.BUY).stream()
                .filter(o -> o.getPrice() == priceLevel)
                .collect(Collectors.toCollection(LinkedList::new));

        // Check the is not already last in the list
        if (ordersAtPriceLevel.getLast().getId().equals(order.getId())) {
            System.out.println("\n===================================================================================");
            System.out.println("4. Order priority modification test inconclusive. Order is already last in priority. ");
            System.out.println("===================================================================================\n");
            return;
        }

        // Modify the order's quantity which will trigger a priority reset (new timestamp)
        orderBook.modifyOrder(order.getId(), order.getQuantity() + 10);

        // RGet the updated list of orders at the price level
        ordersAtPriceLevel = orderBook.getOrders(OrderSide.BUY).stream()
                .filter(o -> o.getPrice() == priceLevel)
                .collect(Collectors.toCollection(LinkedList::new));

        // Assert that the modified order is now the the lowest priority at that price level
        if (ordersAtPriceLevel.getLast().getId().equals(order.getId())) {
            System.out.println("\n==========================================================================");
            System.out.println("4. Order priority test PASSED: Order is last in the list at its price level.");
            System.out.println("==========================================================================\n");
        } else {
            System.out.println("\n=============================================================================");
            System.out.println("4. Order priority test FAILED: Order isn't last in the list at its price level.");
            System.out.println("=============================================================================\n");
        }
    }
}
