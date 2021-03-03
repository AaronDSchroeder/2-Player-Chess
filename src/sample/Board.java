package sample;
import java.sql.SQLOutput;
import java.util.*;
public class Board{
    int[][] board = new int[8][8];
    int[][] moveDisplay = new int[8][8];
    ArrayList<Piece> pieces = new ArrayList<>();

    public Board(){

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = 0;
                moveDisplay[i][j] = 0;
            }
        }
        // white pieces added, indices 0 through 15
        pieces.add(new King(4, 7, board, 1));
        pieces.add(new Queen(3, 7, board, 2));
        pieces.add(new Rook(0, 7, board, 3));
        pieces.add(new Rook(7, 7, board, 4));
        pieces.add(new Knight(1, 7, board, 5));
        pieces.add(new Knight(6, 7, board, 6));
        pieces.add(new Bishop(2, 7, board, 7));
        pieces.add(new Bishop(5, 7, board, 8));
        for(int i = 0; i < 8; i++){
            pieces.add(new Pawn(i, 6, board, i + 9));
        }

        // black pieces, indices 17 though 33
        pieces.add(new King(4, 0, board, -1));
        pieces.add(new Queen(3, 0, board, -2));
        pieces.add(new Rook(0, 0, board, -3));
        pieces.add(new Rook(7, 0, board, -4));
        pieces.add(new Knight(1, 0, board, -5));
        pieces.add(new Knight(6, 0, board, -6));
        pieces.add(new Bishop(2, 0, board, -7));
        pieces.add(new Bishop(5, 0, board, -8));
        for(int i = 0; i < 8; i++){
            pieces.add(new Pawn(i, 1, board, -i - 9));
        }

        int num = 0;
        for(int j = -3; j >= -7; j = j - 2){
            board[0][num] = j;
            num++;
        }
        num = 5;
        board[0][3] = -2;
        board[0][4] = -1;
        for(int j = -8; j <= -4; j = j + 2){
            board[0][num] = j;
            num++;
        }
        num = 9;
        for(int i = 0; i < 8; i++){
            board[1][i] = -num;
            num++;
        }
        num = 0;
        for(int j = 3; j <= 7; j = j + 2){
            board[7][num] = j;
            num++;
        }
        num = 5;
        board[7][3] = 2;
        board[7][4] = 1;
        for(int j = 8; j >= 4; j = j - 2){
            board[7][num] = j;
            num++;
        }
        num = 9;
        for(int i = 0; i < 8; i++){
            board[6][i] = num;
            num++;
        }



    }

    public int[][] getBoard() {
        return board;
    }

    //aggregates all possible white moves, without taking checks into consideration. collects all locations of threatened squares (i. e., where black's king cannot move.)
    public ArrayList<Loc> getWhiteThreats(ArrayList<Piece> pieces, int[][] board){
        ArrayList<Loc> whiteThreats = new ArrayList<>();
        for(int i = 0; i < 16; i++){
            if(!pieces.get(i).isCaptured()) {
                whiteThreats.addAll(pieces.get(i).calculateMoves(pieces.get(i).getLoc(), board));
            }
        }
        return whiteThreats;
    }
    public ArrayList<Loc> getBlackThreats(ArrayList<Piece> pieces, int[][] board){
        ArrayList<Loc> blackThreats = new ArrayList<>();
        for(int i = 16; i < pieces.size() ; i++){
            if(!pieces.get(i).isCaptured()) {
                blackThreats.addAll(pieces.get(i).calculateMoves(pieces.get(i).getLoc(), board));
            }
        }
        return blackThreats;
    }

    public boolean isInCheck(int color,ArrayList<Piece> pieces, int[][] board){
        ArrayList<Loc> threats = new ArrayList<>();
        if(color < 0){
            King king = (King) pieces.get(16);
            Loc kingPos = king.getLoc();
            threats = this.getWhiteThreats(pieces, board);
            for(int i = 0; i < threats.size(); i++){
                if(kingPos.compareTo(threats.get(i)) == 0){
                    return true;
                }
            }
            return false;
        }
        else{
            King king = (King)pieces.get(0);
            Loc kingPos = king.getLoc();
            threats = this.getBlackThreats(pieces, board);
            for(int i = 0; i < threats.size(); i++){
                if(kingPos.compareTo(threats.get(i)) == 0){
                    return true;
                }
            }
            return false;
        }
    }

    public int isEndgame(int color, int[][] board){
        ArrayList<Loc> allMoves = new ArrayList<>();
        findLegalmoves(color, board);
        if(color > 0){
            for(int i = 0; i<16; i++){
                if(!pieces.get(i).isCaptured()) {
                    allMoves.addAll(pieces.get(i).getMoves());
                }
            }
            if(allMoves.size() == 0){
                if(isInCheck(1, pieces, board)){
                    return 1; //checkmate
                }
                else{
                    return -1; //stalemate
                }
            }
            else{
                return 0; // game still in progress
            }
        }
        else if(color < 0){
            for(int i = 16; i< pieces.size(); i++){
                if(!pieces.get(i).isCaptured()) {
                    allMoves.addAll(pieces.get(i).getMoves());
                }
            }
            if(allMoves.size() == 0){
                if(isInCheck(-1, pieces, board)){
                    return 1; //checkmate
                }
                else{
                    return -1; //stalemate
                }
            }
            else{
                return 0; // game still in progress
            }
        }
        else{
            return 0;
        }
    }

    public void findLegalmoves(int color, int[][] board) {
        int[][] boardCopy = board;
        if (color > 0) {
            for (int i = 0; i < 16; i++) {
                ArrayList<Loc> moves = new ArrayList<>();
                boardCopy = board.clone();
                if (!pieces.get(i).isCaptured()) {
                    Piece p = pieces.get(i);
                    Loc locCopy = p.getLoc();
                    if (p instanceof Pawn) {
                        moves.addAll(((Pawn) p).pawnMovements(p.getLoc(), boardCopy));
                        ArrayList<Loc> allmoves = p.calculateMoves(p.getLoc(), boardCopy);
                        for(int j = 0; j < allmoves.size(); j++){
                            if(((Pawn) p).canCapture(p.getColor(), allmoves.get(j), boardCopy)){
                                moves.add(allmoves.get(j));
                            }
                        }
                    }
                    else {
                        moves.addAll(p.calculateMoves(p.getLoc(), boardCopy));
                    }
                    ArrayList<Loc> newMoves = new ArrayList<>();
                    for (int j = 0; j < moves.size(); j++) {
                        ArrayList<Piece> pieceCopy = getAllPieces();
                        Piece pc = pieceCopy.get(i);
                        pc.setLoc(locCopy);
                        boardCopy = board.clone();
                        if (boardCopy[moves.get(j).getY()][moves.get(j).getX()] * pc.getColor() < 0) {
                            getPiece(boardCopy[moves.get(j).getY()][moves.get(j).getX()], pieceCopy).setIsCaptured(true);
                        }
                        int numcopy = boardCopy[moves.get(j).getY()][moves.get(j).getX()];
                        pc.setLoc(moves.get(j));

                        boardCopy[pc.getLoc().getY()][pc.getLoc().getX()] = pc.getColor();
                        boardCopy[locCopy.getY()][locCopy.getX()] = 0;
                        if (!isInCheck(color, pieceCopy, boardCopy)) {
                            newMoves.add(moves.get(j));
                        }
                        boardCopy[locCopy.getY()][locCopy.getX()] = pc.getColor();
                        boardCopy[moves.get(j).getY()][moves.get(j).getX()] = numcopy;
                        if (boardCopy[moves.get(j).getY()][moves.get(j).getX()] * pc.getColor() < 0) {
                            getPiece(boardCopy[moves.get(j).getY()][moves.get(j).getX()], pieceCopy).setIsCaptured(false);
                        }

                        pc.setLoc(locCopy);

                    }
                    pieces.get(i).setMoves(newMoves);
                }
            }
        } else {
            for (int i = 16; i < pieces.size(); i++) {
                ArrayList<Loc> moves = new ArrayList<>();
                if (!pieces.get(i).isCaptured()) {
                    boardCopy = board;
                    Piece p = pieces.get(i);
                    Loc locCopy = p.getLoc();
                    if (p instanceof Pawn) {
                        moves.addAll(((Pawn) p).pawnMovements(p.getLoc(), boardCopy));
                        ArrayList<Loc> allmoves = p.calculateMoves(p.getLoc(), boardCopy);
                        for(int j = 0; j < allmoves.size(); j++){
                            if(((Pawn) p).canCapture(p.getColor(), allmoves.get(j), boardCopy)){
                                moves.add(allmoves.get(j));
                            }
                        }
                    }
                    else {
                        moves.addAll(p.calculateMoves(p.getLoc(), boardCopy));
                    }
                    ArrayList<Loc> newMoves = new ArrayList<>();
                    for (int j = 0; j < moves.size(); j++) {
                        ArrayList<Piece> pieceCopy = getAllPieces();
                        Piece pc = pieceCopy.get(i);
                        pc.setLoc(locCopy);
                        boardCopy = board.clone();
                        if (boardCopy[moves.get(j).getY()][moves.get(j).getX()] * pc.getColor() < 0) {
                            getPiece(boardCopy[moves.get(j).getY()][moves.get(j).getX()], pieceCopy).setIsCaptured(true);
                        }
                        int numcopy = boardCopy[moves.get(j).getY()][moves.get(j).getX()];
                        pc.setLoc(moves.get(j));

                        boardCopy[pc.getLoc().getY()][pc.getLoc().getX()] = pc.getColor();
                        boardCopy[locCopy.getY()][locCopy.getX()] = 0;
                        if (!isInCheck(color, pieceCopy, boardCopy)) {
                            newMoves.add(moves.get(j));
                        }
                        boardCopy[locCopy.getY()][locCopy.getX()] = pc.getColor();
                        boardCopy[moves.get(j).getY()][moves.get(j).getX()] = numcopy;
                        if (boardCopy[moves.get(j).getY()][moves.get(j).getX()] * pc.getColor() < 0) {
                            getPiece(boardCopy[moves.get(j).getY()][moves.get(j).getX()], pieceCopy).setIsCaptured(false);
                        }
                        pc.setLoc(locCopy);
                    }
                    pieces.get(i).setMoves(newMoves);

                }

            }
        }
    }


    public Piece getPiece(int color, ArrayList<Piece> pieces){
        if(color < 0){
            return pieces.get((color*(-1)) + 15);
        }
        else{
            return pieces.get(color - 1);
        }
    }
    public ArrayList<Piece> getAllPieces(){
        return pieces;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setShowMoves(int color, int lastSelectedPiece){
        ArrayList<Loc> moves = this.getPiece(color, this.pieces).getMoves();
        if(this.getPiece(color, this.pieces) instanceof Pawn && ((Pawn) this.getPiece(color, this.pieces)).canEnPassantLeft(color, lastSelectedPiece, this)) {
            if (color > 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() - 1, this.getPiece(color, this.pieces).getLoc().getY() - 1));
            } else if (color < 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() - 1, this.getPiece(color, this.pieces).getLoc().getY() + 1));
            }
        }
        if(this.getPiece(color, this.pieces) instanceof Pawn && ((Pawn) this.getPiece(color, this.pieces)).canEnPassantRight(color, lastSelectedPiece, this)) {
            if (color > 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() + 1, this.getPiece(color, this.pieces).getLoc().getY() - 1));
            } else if (color < 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() + 1, this.getPiece(color, this.pieces).getLoc().getY() + 1));
            }
        }

        if(this.getPiece(color, this.pieces) instanceof King && ((King) this.getPiece(color, this.pieces)).canCastle(color, this)) {
            if (color > 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() + 2, this.getPiece(color, this.pieces).getLoc().getY()));
            } else if (color < 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() + 2, this.getPiece(color, this.pieces).getLoc().getY()));
            }
        }

        if(this.getPiece(color, this.pieces) instanceof King && ((King) this.getPiece(color, this.pieces)).canQueenSideCastle(color, this)) {
            if (color > 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() - 2, this.getPiece(color, this.pieces).getLoc().getY()));
            } else if (color < 0) {
                moves.add(new Loc(this.getPiece(color, this.pieces).getLoc().getX() - 2, this.getPiece(color, this.pieces).getLoc().getY()));
            }
        }

        if(this.isInCheck(color, this.pieces, this.board)){
            if(color > 0){
                Loc kingLoc = this.getPiece(1, this.pieces).getLoc();
                moveDisplay[kingLoc.getY()][kingLoc.getX()] = 2;
            }
            else if(color < 0){
                Loc kingLoc = this.getPiece(-1, this.pieces).getLoc();
                moveDisplay[kingLoc.getY()][kingLoc.getX()] = 2;
            }
        }


        for(int i = 0; i < moveDisplay.length; i++){
            for(int j = 0; j < moveDisplay.length; j++){
               for(int k = 0; k < moves.size(); k++){
                   if(i == moves.get(k).getY() && j == moves.get(k).getX()){
                       moveDisplay[i][j] = 1;
                   }


               }

            }
        }
    }


    public void clearMoves(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                moveDisplay[i][j] = 0;
            }
        }
    }

    public int[][] getMoveDisplay(){
        return moveDisplay;
    }

    public void printBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
