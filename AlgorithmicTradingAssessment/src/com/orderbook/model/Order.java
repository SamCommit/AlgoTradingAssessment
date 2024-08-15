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
	private final double price;
	private int quantity;
	private final long timestamp;
	
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
	 * Getters for the respective fields above
	 * Because a TreeMap data structure is being used, no setters will be necessary
	 * This is because when an order is to be modified, the order will be removed from the
	 * order book and re-added with an updated timestamp to enforce integrity of the order book priority
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
	
	public int getQuantity() {
		return quantity;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", side=" + side +
                ", price=" + price +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}


