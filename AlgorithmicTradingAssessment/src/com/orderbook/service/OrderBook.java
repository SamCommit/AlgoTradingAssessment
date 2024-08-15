package com.orderbook.service;

import com.orderbook.model.*;
//import java.util.ArrayList;
import java.util.Collections;
//import java.util.Iterator;
import java.util.LinkedList;
//import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The OrderBook class manages and maintains the book of both buy and sell orders
 * Orders are stored in a TreeMap where the key is the price and the value is a LinkedList of orders at that price.
 * Buy orders are sorted by prices from highest to lowest, and then by timestamp for priority.
 * Sell orders are sorted by prices from lowest to highest, and then by timestamp for priority.
 */
public class OrderBook {
	// TreeMap that holds the buy orders, sorted by price from highest to lowest. 
	private final Map<Double, LinkedList<Order>> buyOrders = new TreeMap<>(Collections.reverseOrder());
	
}