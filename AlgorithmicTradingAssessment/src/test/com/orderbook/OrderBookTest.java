package test.com.orderbook;

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
	 * @param orderBook
	 */
    public static void testAddOrder(OrderBook orderBook) {
        // Verify that orders have been added correctly
        if (orderBook.getOrders(OrderSide.BUY).size() == 4 && orderBook.getOrders(OrderSide.SELL).size() == 4) {
            System.out.println("Add orders test PASSED");
        } else {
            System.out.println("Add orders test FAILED");
        }
    }
    
	/**
	 * This function validates whether the order has been correctly modified or not.
	 * 
	 * @param orderBook
	 * @param order
	 */
    public static void testModifyOrder(OrderBook orderBook, Order order) {
        // Verify that order's quantity has been updated
        if (orderBook.getOrders(OrderSide.BUY).stream().anyMatch(o -> o.getId().equals(order.getId()) && o.getQuantity() == 15)) {
            System.out.println("Modify order test PASSED");
        } else {
            System.out.println("Modify order test FAILED");
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
            System.out.println("Delete order test PASSED");
        } else {
            System.out.println("Delete order test FAILED");
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
            System.out.println("Order is already last in priority. Priority modification test is inconclusive.");
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
            System.out.println("Order priority test PASSED: Order is last in the list at its price level.");
        } else {
            System.out.println("Order priority test FAILED: Order isn't last in the list at its price level.");
        }
    }

}
