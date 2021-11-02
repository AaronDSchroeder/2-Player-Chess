package sample;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.*;

import javax.swing.plaf.basic.BasicBorders;
import java.io.*;


import java.io.IOException;
import java.util.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException{
        primaryStage.setTitle("Chess");
        Board board = new Board();
        int[][] chess = board.getBoard();
        int[][] moveDisplay = board.getMoveDisplay();
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        primaryStage.setScene(new Scene(root, 1000, 775));
        grid.setAlignment(Pos.CENTER);
        primaryStage.setResizable(true);
        SimpleObjectProperty<Color> ColorSwitch = new SimpleObjectProperty<Color>(Color.WHITE);
        BooleanProperty hasClicked = new SimpleBooleanProperty(false);
        SimpleObjectProperty<Integer> selectedPiece = new SimpleObjectProperty<>(0);
        SimpleObjectProperty<Integer> lastSelectedPiece = new SimpleObjectProperty<>(0);
        for (int i = 0; i < 8; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(90, 90, Double.MAX_VALUE));
            grid.getRowConstraints().add(new RowConstraints(90, 90, Double.MAX_VALUE));
        }
        grid.getChildren().clear();
        Button[][] squares = new Button[8][8];

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Button();
                squares[i][j].setStyle(setSquareColor(i, j));
                squares[i][j].setMaxSize(90, 90);
                squares[i][j].setMinSize(90, 90);
                final int x = i;
                final int y = j;
                try {
                    setImages(board, squares);
                }
                catch (IOException ex){
                    ex.getMessage();
                }
                squares[i][j].setOnMouseClicked(e -> {
                    int[][] chessboard = board.getBoard();
                    if (ColorSwitch.get() == Color.WHITE) {


                        if (!hasClicked.getValue() && chessboard[y][x] > 0) {
                            colorSquares(x, y, board, squares, lastSelectedPiece.get());
                            selectedPiece.set(chessboard[y][x]);
                            hasClicked.setValue(true);
                        } else if (hasClicked.getValue()) {
                            uncolorSquares(x, y, board, squares);
                            if (board.getMoveDisplay()[y][x] == 1) {
                                if (board.getPiece((selectedPiece.get()), board.pieces).getColor() * chessboard[y][x] < 0) {
                                    board.getPiece(chessboard[y][x], board.pieces).setIsCaptured(true);
                                }
                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof Pawn){
                                    if(((Pawn) board.getPiece(selectedPiece.get(), board.pieces)).canEnPassantLeft(selectedPiece.get(), lastSelectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() - 1){
                                            board.getPiece(chessboard[y+1][x], board.pieces).setIsCaptured(true);
                                            chessboard[y+1][x] = 0;
                                        }
                                    }
                                    else if(((Pawn) board.getPiece(selectedPiece.get(), board.pieces)).canEnPassantRight(selectedPiece.get(), lastSelectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() + 1){
                                            board.getPiece(chessboard[y+1][x], board.pieces).setIsCaptured(true);
                                            chessboard[y+1][x] = 0;
                                        }
                                    }
                                }

                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof King){
                                    if(((King) board.getPiece(selectedPiece.get(), board.pieces)).canCastle(selectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() + 2){
                                            board.getPiece(chessboard[y][x+1], board.pieces).setLoc(x - 1, y);
                                            chessboard[y][x-1] = chessboard[y][x+1];
                                            chessboard[y][x+1] = 0;
                                        }
                                    }
                                }
                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof King){
                                    if(((King) board.getPiece(selectedPiece.get(), board.pieces)).canQueenSideCastle(selectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() - 2){
                                            board.getPiece(chessboard[y][x-2], board.pieces).setLoc(x + 1, y);
                                            chessboard[y][x+1] = chessboard[y][x-2];
                                            chessboard[y][x-2] = 0;
                                        }
                                    }
                                }

                                lastSelectedPiece.set(selectedPiece.get());
                                chessboard[y][x] = selectedPiece.get();
                                chessboard[board.getPiece(selectedPiece.get(), board.pieces).getLoc().getY()][board.getPiece(selectedPiece.get(), board.pieces).getLoc().getX()] = 0;
                                board.getPiece(selectedPiece.get(), board.pieces).setLoc(x, y);
                                board.setBoard(chessboard);
                                //checking for pawn promotion
                                if (board.getPiece(selectedPiece.get(), board.pieces) instanceof Pawn && y == 0) {
                                    int PawnIndex = selectedPiece.get();
                                    hasClicked.set(true);
                                    final String[] dialogData = {"Queen", "Bishop", "Rook", "Knight"};
                                    List<String> data = Arrays.asList(dialogData);
                                    ChoiceDialog dialog = new ChoiceDialog(data.get(0), data);

                                    toggleDisableButtons(squares);
                                    dialog.setTitle("Pawn Promotion");
                                    dialog.setHeaderText("Please make a selection.");
                                    Optional<String> result = dialog.showAndWait();
                                    String select = "Queen";
                                    if(result.isPresent()){
                                        select = result.get();
                                    }



                                    if (select.equals("Queen")) {
                                        board.pieces.set(PawnIndex - 1, new Queen(x, y, chessboard, PawnIndex));
                                    } else if (select.equals("Rook")) {
                                        board.pieces.set(PawnIndex - 1, new Rook(x, y, chessboard, PawnIndex));
                                    } else if (select.equals("Bishop")) {
                                        board.pieces.set(PawnIndex - 1, new Bishop(x, y, chessboard, PawnIndex));
                                    } else if (select.equals("Knight")) {
                                        board.pieces.set(PawnIndex - 1, new Knight(x, y, chessboard, PawnIndex));
                                    }

                                    try {
                                        setImages(board, squares);
                                    } catch (Exception o) {
                                        o.getMessage();
                                    }
                                    toggleDisableButtons(squares);
                                    if(board.isEndgame(-1, chessboard) == 1){
                                        Label label = new Label("CHECKMATE: WHITE WINS      ");
                                        root.setTop(label);
                                        toggleDisableButtons(squares);
                                    }
                                    else if(board.isEndgame(-1, chessboard) == -1 || board.isEndgame(1, chessboard) == -1){
                                        Label label = new Label("STALEMATE: DRAW       ");
                                        root.setTop(label);
                                        toggleDisableButtons(squares);
                                    }




                                }


                                ColorSwitch.set(Color.BLACK);
                            }
                            board.clearMoves();
                            hasClicked.setValue(false);
                        }
                            if(board.isEndgame(-1, chessboard) == 1){
                                Label label = new Label("CHECKMATE: WHITE WINS      ");
                                root.setTop(label);
                                toggleDisableButtons(squares);
                            }
                            else if(board.isEndgame(-1, chessboard) == -1 || board.isEndgame(1, chessboard) == -1){
                                Label label = new Label("STALEMATE: DRAW       ");
                                root.setTop(label);
                                toggleDisableButtons(squares);
                            }

                    } else {
                        if (!hasClicked.getValue() && chessboard[y][x] < 0) {
                            colorSquares(x, y, board, squares, lastSelectedPiece.get());
                            selectedPiece.set(chessboard[y][x]);
                            hasClicked.setValue(true);
                        } else if (hasClicked.getValue()) {
                            uncolorSquares(x, y, board, squares);
                            if (board.getMoveDisplay()[y][x] == 1) {
                                if (board.getPiece((selectedPiece.get()), board.pieces).getColor() * chessboard[y][x] < 0) {
                                    board.getPiece(chessboard[y][x], board.pieces).setIsCaptured(true);
                                }
                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof Pawn){
                                    if(((Pawn) board.getPiece(selectedPiece.get(), board.pieces)).canEnPassantLeft(selectedPiece.get(), lastSelectedPiece.get(), board)){
                                       Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                       if(x == pieceLoc.getX() - 1){
                                           board.getPiece(chessboard[y-1][x], board.pieces).setIsCaptured(true);
                                           chessboard[y-1][x] = 0;
                                       }
                                    }
                                    else if(((Pawn) board.getPiece(selectedPiece.get(), board.pieces)).canEnPassantRight(selectedPiece.get(), lastSelectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() + 1){
                                            board.getPiece(chessboard[y-1][x], board.pieces).setIsCaptured(true);
                                            chessboard[y-1][x] = 0;
                                        }
                                    }
                                }
                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof King){
                                    if(((King) board.getPiece(selectedPiece.get(), board.pieces)).canCastle(selectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() + 2){
                                            board.getPiece(chessboard[y][x+1], board.pieces).setLoc(x - 1, y);
                                            chessboard[y][x-1] = chessboard[y][x+1];
                                            chessboard[y][x+1] = 0;
                                        }
                                    }
                                }

                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof King){
                                    if(((King) board.getPiece(selectedPiece.get(), board.pieces)).canQueenSideCastle(selectedPiece.get(), board)){
                                        Loc pieceLoc = board.getPiece(selectedPiece.get(), board.pieces).getLoc();
                                        if(x == pieceLoc.getX() - 2){
                                            board.getPiece(chessboard[y][x-2], board.pieces).setLoc(x + 1, y);
                                            chessboard[y][x+1] = chessboard[y][x-2];
                                            chessboard[y][x-2] = 0;
                                        }
                                    }
                                }

                                lastSelectedPiece.set(selectedPiece.get());
                                chessboard[y][x] = selectedPiece.get();
                                chessboard[board.getPiece(selectedPiece.get(), board.pieces).getLoc().getY()][board.getPiece(selectedPiece.get(), board.pieces).getLoc().getX()] = 0;
                                board.getPiece(selectedPiece.get(), board.pieces).setLoc(x, y);
                                board.setBoard(chessboard);


                                // Black Pawn Promotion
                                if(board.getPiece(selectedPiece.get(), board.pieces) instanceof Pawn && y == 7){
                                    int PawnIndex = selectedPiece.get();
                                    hasClicked.set(true);
                                    final String[] dialogData = {"Queen", "Bishop", "Rook", "Knight"};
                                    List<String> data = Arrays.asList(dialogData);
                                    ChoiceDialog dialog = new ChoiceDialog(data.get(0), data);
                                    dialog.setTitle("Pawn Promotion");
                                    dialog.setHeaderText("Please make a selection.");
                                    Optional<String> result = dialog.showAndWait();
                                    String select = "Queen";
                                    if(result.isPresent()){
                                        select = result.get();
                                    }
                                    toggleDisableButtons(squares);
                                    if (select.equals("Queen")) {
                                        board.pieces.set(PawnIndex*(-1) + 15, new Queen(x, y, chessboard, PawnIndex));
                                    }
                                    else if(select.equals("Rook")){
                                        board.pieces.set(PawnIndex*(-1) + 15, new Rook(x, y, chessboard, PawnIndex));
                                    }
                                    else if(select.equals("Bishop")){
                                        board.pieces.set(PawnIndex*(-1) + 15, new Bishop(x, y, chessboard, PawnIndex));
                                    }
                                    else if(select.equals("Knight")){
                                        board.pieces.set(PawnIndex*(-1) + 15, new Knight(x, y, chessboard, PawnIndex));
                                    }

                                    try {
                                        setImages(board, squares);
                                    }
                                    catch (Exception o){
                                        o.getMessage();
                                    }
                                    toggleDisableButtons(squares);
                                    if(board.isEndgame(1, chessboard) == 1){
                                        Label label = new Label("CHECKMATE: BLACK WINS      ");
                                        root.setTop(label);
                                        toggleDisableButtons(squares);
                                    }
                                    else if(board.isEndgame(1, chessboard) == -1 || board.isEndgame(1, chessboard) == -1){
                                        Label label = new Label("STALEMATE: DRAW       ");
                                        root.setTop(label);
                                        toggleDisableButtons(squares);
                                    }



                                }

                                ColorSwitch.set(Color.WHITE);

                            }
                            board.clearMoves();
                            hasClicked.setValue(false);
                        }
                        if(board.isEndgame(1, chessboard) == 1){
                            Label label = new Label("CHECKMATE: BLACK WINS      ");
                            root.setTop(label);
                            toggleDisableButtons(squares);
                        }
                        else if(board.isEndgame(1, chessboard) == -1 || board.isEndgame(1, chessboard) == -1){
                            Label label = new Label("STALEMATE: DRAW       ");
                            root.setTop(label);
                            toggleDisableButtons(squares);
                        }
                    }
                    try {
                        setImages(board, squares);
                    }
                    catch (Exception o){
                        o.getMessage();
                    }
                });

                squares[i][j].setAlignment(Pos.CENTER);
                grid.add(squares[i][j], i, j);

            }


        }





        grid.setGridLinesVisible(true);
        root.setCenter(grid);
        primaryStage.show();
}

    public static int[][] retrievePossibleMoves(int i, int j, Board board, int lastSelectedPiece){
        int[][] chessboard= board.getBoard();
        board.findLegalmoves(chessboard[j][i],chessboard);
        board.setShowMoves(chessboard[j][i], lastSelectedPiece);
        return board.getMoveDisplay();
        }
    public void colorSquares(int i, int j, Board board, Button[][] squares, int lastSelectedPiece){
    int[][] moves = retrievePossibleMoves(i, j, board, lastSelectedPiece);

            for (int k = 0; k < moves.length; k++) {
                for (int l = 0; l < moves.length; l++) {
                    if (moves[l][k] == 1) {
                        squares[k][l].setStyle("-fx-background-color:yellow");
                    }
                    if (moves[l][k] == 2) {
                        squares[k][l].setStyle("-fx-background-color:red");
                    }
                }
            }

    }

    public void toggleDisableButtons(Button[][] squares){
        for(int l = 0; l < squares.length; l++){
            for(int m = 0; m < squares[l].length; m++){
                if(squares[l][m].isDisabled()) {
                    squares[l][m].setDisable(false);
                }
                else{
                    squares[l][m].setDisable(true);
                }
            }
        }
    }



    public void uncolorSquares(int i, int j, Board board, Button[][] squares){
        for (int k = 0; k < squares.length; k++) {
            for (int l = 0; l < squares.length; l++) {
                 squares[k][l].setStyle(setSquareColor(k, l));
            }
        }
    }

    public void setImages(Board board, Button[][] squares) throws IOException {
        try {
            for (int i = 0; i < squares.length; i++) {
                for (int j = 0; j < squares.length; j++) {

                    if (board.getBoard()[j][i] == 0) {
                        FileInputStream input = new FileInputStream("Images/wRook.png");
                        Image image = new Image(input);
                        ImageView imageview = new ImageView(image);
                        imageview.setOpacity(0);
                        squares[i][j].setGraphic(imageview);
                    } else if(board.getBoard()[j][i] > 0){

                      if(board.getBoard()[j][i] == 1){
                          FileInputStream input = new FileInputStream("Images/wKing.png");
                          ImageView imageView = new ImageView(new Image(input));
                          squares[i][j].setGraphic(imageView);
                      }
                      else if(board.getBoard()[j][i] == 2){
                          FileInputStream input = new FileInputStream("Images/wQueen.png");
                          ImageView imageView = new ImageView(new Image(input));
                          squares[i][j].setGraphic(imageView);
                      }
                      else if(board.getBoard()[j][i] == 3 || board.getBoard()[j][i] == 4){
                          FileInputStream input = new FileInputStream("Images/wRook.png");
                          ImageView imageView = new ImageView(new Image(input));
                          squares[i][j].setGraphic(imageView);
                      }
                      else if(board.getBoard()[j][i] == 5 || board.getBoard()[j][i] == 6){
                          FileInputStream input = new FileInputStream("Images/wKnight.png");
                          ImageView imageView = new ImageView(new Image(input));
                          squares[i][j].setGraphic(imageView);
                      }
                      else if(board.getBoard()[j][i] == 7 || board.getBoard()[j][i] == 8){
                          FileInputStream input = new FileInputStream("Images/wBishop.png");
                          ImageView imageView = new ImageView(new Image(input));
                          squares[i][j].setGraphic(imageView);
                      }
                      else if(board.getBoard()[j][i] > 7 && board.getBoard()[j][i] <= 16){
                          FileInputStream input = new FileInputStream("Images/bPawn.png");
                          if(board.getPiece(board.getBoard()[j][i], board.getAllPieces()) instanceof Pawn) {
                              input = new FileInputStream("Images/wPawn.png");
                          }
                          else if(board.getPiece(board.getBoard()[j][i], board.getAllPieces()) instanceof Queen){
                              input = new FileInputStream("Images/wQueen.png");
                          }
                          else if(board.getPiece(board.getBoard() [j][i], board.getAllPieces()) instanceof Knight){
                              input = new FileInputStream ("Images/wKnight.png");
                          }
                          else if (board.getPiece(board.getBoard() [j][i], board.getAllPieces()) instanceof Rook) {
                              input = new FileInputStream ("Images/wRook.png");
                          }
                          else if (board.getPiece(board.getBoard() [j][i], board.getAllPieces()) instanceof Bishop) {
                              input = new FileInputStream ("Images/wBishop.png");
                          }
                          ImageView imageView = new ImageView(new Image(input));
                          squares[i][j].setGraphic(imageView);
                      }
                    }
                    else{
                        if(board.getBoard()[j][i] == -1){
                            FileInputStream input = new FileInputStream("Images/bKing.png");
                            ImageView imageView = new ImageView(new Image(input));
                            squares[i][j].setGraphic(imageView);
                        }
                        else if(board.getBoard()[j][i] == -2){
                            FileInputStream input = new FileInputStream("Images/bQueen.png");
                            ImageView imageView = new ImageView(new Image(input));
                            squares[i][j].setGraphic(imageView);
                        }
                        else if(board.getBoard()[j][i] == -3 || board.getBoard()[j][i] == -4){
                            FileInputStream input = new FileInputStream("Images/bRook.png");
                            ImageView imageView = new ImageView(new Image(input));
                            squares[i][j].setGraphic(imageView);
                        }
                        else if(board.getBoard()[j][i] == -5 || board.getBoard()[j][i] == -6){
                            FileInputStream input = new FileInputStream("Images/bKnight.png");
                            ImageView imageView = new ImageView(new Image(input));
                            squares[i][j].setGraphic(imageView);
                        }
                        else if(board.getBoard()[j][i] == -7 || board.getBoard()[j][i] == -8){
                            FileInputStream input = new FileInputStream("Images/bBishop.png");
                            ImageView imageView = new ImageView(new Image(input));
                            squares[i][j].setGraphic(imageView);
                        }
                        else if(board.getBoard()[j][i] < -7 && board.getBoard()[j][i] >= -16){
                            FileInputStream input = new FileInputStream("Images/bPawn.png");
                            if(board.getPiece(board.getBoard()[j][i], board.getAllPieces()) instanceof Pawn) {
                                input = new FileInputStream("Images/bPawn.png");
                            }
                            else if(board.getPiece(board.getBoard()[j][i], board.getAllPieces()) instanceof Queen){
                                input = new FileInputStream("Images/bQueen.png");
                            }
                            else if(board.getPiece(board.getBoard() [j][i], board.getAllPieces()) instanceof Knight){
                                input = new FileInputStream ("Images/bKnight.png");
                            }
                            else if (board.getPiece(board.getBoard() [j][i], board.getAllPieces()) instanceof Rook) {
                                input = new FileInputStream ("Images/bRook.png");
                            }
                            else if (board.getPiece(board.getBoard() [j][i], board.getAllPieces()) instanceof Bishop) {
                                input = new FileInputStream ("Images/bBishop.png");
                            }
                            ImageView imageView = new ImageView(new Image(input));
                            squares[i][j].setGraphic(imageView);
                        }
                    }

                }
            }

        }
        catch (Exception e){
            e.getMessage();
        }
    }

    public static String setSquareColor(int i, int j){
        if(i % 2 == 0){
            if((j % 2) == 0) {
                return "-fx-background-color:ivory";
            }
            else{
                return "-fx-background-color:tan";
            }
        }
        else{
            if((j % 2) == 0){
                return "-fx-background-color:tan";
            }
            else{
                return "-fx-background-color:ivory";

            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
