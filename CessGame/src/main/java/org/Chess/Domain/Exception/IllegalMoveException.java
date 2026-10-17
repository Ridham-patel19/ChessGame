package org.Chess.Domain.Exception;

public class IllegalMoveException extends RuntimeException{
    public IllegalMoveException(String msg){
        super(msg);
    }
}
