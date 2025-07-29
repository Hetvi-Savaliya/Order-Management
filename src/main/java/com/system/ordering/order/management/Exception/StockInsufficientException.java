package com.system.ordering.order.management.Exception;

public class StockInsufficientException extends RuntimeException{
    public StockInsufficientException(String message){
        super(message);
    }
}
