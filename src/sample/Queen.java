package sample;

import java.util.*;

public class Queen extends Piece{
    public Queen(int x, int y, int[][] board, int color) {
        super();
        this.setLoc(x, y);
        this.setColor(color);
    }


    @Override
    public ArrayList<Loc> calculateMoves(Loc loc, int[][] board) {
        ArrayList<Loc> moves = new ArrayList<>();
        int i = loc.getY();
        int j = loc.getX();
        while(i < board.length - 1 && j < board.length - 1 && this.canThreaten(this.getColor(), board[i+1][j+1])){
            moves.add(new Loc(j+1,i + 1));
            i++;
            j++;
            if(board[i][j]*this.getColor() < 0){
                break;
            }
        }
        i = loc.getY();
        j = loc.getX();
        while(i < board.length -1 && j >= 1 && this.canThreaten(this.getColor(), board[i + 1][j - 1])){
            moves.add(new Loc(j - 1, i + 1));
            i++;
            j--;
            if(board[i][j]*this.getColor() < 0){
                break;
            }
        }
        i = loc.getY();
        j = loc.getX();
        while(i >= 1 && j >= 1 && this.canThreaten(this.getColor(), board[i - 1][j - 1])){
            moves.add(new Loc(j - 1, i - 1));
            i--;
            j--;
            if(board[i][j]*this.getColor() < 0){
                break;
            }
        }
        i = loc.getY();
        j = loc.getX();
        while(i >= 1  && j < board.length - 1 && this.canThreaten(this.getColor(), board[i - 1][j + 1])){
            moves.add(new Loc(j + 1, i - 1));
            i--;
            j++;
            if(board[i][j]*this.getColor() < 0){
                break;
            }
        }
        i = loc.getY();
        j = loc.getX();
        while(i < board.length - 1 && canThreaten(this.getColor(), board[i + 1][j])){
            moves.add(new Loc(j, i + 1));
            i++;
            if(this.getColor()*board[i][j] < 0){
                break;
            }
        }
        i = loc.getY();
        while(j < board.length - 1 && this.canThreaten(this.getColor(), board[i][j+1])){
            moves.add(new Loc(j + 1, i));
            j++;
            if(this.getColor()*board[i][j] < 0){
                break;
            }
        }
        j = loc.getX();
        while(i >= 1 && canThreaten(this.getColor(), board[i - 1][j])){
            moves.add(new Loc(j, i - 1));
            i--;
            if(this.getColor()*board[i][j] < 0){
                break;
            }
        }
        i = loc.getY();
        j = loc.getX();
        while(j >= 1 && canThreaten(this.getColor(), board[i][j - 1])){
            moves.add(new Loc(j - 1, i));
            j--;
            if(this.getColor()*board[i][j] < 0){
                break;
            }
        }
        return moves;

    }
}