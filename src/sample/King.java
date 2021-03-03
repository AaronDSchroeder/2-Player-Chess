package sample;

import java.util.ArrayList;

public class King extends Piece {
    private boolean hasMoved;
    public King(int x, int y, int[][] board, int color) {
        super();
        this.setLoc(new Loc(x, y));
        this.setColor(color);
        this.hasMoved = false;


    }

    @Override
    public void setLoc(int x, int y) {
        this.hasMoved = true;
        super.setLoc(x, y);
    }

    public boolean canCastle(int color, Board board){
        if(color != 0) {
            if(board.isInCheck(color, board.pieces, board.board)){
                return false;
            }
            if ((!this.hasMoved) && (board.board[this.getLoc().getY()][7] != 0)){
                for(int i = this.getLoc().getX() + 1; i < 7; i++){
                    if(board.board[this.getLoc().getY()][i] != 0){
                        return false;
                    }
                }

                if(board.getPiece(board.board[this.getLoc().getY()][7], board.pieces) instanceof Rook){
                    Rook rook = (Rook)board.getPiece(board.board[this.getLoc().getY()][7], board.pieces);
                    if(!rook.getHasMoved()){
                        Loc locKing = this.getLoc();
                        Loc locRook = rook.getLoc();
                        this.setLoc(new Loc(this.getLoc().getX() + 2, this.getLoc().getY()));
                        rook.setLoc(new Loc(rook.getLoc().getX() - 2, rook.getLoc().getY()));
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        board.board[rook.getLoc().getY()][rook.getLoc().getX()] = rook.getColor();
                        board.board[locKing.getY()][locKing.getX()] = 0;
                        board.board[locRook.getY()][locRook.getX()] = 0;
                        boolean isLegal = false;
                        isLegal = !board.isInCheck(color, board.pieces, board.board);
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = 0;
                        board.board[rook.getLoc().getY()][rook.getLoc().getX()] = 0;
                        this.setLoc(locKing);
                        rook.setLoc(locRook);
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        board.board[rook.getLoc().getY()][rook.getLoc().getX()] = rook.getColor();
                        return isLegal;
                    }
                }
            }
        }
        return false;
    }

    public boolean canQueenSideCastle(int color, Board board){
        if(color != 0) {
            if(board.isInCheck(color, board.pieces, board.board)){
                return false;
            }
            if ((!this.hasMoved) && (board.board[this.getLoc().getY()][0] != 0)){
                for(int i = this.getLoc().getX()  - 1; i > 0; i--){
                    if(board.board[this.getLoc().getY()][i] != 0){
                        return false;
                    }
                }

                if(board.getPiece(board.board[this.getLoc().getY()][0], board.pieces) instanceof Rook){
                    Rook rook = (Rook)board.getPiece(board.board[this.getLoc().getY()][0], board.pieces);
                    if(!rook.getHasMoved()){
                        Loc locKing = this.getLoc();
                        Loc locRook = rook.getLoc();
                        this.setLoc(new Loc(this.getLoc().getX() - 2, this.getLoc().getY()));
                        rook.setLoc(new Loc(rook.getLoc().getX() + 3, rook.getLoc().getY()));
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        board.board[rook.getLoc().getY()][rook.getLoc().getX()] = rook.getColor();
                        board.board[locKing.getY()][locKing.getX()] = 0;
                        board.board[locRook.getY()][locRook.getX()] = 0;
                        boolean isLegal = false;
                        isLegal = !board.isInCheck(color, board.pieces, board.board);
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = 0;
                        board.board[rook.getLoc().getY()][rook.getLoc().getX()] = 0;
                        this.setLoc(locKing);
                        rook.setLoc(locRook);
                        board.board[this.getLoc().getY()][this.getLoc().getX()] = color;
                        board.board[rook.getLoc().getY()][rook.getLoc().getX()] = rook.getColor();
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
        int i = loc.getY();
        int j = loc.getX();
        if(j + 1 < board.length && this.canThreaten(this.getColor(), board[i][j+1])){
            moves.add(new Loc(j + 1, i));
        }
        if( i + 1 < board.length && this.canThreaten(this.getColor(), board[i+1][j]) ){
            moves.add(new Loc(j, i + 1));
        }
        if(j - 1 >= 0 && this.canThreaten(this.getColor(), board[i][j-1])){
            moves.add(new Loc(j - 1, i));
        }
        if(i - 1 >= 0 && this.canThreaten(this.getColor(), board[i-1][j]) ){
            moves.add(new Loc(j, i - 1));
        }
        if(j - 1 >= 0 && i - 1 >= 0 && this.canThreaten(this.getColor(), board[i-1][j-1]) ){
            moves.add(new Loc(j - 1, i - 1));
        }
        if(j + 1 < board.length && i - 1 >= 0 && this.canThreaten(this.getColor(), board[i-1][j+1]) ){
            moves.add(new Loc(j + 1, i - 1));
        }
        if(j - 1 >= 0 && i + 1 < board.length && this.canThreaten(this.getColor(), board[i+1][j-1]) ){
            moves.add(new Loc(j - 1, i + 1));
        }
        if(j +1 < board.length && i + 1 < board.length && this.canThreaten(this.getColor(), board[i+1][j+1]) ){
            moves.add(new Loc(j + 1, i + 1));
        }
        return moves;
    }
}

