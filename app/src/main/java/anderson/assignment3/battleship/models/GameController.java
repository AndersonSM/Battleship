package anderson.assignment3.battleship.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by anderson on 10/25/15.
 */
public class GameController {
    private static GameController Instance = null;
    private Game currentGame = null;
    private static ArrayList<Game> games;

    public GameController(){
    }

    public static GameController getInstance(){
        if(Instance == null){
            Instance = new GameController();
            games = new ArrayList<>();
        }

        return Instance;
    }

    public void startNewGame(){
        currentGame = new Game();
        games.add(0, currentGame);
    }

    public void startNewGame(String tag){
        startNewGame();
        currentGame.setTag(tag);
    }

    public boolean attackSpace(int x, int y){
        boolean attackPossible = currentGame.attackSpace(x, y);
        if(attackPossible){
            currentGame.switchCurrentPlayer();
        }
        return attackPossible;
    }

    public boolean isCurrentGameOver(){
        return currentGame.isGameOver();
    }

    public int[][] getCurrentPlayerGrid(){
        return currentGame.getCurrentPlayer().getGrid();
    }

    public int[][] getCurrentEnemyGrid(){
        return currentGame.getCurrentEnemy().getGrid();
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public ArrayList<Game> getGamesList(){
        return games;
    }

    public void setCurrentGame(int position){
        currentGame = games.get(position);
    }

    public String getCurrentPlayerName(){
        return currentGame.getCurrentPlayer().getName();
    }

    public String getCurrentEnemyName(){
        return currentGame.getCurrentEnemy().getName();
    }

    public void loadGames(String path) throws Exception {
        Gson gson = new Gson();

        try {
            File gamesFile = new File(path);
            FileReader fileReader = new FileReader(gamesFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String gamesJson = reader.readLine();

            Type collectionType = new TypeToken<ArrayList<Game>>(){}.getType();
            games = gson.fromJson(gamesJson, collectionType);
        } catch (FileNotFoundException e){
            saveGames(path);
            loadGames(path);
            throw new Exception("Loading file created.");
        } catch (Exception e){
            throw new Exception("Error - Loading games");
        }
    }

    public void saveGames(String path) throws Exception {
        Gson gson = new Gson();
        String jsonGames = gson.toJson(games);

        try {
            File gamesFile = new File(path);
            FileWriter fileWriter = new FileWriter(gamesFile, false);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(jsonGames);
            writer.close();
        } catch (Exception e){
            throw new Exception("Error - Saving games");
        }
    }
}
