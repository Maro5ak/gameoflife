package sample;


import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.channels.WritableByteChannel;


public class Controller {

    @FXML
    private CheckBox saveFirstCheck;
    @FXML
    private Label generationLabel;
    @FXML
    private TextField seedTextField;
    @FXML
    private Canvas canvas;
    @FXML
    private Button buttonSeed;

    private final int SIZE = 85;
    private long seed = 0;
    private byte[] seedArray;
    private int snapshotsTaken = 0;


    @FXML
    private void handleEnterSeed() {
        if(seedTextField.getText().isEmpty()){
            seedTextField.setPromptText("Insert anything!");
        }
        else {
            buttonSeed.setDisable(true);
            this.seed = 0;
            snapshotsTaken = 0;
            seedArray = seedTextField.getText().getBytes();
            for (int i = 0; i < seedArray.length; i++) {
                this.seed += seedArray[i];
            }
            StartGame game = new StartGame(SIZE, seed, canvas, saveFirstCheck.isSelected(), seedTextField.getText());

            Thread thread = new Thread(game);
            thread.setDaemon(true);
            generationLabel.textProperty().bind(game.messageProperty());
            thread.start();
        }

    }

    @FXML
    private void handleTakeSnapshot(){
        snapshotsTaken++;
        WritableImage image = canvas.snapshot(new SnapshotParameters(), new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight()));

        File file = new File(System.getProperty("user.home") + "/Desktop/" + seedTextField.getText() + "[" + snapshotsTaken + "].png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (Exception e) {
            System.out.println("Unable to create file!");
        }
    }


}
