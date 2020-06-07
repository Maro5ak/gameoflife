package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Random;

public class StartGame extends Task<Canvas> {

    //values for constructor
    private final int size;
    private long seed;
    private Canvas canvas;
    private boolean saveFirst;
    private String originalSeed;


    private GraphicsContext gc;
    private long newSeed;
    private int generation = 0;



    public StartGame(int size, long seed, Canvas canvas, boolean saveFirst, String originalSeed){
        this.size = size;
        this.canvas = canvas;
        this.seed = seed;
        this.saveFirst = saveFirst;
        this.originalSeed = originalSeed;
    }

    @Override
    protected Canvas call() throws Exception {
        gc = canvas.getGraphicsContext2D();
        int[][] map = generateMap();
        drawMap(map);
        gameOfLife(map);
        return canvas;
    }
    public int[][] generateMap(){
        int map[][] = new int[size][size];
        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                long randomNumber = getRandomNumber();
                if((randomNumber % newSeed > -(newSeed/98613)) && (randomNumber % newSeed < newSeed*698513)) map[x][y] = 1;
                else map[x][y] = 0;
            }
        }

        return map;
    }
    //endlessly loop this
    private void gameOfLife(int[][] map) {

        int[][] oldMap = map;
        while(true) {
            int[][] newGrid = new int[size][size];
            generation++;
            updateMessage("Generation: " + generation);

            for (int x = 1; x < size - 1; x++) {
                for (int y = 1; y < size - 1; y++) {
                    int aliveCells = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            aliveCells += oldMap[x + i][y + j];
                        }
                    }
                    aliveCells -= oldMap[x][y];
                    //less than 2 neighbours alive = death
                    if (oldMap[x][y] == 1 && aliveCells < 2) newGrid[x][y] = 0;
                        //overpopulation = death
                    else if (oldMap[x][y] == 1 && aliveCells > 3) newGrid[x][y] = 0;
                        //new cell born
                    else if (oldMap[x][y] == 0 && aliveCells == 3) newGrid[x][y] = 1;
                        //remains
                    else newGrid[x][y] = oldMap[x][y];
                }
            }

            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //THIS IS A MESS
            if(saveFirst && generation == 1){
                Platform.runLater(this::saveFirstGeneration);
            }
            //yeah, took me a long time to realise this is the reason why the app freezes...
            Platform.runLater(() -> drawMap(newGrid));
            oldMap = newGrid;

        }
    }
    //Method to draw 10px*10px cubes on the canvas(screen)
    private void drawMap(int[][] map){

        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                if(map[x][y] == 0) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(y*10, x*10, 10, 10);
                }
                else{
                    gc.setFill(Color.BLACK);
                    gc.fillRect(y*10, x*10, 10, 10);
                }
            }
        }

    }
    //some insanely weird calculations for a pseudo random number..
    private long getRandomNumber(){
        this.newSeed = (seed * 95657388) % 6065798;
        Random rnd = new Random(newSeed);
        this.seed = rnd.nextInt();
        return seed;
    }
    //TWO SAME METHODS IN 2 DIFFERENT CLASSES YAY
    private void saveFirstGeneration(){
        WritableImage image = canvas.snapshot(new SnapshotParameters(), new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight()));

        File file = new File(System.getProperty("user.home") + "/Desktop/" + originalSeed + "[0] .png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (Exception e) {
            System.out.println("Unable to create file!");
        }
    }



}
