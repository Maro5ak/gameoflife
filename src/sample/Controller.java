package sample;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



public class Controller {

    @FXML
    private Label generationLabel;
    @FXML
    private TextField seedTextField;
    @FXML
    private Canvas canvas;

    private final int SIZE = 85;
    private long seed = 0;
    private byte[] seedArray;

    @FXML
    private void handleEnterSeed() {
        this.seed = 0;
        seedArray = seedTextField.getText().getBytes();
        for(int i = 0; i < seedArray.length; i++){
            this.seed += seedArray[i];
        }
        StartGame game = new StartGame(SIZE, seed, canvas);
        generationLabel.textProperty().bind(game.messageProperty());
        Thread thread = new Thread(game);
        thread.setDaemon(true);
        thread.start();

    }















}
