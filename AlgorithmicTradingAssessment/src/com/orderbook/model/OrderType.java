package com.orderbook.model;

/**
 * Enum thats represents the type of an order in the order book.
 * Limit specifies a price at which the order should be executed.
 * Market should be executed immediately at the best possible price.
 */
public enum OrderType {
    LIMIT, // Limit order, used in this assignment
    MARKET; // Market order, used For future expansion on this assignment
}
