package sample;
import java.io.*;
import java.util.ArrayList;


public abstract class Piece{
    private Loc loc;
    private int color;
    private ArrayList<Loc> moves;
    private boolean isCaptured = false;


    public ArrayList<Loc> getMoves(){
        return moves;
    }

    public boolean isCaptured(){
        return isCaptured;
    }

    public void setIsCaptured(boolean captured){
        isCaptured = captured;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean canThreaten(int color, int otherColor){
        if((color*otherColor) <= 0){
            return true;
        }
        else{
         return false;
        }
    }



    public void setLoc(int x, int y) {
        loc = new Loc(x, y);

    }
    public void setLoc(Loc loc){
        this.loc = loc;
    }

    public Loc getLoc() {
        return loc;
    }

    public void setMoves(ArrayList<Loc> moves) {
        this.moves = moves;
    }

    public abstract ArrayList<Loc> calculateMoves(Loc loc, int[][] board);
}