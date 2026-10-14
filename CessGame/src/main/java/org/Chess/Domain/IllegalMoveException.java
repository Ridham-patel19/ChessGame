package org.Chess.Domain;

public class IllegalMoveException extends RuntimeException{
    public IllegalMoveException(String msg){
        super(msg);
    }
}
