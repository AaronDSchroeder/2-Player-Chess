package sample;
import java.util.*;
import java.lang.Math;
public class Pawn extends Piece{
    public boolean hasjumped = false;
    ArrayList<Loc> captureMoves = new ArrayList<>();
    private int lastPos;
    public Pawn(int x, int y, int[][] board, int color){
        super();
        this.setLoc(new Loc(x, y));
        this.setColor(color);
        lastPos = y;
    }
    public int getLastPos(){
        return lastPos;
    }

    public void setLastPos(int y) {
        this.lastPos = y;
    }

    @Override
    public void setLoc(int x, int y) {
        lastPos = this.getLoc().getY();
        super.setLoc(x, y);
    }

    public ArrayList<Loc> pawnMovements(Loc loc, int[][] board){
        ArrayList<Loc> pawnMoves = new ArrayList<>();
        if(this.getColor() < 0){
            if((loc.getY() == 1) && (board[2][loc.getX()] == 0) && board[3][loc.getX()] == 0){
                pawnMoves.add(new Loc(loc.getX(), 2));
                pawnMoves.add(new Loc(loc.getX(), 3));
            }
            else if((loc.getY() != 7) && board[(loc.getY() + 1)][loc.getX()] == 0){
                pawnMoves.add(new Loc(loc.getX(), loc.getY() + 1));
            }
        }
        else{
            if((loc.getY() == 6) && (board[5][loc.getX()] == 0) && board[4][loc.getX()] == 0){
                pawnMoves.add(new Loc(loc.getX(), 5));
                pawnMoves.add(new Loc(loc.getX(), 4));
            }
            else if((loc.getY() != 0) && board[(loc.getY() - 1)][loc.getX()] == 0){
                pawnMoves.add(new Loc(loc.getX(), loc.getY() - 1));
            }
        }
        return pawnMoves;
    }

    public boolean canCapture(int color, Loc otherLoc, int[][] board){

        if(color*board[otherLoc.getY()][otherLoc.getX()] < 0  && (Math.abs(this.getLoc().getX() - otherLoc.getX()) == 1 )){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean canEnPassantLeft(int color, int lastPieceMoved, Board board){
        //checks whether the white pawn can use en passant

        if(color > 0 && this.getLoc().getY() == 3){
            if(this.getLoc().getX() > 0){
                int leftSquareColor = board.board[3][this.getLoc().getX() - 1];
                if(leftSquareColor < 0 && board.getPiece(leftSquareColor, board.pieces) instanceof Pawn && leftSquareColor == lastPieceMoved){
                    Pawn otherPawn = (Pawn) board.getPiece(leftSquareColor, board.pieces);
                    boolean isLegal = false;
                    if(otherPawn.lastPos == 1){
                        Loc locCopy = this.getLoc();
                        this.setLoc(locCopy.getX() - 1, 2);
                        board.board[locCopy.getY()][locCopy.getX()] = 0;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = 0;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        otherPawn.setIsCaptured(true);
                        isLegal = !board.isInCheck(color, board.pieces, board.board);
                        otherPawn.setIsCaptured(false);
                        board.board[locCopy.getY()][locCopy.getX()] = color;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = leftSquareColor;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = 0;

                        this.setLoc(locCopy);
                        return isLegal;
                    }
                }
            }
        }
        else if(color < 0 && this.getLoc().getY() == 4){
            if(this.getLoc().getX() > 0){
                int leftSquareColor = board.board[4][this.getLoc().getX() - 1];
                if(leftSquareColor > 0 && board.getPiece(leftSquareColor, board.pieces) instanceof Pawn && leftSquareColor == lastPieceMoved){
                    Pawn otherPawn = (Pawn) board.getPiece(leftSquareColor, board.pieces);
                    boolean isLegal = false;
                    if(otherPawn.lastPos == 6){
                        Loc locCopy = this.getLoc();
                        this.setLoc(locCopy.getX() - 1, 5);
                        board.board[locCopy.getY()][locCopy.getX()] = 0;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = 0;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        otherPawn.setIsCaptured(true);
                        isLegal = !board.isInCheck(color, board.pieces, board.board);
                        otherPawn.setIsCaptured(false);
                        board.board[locCopy.getY()][locCopy.getX()] = color;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = leftSquareColor;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = 0;

                        this.setLoc(locCopy);
                        return isLegal;
                    }
                }
            }
        }
        return false;
    }

    public boolean canEnPassantRight(int color, int lastPieceMoved, Board board){
        //checks whether the white pawn can use en passant

        if(color > 0 && this.getLoc().getY() == 3){
            if(this.getLoc().getX() < 7){
                int leftSquareColor = board.board[3][this.getLoc().getX() + 1];
                if(leftSquareColor < 0 && board.getPiece(leftSquareColor, board.pieces) instanceof Pawn && leftSquareColor == lastPieceMoved){
                    Pawn otherPawn = (Pawn) board.getPiece(leftSquareColor, board.pieces);
                    boolean isLegal = false;
                    if(otherPawn.lastPos == 1){
                        Loc locCopy = this.getLoc();
                        this.setLoc(locCopy.getX() + 1, 2);
                        board.board[locCopy.getY()][locCopy.getX()] = 0;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = 0;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        otherPawn.setIsCaptured(true);
                        isLegal = !board.isInCheck(color, board.pieces, board.board);
                        otherPawn.setIsCaptured(false);
                        board.board[locCopy.getY()][locCopy.getX()] = color;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = leftSquareColor;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = 0;

                        this.setLoc(locCopy);
                        return isLegal;
                    }
                }
            }
        }
        else if(color < 0 && this.getLoc().getY() == 4){
            if(this.getLoc().getX() < 7){
                int leftSquareColor = board.board[4][this.getLoc().getX() + 1];
                if(leftSquareColor > 0 && board.getPiece(leftSquareColor, board.pieces) instanceof Pawn && leftSquareColor == lastPieceMoved){
                    Pawn otherPawn = (Pawn) board.getPiece(leftSquareColor, board.pieces);
                    boolean isLegal = false;
                    if(otherPawn.lastPos == 6){
                        Loc locCopy = this.getLoc();
                        this.setLoc(locCopy.getX() + 1, 5);
                        board.board[locCopy.getY()][locCopy.getX()] = 0;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = 0;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        otherPawn.setIsCaptured(true);
                        isLegal = !board.isInCheck(color, board.pieces, board.board);
                        otherPawn.setIsCaptured(false);
                        board.board[locCopy.getY()][locCopy.getX()] = color;
                        board.board[otherPawn.getLoc().getY()][otherPawn.getLoc().getX()] = leftSquareColor;
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = 0;

                        this.setLoc(locCopy);
                        return isLegal;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public ArrayList<Loc> calculateMoves(Loc loc, int[][] board) {
        ArrayList<Loc> moves = new ArrayList<>();
        if(this.getColor() < 0){
            if(loc.getY() + 1 < 8){
                if(loc.getX() - 1 >= 0  && this.canThreaten(this.getColor(), board[loc.getY() + 1][loc.getX()-1])){
                    moves.add(new Loc(loc.getX() - 1, loc.getY()+1));
                }
                if(loc.getX() + 1 < 8 && this.canThreaten(this.getColor(), board[loc.getY() + 1][loc.getX()+1])){
                    moves.add(new Loc(loc.getX() + 1, loc.getY()+1));
                }
            }

        }
        else{
            if(loc.getY() - 1 >=0){
                 if(loc.getX() - 1 >= 0  && this.canThreaten(this.getColor(), board[loc.getY() - 1][loc.getX()-1])){
                     moves.add(new Loc(loc.getX() - 1, loc.getY()-1));
                }
                if(loc.getX() + 1 < 8 && this.canThreaten(this.getColor(), board[loc.getY() - 1][loc.getX()+1])){
                    moves.add(new Loc(loc.getX() + 1, loc.getY()-1));
                }
            }
        }
        return moves;
    }
}