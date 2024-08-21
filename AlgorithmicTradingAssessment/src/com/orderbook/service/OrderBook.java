package com.orderbook.service;

import com.orderbook.model.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The OrderBook class manages and maintains the book of both buy and sell orders.
 * Orders are stored in a TreeMap where the key is the price and the value is a LinkedList of orders at that price.
 * Buy orders are sorted by prices from highest to lowest, and then by timestamp for priority.
 * Sell orders are sorted by prices from lowest to highest, and then by timestamp for priority.
 */
public class OrderBook {
	// TreeMap that stores the buy orders, sorted by price from highest to lowest. 
	private final TreeMap<Double, LinkedList<Order>> buyOrders = new TreeMap<>(Collections.reverseOrder());
	
	// TreeMap that stores the sell orders, sorted by price from lowest to highest. 
	private final TreeMap<Double, LinkedList<Order>> sellOrders = new TreeMap<>();

	/**
	 * Add an order to the order book:
	 * 1. Check if the order is a buy or sell order and add it to that TreeMap
	 * 2. Check if a list of orders exists at this price or not
	 * 3. Add the order to the list or make a new list at that price.
	 * 
	 * This addOrder method is O(log P) complexity due to the computeIfAbsent search, P being the number of price levels
	 * 
	 * @param order
	 */
	public void addOrder(Order order) { // Adding an order is 
		// Determine if the order is a buy or sell order
		TreeMap<Double, LinkedList<Order>> orders = order.getSide() == OrderSide.BUY ? buyOrders : sellOrders;
		
		// Get the list of orders (if any) at the price level of the order to be added
		orders.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
	}
	
	/**
	 * Modify an order in the order book:
	 * 1. Delete the existing order
	 * 2. Create a new order with the new quantity (this resets the priority)
	 * 3. Re-add the order back to the order book with a lowest priority due to modifying
	 * 
	 * This modifyOrder function is O(n) complexity as it must complete a delete in it functionality
	 * 
	 * @param orderId
	 * @param newQuantity
	 * @param price
	 */
	public void modifyOrder(String orderId, double newQuantity) {
		Order order = deleteOrder(orderId);
		if (order != null) {
			order.setQuantity(newQuantity); //Set the new quantity of the order and reset the priority timestamp (See Order Class)
			addOrder(order);
		}
	}
	
	/**
	 * Delete an order from the order book:
	 * 1. Loop through buy and sell order collections
	 * 2. Loop through each price level
	 * 3. Use the iterator to safely remove the element (avoiding ConcurrentModificationException)
	 * 4. If the price level has no more orders, remove that price level from the map
	 * 5. Return the order if it was successfully deleted or null if it wasn't
	 * 
	 * This deleteOrder method is O(n) complexity because each orderId needs to be checked as it's a UUID
	 * 
	 * @param orderId
	 * @return The deleted order if success or null if failed
	 */
	public Order deleteOrder(String orderId) {
	    for (TreeMap<Double, LinkedList<Order>> orders : Arrays.asList(buyOrders, sellOrders)) {
	        for (Map.Entry<Double, LinkedList<Order>> priceLevel : orders.entrySet()) {
	            LinkedList<Order> orderList = priceLevel.getValue();
	            for (Iterator<Order> orderIterator = orderList.iterator(); orderIterator.hasNext(); ) {
	                Order order = orderIterator.next();
	                if (order.getId().equals(orderId)) {
	                    orderIterator.remove(); // Remove the order from the list
	                    // If the list is now empty, remove the price level
	                    if (orderList.isEmpty()) {
	                        orders.remove(priceLevel.getKey());
	                    }
	                    return order; // Return the deleted order
	                }
	            }
	        }
	    }
	    return null; // If no order with the specified order Id was found, return null.
	}
	
	/**
	 * Get the orders for the order book:
	 * 1. Select the buy or sell side of the order book.
	 * 2. Returns a flattened list of the orders from the respective side.
	 * 
	 * This getOrders method is O(n) complexity because it returns all orders in the list
	 * 
	 * @param side
	 * @return A List of the order book for the specified side
	 */
	public List<Order> getOrders(OrderSide side){
		TreeMap<Double, LinkedList<Order>> orders = side == OrderSide.BUY ? buyOrders : sellOrders;
		
		List<Order> result = new ArrayList<>(); // Instantiate an ArrayList to store all the orders
		for (LinkedList<Order> orderList : orders.values()) {
			result.addAll(orderList); // Add all orders to the list
		}
		return result; // Return the list of orders
	}
	
	/**
	 * Return the TreeMap of the orders for either side of the book
	 * 
	 * This method is used in the matching engine for efficient order matching
	 * 
	 * @param side (BUY/SELL)
	 * @return A TreeMap of the order book for the specified side
	 */
	public TreeMap<Double, LinkedList<Order>> getOrderMap(OrderSide side) {
        return side == OrderSide.BUY ? (TreeMap<Double, LinkedList<Order>>) buyOrders : (TreeMap<Double, LinkedList<Order>>) sellOrders;
    }

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("OrderBook:\n");
	    
	    sb.append("Buy Orders (Priority highest to lowest):\n");
	    if (buyOrders.isEmpty()) {
	        sb.append("  None\n");
	    } else {
	        for (Map.Entry<Double, LinkedList<Order>> entry : buyOrders.entrySet()) {
	            sb.append(String.format("  Price: %.2f\n", entry.getKey()));
	            for (Order order : entry.getValue()) {
	                sb.append("    ").append(order).append("\n");
	            }
	        }
	    }
	    
	    sb.append("Sell Orders (Priority highest to lowest):\n");
	    if (sellOrders.isEmpty()) {
	        sb.append("  None\n");
	    } else {
	        for (Map.Entry<Double, LinkedList<Order>> entry : sellOrders.entrySet()) {
	            sb.append(String.format("  Price: %.2f\n", entry.getKey()));
	            for (Order order : entry.getValue()) {
	                sb.append("    ").append(order).append("\n");
	            }
	        }
	    }
	    
	    return sb.toString();
	}
}	
	
	

	
	
	
	
	
	
	
	