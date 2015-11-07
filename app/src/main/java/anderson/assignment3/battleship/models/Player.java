package anderson.assignment3.battleship.models;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by anderson on 10/25/15.
 */
public class Player implements Serializable{
    private final int HORIZONTAL = 0;
    private final int VERTICAL = 1;
    // When the value of the space is 0, it means that the space is empty
    public static final int EMPTY = 0;
    // When the value of the space is 1, it means that the space has a ship
    public static final int SHIP = 1;
    // When the value of the space is 2, it means that a ship was hit
    public static final int HIT = 2;
    // When the value of the space is 3, it means that the space was empty and was attacked
    public static final int MISS = 3;

    private int[][] grid;
    private String name;
    private int missilesLaunched;

    public Player(String name) {
        this.name = name;
        grid = new int[10][10];
        randomlyPlaceShips();
    }

    private void randomlyPlaceShips() {
        Random rand = new Random();

        int position;
        int startingPointX;
        int startingPointY;
        int[] shipsSizes = {5, 4, 3, 3, 2};

        for (Integer size : shipsSizes) {
            position = rand.nextInt(2);
            switch (position) {
                case HORIZONTAL:
                    startingPointX = rand.nextInt(10);
                    startingPointY = rand.nextInt((10 - size) + 1);
                    while(!isSpacesSetEmpty(startingPointX, startingPointY, size, position)){
                        startingPointX = rand.nextInt(10);
                        startingPointY = rand.nextInt((10 - size) + 1);
                    }

                    for (int i = startingPointY; i < (startingPointY + size); i++) {
                        grid[startingPointX][i] = 1;
                    }

                    break;

                case VERTICAL:
                    startingPointX = rand.nextInt((10 - size) + 1);
                    startingPointY = rand.nextInt(10);
                    while(!isSpacesSetEmpty(startingPointX, startingPointY, size, position)){
                        startingPointX = rand.nextInt((10 - size) + 1);
                        startingPointY = rand.nextInt(10);
                    }

                    for (int i = startingPointX; i < (startingPointX + size); i++) {
                        grid[i][startingPointY] = 1;
                    }

                    break;
            }
        }
    }

    private boolean isSpacesSetEmpty(int startingPointX, int startingPointY, int size, int pos){
        switch (pos) {
            case HORIZONTAL:
                for (int i = startingPointY; i < (startingPointY + size); i++) {
                    if(!isSpaceEmpty(startingPointX, i)){
                        return false;
                    }
                }
                break;

            case VERTICAL:
                for (int i = startingPointX; i < (startingPointX + size); i++) {
                    if(!isSpaceEmpty(i, startingPointY)){
                        return false;
                    }
                }
                break;

        }

        return true;
    }

    private boolean isSpaceEmpty(int x, int y) {
        return (grid[x][y] == EMPTY);
    }

    private boolean isSpaceAttacked(int x, int y) {
        if(grid[x][y] == EMPTY || grid[x][y] == SHIP) {
            return false;
        }

        return true;
    }

    private boolean canAttack(int x, int y){
        if(isSpaceAttacked(x, y)) {
            return false;
        }

        return true;
    }

    public boolean attackSpace(int x, int y){
        boolean canAttack = false;
        if(canAttack(x, y)) {
            canAttack = true;
            switch (grid[x][y]) {
                case EMPTY:
                    grid[x][y] = MISS;
                    break;

                case SHIP:
                    grid[x][y] = HIT;
                    break;
            }
        }

        return canAttack;
    }

    public boolean isAlive(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(grid[i][j] == SHIP)
                    return true;
            }
        }

        return false;
    }

    public void launchMissile(){
        missilesLaunched++;
    }

    public int getMissilesLaunched(){
        return missilesLaunched;
    }

    public String getName(){
        return name;
    }

    public int[][] getGrid(){
        return grid.clone();
    }
}
