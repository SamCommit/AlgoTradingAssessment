package com.orderbook.model;

import java.util.UUID;

/**
 * The Order class is used to describe a single order in the order book 
 * Each order has a unique Id (UUID), a price, a quantity, and a side (buy or sell), a type (limit or market)
 * A timestamp is also be added to the Order class to track which orders are of highest priority 
 */
public class Order {
	private final String id;
	private final OrderSide side;
	private final double price; // This cannot be updated if the order is modified
	private double quantity; // This can be updated if the order is modified
	private long timestamp; // This will be reset if either the price or quantity is modified to reset priority
	
	/**
	 * 
	 * @param side The side of the order
	 * @param price The price of the order
	 * @param quantity The quantity of the order
	 */
	public Order(OrderSide side, double price, int quantity) {
		this.id = UUID.randomUUID().toString();
		this.side = side;
		this.price = price;
		this.quantity = quantity;
		this.timestamp = System.nanoTime();
	}
	
	/**
	 * Getters and setter for the respective fields above.
	 * Modifying quantity is allowed, and the timestamp will be updated.
	 * This updated timestamp resets the orders' priority (to lowest) and enforces order book integrity.
	 */
	public String getId() {
		return id;
	}
	
	public OrderSide getSide() {
		return side;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getQuantity() {
		return quantity;
	}
	
    public void setQuantity(double quantity) {
        this.quantity = quantity; // Allow modification of the order's quantity
        this.timestamp = System.nanoTime(); // Resets the timestamp to reset the order priority
    }
	
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
	    return String.format(
	        "Order{id='%s', side=%s, price=%.2f, quantity=%.2f, timestamp=%d}",
	        id, side, price, quantity, timestamp
	    );
	}
}


