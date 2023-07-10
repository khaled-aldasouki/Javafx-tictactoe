import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Assignment3 extends Application {
    
    String player = "X";    //variable holding the player's sign, always starts as X
    String[][] boardMatrix = new String[3][3];  // a matrix version of the game board which is used to check what cells are marked by what player
    Text stateText = new Text("The X player turn"); //the text above the board indicating the state of the game
    GridPane board = new GridPane();    //main playing board
    int plays = 0;  //number of plays made in total
    boolean winner; //variable used to indicate whether a player won or not

    @Override
    public void start(Stage stage) throws Exception {

        stateText.setFont(Font. font("Courier",15)); 

        VBox mainpane = new VBox(); //main pane that holds the board, stateText and reset button in a vertical line
        mainpane.setAlignment(Pos.CENTER);

        board.setAlignment(Pos.CENTER);
        board.setPadding(new Insets(60,0,50,0));;

        
        Button reset = new Button("Reset"); //button below the board used to reset the game
        reset.setFont(Font. font("Courier",15)); 
        reset.setMaxWidth(90);
        reset.setOnAction(e -> reset());
        
        //board initilization 
        for(int i=0;i<3;i++){
            for(int j = 0;j<3;j++){
                gameCell cell = new gameCell();
                cell.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                cell.setPrefSize(100,100);
                int column = i;
                int row = j;
                cell.setOnMouseClicked(e -> mark(cell,column,row));
                board.add(cell, i, j);
            }
        }

        mainpane.getChildren().addAll(stateText,board,reset);

        Scene scene = new Scene(mainpane,600,600);
        stage.setTitle("XO Game");
        stage.setScene(scene);
        stage.show();

    }    
    

    private void mark(StackPane pane,int column, int row) {
        //method used to mark a cell with X or O, first checks if there's a winner, if not then it's the other players turn 
        //also marks the corrosponding place in the matrix 
        plays += 1;
        boardMatrix[row][column] = player;

        if (player == "X"){
            Line line1 = new Line(20,20,80,80);
            line1.setStroke(Color.BLACK);

            Line line2 = new Line(20,80,80,20);
            line2.setStroke(Color.BLACK);

            pane.getChildren().add(line1);
            pane.getChildren().add(line2);
            pane.setDisable(true);
            
        }

        else if (player == "O"){
            Circle O = new Circle(40);
            O.setStroke(Color.BLACK);
            O.setFill(Color.LIGHTGRAY);

            pane.getChildren().add(O);
            pane.setDisable(true);
            
        }
       
        
        switchPlayer(checkIfWon());
    }

    private Boolean checkIfWon() {
        //checks all possible ways a player could win, if all are false and 9 plays have been made the game ends with no winner
        for (int n = 0; n <3;n++){
            //checks for vertical or horizontal connections
            if ((boardMatrix[n][0] + boardMatrix[n][1] + boardMatrix[n][2]).equals(player + player + player )){
                winner = true;
                endGame();
                return false;
            }
       
            else if ((boardMatrix[0][n] + boardMatrix[1][n] + boardMatrix[2][n]).equals(player + player + player )){
                winner = true;
                endGame();
                return false;
            }
            
        }
        
        //checks for diagonal connections
        if ((boardMatrix[0][0] + boardMatrix[1][1] + boardMatrix[2][2]).equals(player + player + player )){
            winner = true;
            endGame();
            return false;
        }

        if ((boardMatrix[0][2] + boardMatrix[1][1] + boardMatrix[2][0]).equals(player + player + player )){
            winner = true;
            endGame();
            return false;
        }
        if (plays == 9){
            winner = false;
            endGame();
            return false;
        }
        return true;
    }   


    private void switchPlayer(Boolean gameIsRunning) {
        //switches the player and the stateText as long as teh game is still running (as long as there is no winner and less than 9 plays have been made, the game counts as running)
        if (gameIsRunning == true){
            if (player == "X")
                player = "O";
            else 
                player = "X";
            
            stateText.setText("The " + player + " player turn");
        }
    }

    private void endGame(){
        //changes the stateText to indicate winner/draw and disables the whole board
        stateText.setFill(Color.BLUE);
        stateText.setText(player + " player wins");

        if (plays == 9 && winner == false){
            stateText.setText("No Winner");
        }

        for (Node node:board.getChildren()){
            node.setDisable(true);
        }

    }

    private void reset(){
        //removes all marks and resets the matrix, resets player, # of plays and the stateText
        for (Node node:board.getChildren()){
            ((Assignment3.gameCell) node).resetCell();
            node.setDisable(false);
        }

        for (int i = 0; i< 3; i++){
            for (int j = 0; j <3;j++){
                boardMatrix[i][j] = null;
            }
        }
        player = "X";
        plays = 0;
        stateText.setFill(Color.BLACK);
        stateText.setText("The " + player + " player turn");

    }

    class gameCell extends StackPane{
        //a custom variant of stackPane which has a resetCell method
        
        public void resetCell(){
            //removes all the children (marks) on the cell
            this.getChildren().clear();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
