package anderson.assignment3.battleship.models;

import java.io.Serializable;

/**
 * Created by anderson on 10/25/15.
 */
public class Game implements Serializable{
    private Player player1, player2;
    private Player currentPlayer, currentEnemy;
    private Player winner = null;
    private String tag;

    public Game(String tag, String namePlayer1, String namePlayer2) {
        this.tag = tag;
        String name2 = namePlayer2;

        player1 = new Player(namePlayer1);

        if(namePlayer1.equals(namePlayer2)){
            name2 = namePlayer2 + "_2";
        }
        player2 = new Player(name2);

        currentPlayer = player1;
        currentEnemy = player2;
    }

    public Game(String tag) {
        this(tag, "Player 1", "Player 2");
    }

    public Game() {
        this("Game", "Player 1", "Player 2");
    }

    public void switchCurrentPlayer(){
        if(currentPlayer.equals(player1)) {
            currentPlayer = player2;
            currentEnemy = player1;
        } else {
            currentPlayer = player1;
            currentEnemy = player2;
        }
    }

    public boolean attackSpace(int x, int y){
        if(currentEnemy.attackSpace(x, y)){
            currentPlayer.launchMissile();
            return true;
        }

        return false;
    }

    public boolean isGameOver(){
        if(!player1.isAlive()){
            winner = player2;
            return true;
        }
        if(!player2.isAlive()){
            winner = player1;
            return true;
        }

        return false;
    }

    public String getWinnerName(){
        return winner.getName();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getCurrentEnemy(){
        return currentEnemy;
    }

    public String getTag(){
        return tag;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public String getPlayer1Name(){
        return player1.getName();
    }

    public String getPlayer2Name(){
        return player2.getName();
    }

    public int getPlayer1MissilesLaunched(){
        return player1.getMissilesLaunched();
    }

    public int getPlayer2MissilesLaunched(){
        return player2.getMissilesLaunched();
    }
}
