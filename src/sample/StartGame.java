package sample;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class StartGame extends Task<Canvas> {

    //values for constructor
    private final int size;
    private long seed;
    private Canvas canvas;


    private GraphicsContext gc;
    private long newSeed;
    private int generation = 0;

    public StartGame(int size, long seed, Canvas canvas){
        this.size = size;
        this.canvas = canvas;
        this.seed = seed;
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
            drawMap(newGrid);
            oldMap = newGrid;
        }
    }
    //Method to draw 10px*10px cubes on the canvas(screen)
    private void drawMap(int[][] map){
        //for some reason, if this Sout isn't here it get's stuck randomly.. yeah, don't ask me, I don't know either
        System.out.println("called");
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
    private long getRandomNumber(){
        this.newSeed = (seed * 95657388) % 6065798;
        Random rnd = new Random(newSeed);
        this.seed = rnd.nextInt();
        return seed;
    }

}
