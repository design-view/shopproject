package org.green.shop.exception;

public class OutOfStockException extends RuntimeException{
    //생성될때 에러메세지 문자로 전달
    public OutOfStockException(String message){
        super(message);
    }
}
