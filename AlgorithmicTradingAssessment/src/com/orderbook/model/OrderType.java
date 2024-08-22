package com.orderbook.model;

/**
 * Enum thats represents the type of an order in the order book.
 * The limit order specifies a price at which the order should be executed.
 * A market order should be executed immediately at the best possible price.
 */
public enum OrderType {
    LIMIT, // Limit order, used in this assignment.
    MARKET, // Market order, used in potential future expansion.
	STOP; // Stop order, used in potential future expansion.
}
