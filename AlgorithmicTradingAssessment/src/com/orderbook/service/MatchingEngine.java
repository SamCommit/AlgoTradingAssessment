package com.orderbook.service;

import com.orderbook.model.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class MatchingEngine{
	private final OrderBook orderbook;
	
	/**
	 * 
	 * MatchingEngine constructor initialises the MatchingEngine with an order book
	 * 
	 * @param orderbook The order book that the matching engine will process orders for
	 */
	public MatchingEngine(OrderBook orderbook) {
		this.orderbook = orderbook;
	}
	
	/**
	 * Process an incoming order and try to match it with orders in the opposite side of the order book
	 * 
	 * @param newOrder Is the new order to be processed
	 */
	public void processOrder(Order newOrder) {
        if (newOrder.getSide() == OrderSide.BUY) {
            matchOrder(newOrder, orderbook.getOrderMap(OrderSide.SELL));
        } else {
            matchOrder(newOrder, orderbook.getOrderMap(OrderSide.BUY));
        }
    }
	
	/**
	 * Matches the incoming order with orders on the opposite side of the order book
	 * 
	 * 1. Iterate through orders on the opposite side of the order book
	 * 2. Check if the price is acceptable for the incoming order
	 * 3. Get the list of orders at this price-range
	 * 4. Loop through all orders at this price range
	 * 5. Match the buy and sell orders by their minimum quantity
	 * 6. Remove any fully filled orders
	 * 7. Break out of the while loop if matching is no longer possible
	 * 
	 * @param newOrder The order to be matched
	 * @param oppositeOrders A TreeMap of the opposite side of the order book
	 */
	private void matchOrder(Order newOrder, TreeMap<Double, LinkedList<Order>> oppositeOrders) {
        Iterator<Map.Entry<Double, LinkedList<Order>>> iterator = oppositeOrders.entrySet().iterator(); // Instantiate the opposite side order book iterator 
        
        // Loop through the orders in the opposite side of the order book
        while (iterator.hasNext() && newOrder.getQuantity() > 0) {
            Map.Entry<Double, LinkedList<Order>> entry = iterator.next();
            double price = entry.getKey();

            // Check if the price is acceptable for the incoming order
            if ((newOrder.getSide() == OrderSide.BUY && newOrder.getPrice() >= price) ||
                (newOrder.getSide() == OrderSide.SELL && newOrder.getPrice() <= price)) {

            	// Get the list of orders at this price range
                LinkedList<Order> orderList = entry.getValue(); 
                // Create the iterator for this price range
                Iterator<Order> orderIterator = orderList.iterator(); 

                // Loop through all orders at this price range
                while (orderIterator.hasNext() && newOrder.getQuantity() > 0) {
                	// Get the order to be matched with
                    Order order = orderIterator.next(); 
                    // Get the minimum quantity
                    double matchedQuantity = Math.min(newOrder.getQuantity(), order.getQuantity()); // 

                    // Adjust the quantity of the respective orders
                    newOrder.setQuantity(newOrder.getQuantity() - matchedQuantity);
                    order.setQuantity(order.getQuantity() - matchedQuantity);

                    // Remove the order if it was totally matched
                    if (order.getQuantity() == 0) {
                        orderIterator.remove();
                    }
                }

                // If no orders remain at this price level, remove that price level from the map
                if (orderList.isEmpty()) {
                    iterator.remove();
                }

                // If the incoming order has been matched, break out of the while loop
                if (newOrder.getQuantity() == 0) {
                    break;
                }
            } else {
            	break; // If outside of the acceptable price range, break out of the while loop as the order cannot be matched
            }
        }

        // If there's any remaining quantity, add the new order back to the order book
        if (newOrder.getQuantity() > 0) {
            orderbook.addOrder(newOrder);
        }
    }
}





