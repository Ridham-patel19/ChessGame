package org.Chess.Domain;

//its a fast way to create any small model class her this record will create Sqaure class and constructer
//with field row and column  now we can use it as a class and can create obj out of it
public record Square( int row , int column){

    //it act as a compact constructer while we create obj it will validate input
    public Square{
        if(row < 0 &&  row > 7){
            throw new IllegalArgumentException("Please enter valid input");
        }
        if(column < 0 &&  column > 7){
            throw new IllegalArgumentException("Please enter valid input");
        }
    }

    public static Square fromNotation(String notation) {

        if (notation == null || notation.length() != 2)
        { throw new IllegalArgumentException("Invalid square: " + notation);
        }

        char file = notation.charAt(0);
        char rank = notation.charAt(1);
        int column = file - 'a';
        int row = rank - '1';
        return new Square(row, column);
    }
    public String toNotation()
    {
        char file = (char) ('a' + column);
        char rank = (char) ('1' + row);
        return "" + file + rank;
    }
}
