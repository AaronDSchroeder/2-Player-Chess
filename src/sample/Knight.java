package sample;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
public class Knight extends Piece{
    public Knight(int x, int y, int[][] board, int color) {
        super();
        this.setLoc(x, y);
        this.setColor(color);
    }

    public ArrayList<Loc> calculateMoves(Loc loc, int[][] board) {
        ArrayList<Loc> moves = new ArrayList<>();
        int i = loc.getY();
        int j = loc.getX();
        if(j + 1 < board.length && i + 2 < board.length && this.canThreaten(this.getColor(), board[i+2][j+1])){
            moves.add(new Loc(j + 1, i + 2));
        }
        if(j + 2 < board.length && i + 1 < board.length && this.canThreaten(this.getColor(), board[i+1][j+2]) ){
            moves.add(new Loc(j + 2, i + 1));
        }
        if(j - 2 >= 0 && i + 1 < board.length && this.canThreaten(this.getColor(), board[i+1][j-2])){
            moves.add(new Loc(j - 2, i + 1));
        }
        if(j + 2 < board.length && i - 1 >= 0 && this.canThreaten(this.getColor(), board[i-1][j+2]) ){
            moves.add(new Loc(j + 2, i - 1));
        }
        if(j - 2 >= 0 && i - 1 >= 0 && this.canThreaten(this.getColor(), board[i-1][j-2]) ){
            moves.add(new Loc(j - 2, i - 1));
        }
        if(j - 1 >= 0 && i - 2 >= 0 && this.canThreaten(this.getColor(), board[i-2][j-1]) ){
            moves.add(new Loc(j - 1, i - 2));
        }
        if(j + 1 < board.length && i - 2 >= 0 && this.canThreaten(this.getColor(), board[i-2][j+1]) ){
            moves.add(new Loc(j + 1, i - 2));
        }
        if(j  - 1 >= 0 && i + 2 < board.length && this.canThreaten(this.getColor(), board[i+2][j-1]) ){
            moves.add(new Loc(j - 1, i + 2));
        }
        return moves;
    }
}