package anderson.assignment3.battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import anderson.assignment3.battleship.models.GameController;

public class GameActivity extends Activity {
    private String GAME_FRAGMENT_TAG = "GAME_FRAGMENT_TAG";
    private String GAMES_LIST_FRAGMENT_TAG = "GAMES_LIST_FRAGMENT_TAG";
    private String FILE_NAME = "games.txt";
    private GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = GameController.getInstance();
        loadGames();

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(rootLayout);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);


        FrameLayout masterFrameLayout = new FrameLayout(this);
        masterFrameLayout.setId(10);
        //if(isTablet(this)) {
            horizontalLayout.addView(masterFrameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        //}

        FrameLayout detailFrameLayout = new FrameLayout(this);
        detailFrameLayout.setId(11);
        horizontalLayout.addView(detailFrameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4));

        rootLayout.addView(horizontalLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        GamesListFragment gamesListFragment = (GamesListFragment) getFragmentManager().findFragmentByTag(GAMES_LIST_FRAGMENT_TAG);
        if (gamesListFragment == null) {
            gamesListFragment = GamesListFragment.newInstance(controller.getGamesList());
            //
            transaction.add(masterFrameLayout.getId(), gamesListFragment, GAMES_LIST_FRAGMENT_TAG);
        }

        GameFragment gameFragment = (GameFragment)getFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG);
        if(gameFragment == null){
            gameFragment = GameFragment.newInstance(null, null);
            transaction.add(detailFrameLayout.getId(), gameFragment, GAME_FRAGMENT_TAG);
        }

        transaction.commit();

        gameFragment.setOnSpaceSelectedListener(new GameFragment.OnSpaceSelectedListener() {
            @Override
            public void onSpaceSelected(int[] points) {
                if (controller.getCurrentGame() == null) {
                    return;
                }

                final GameFragment gameFragment = (GameFragment) getFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG);

                int x = points[0];
                int y = points[1];

                if (!controller.isCurrentGameOver() && controller.attackSpace(x, y)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle(controller.getCurrentPlayerName() + "'s turn!");
                    builder.setMessage("Give the device to your enemy.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gameFragment.setPlayerGrids(controller.getCurrentPlayerGrid(), controller.getCurrentEnemyGrid());
                        }
                    });
                    builder.show();

                } else {
                    Toast.makeText(GameActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                }
                if (controller.isCurrentGameOver()) {
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle("Game Over!")
                            .setMessage("The winner is " + controller.getCurrentGame().getWinnerName())
                            .setPositiveButton("Ok", null)
                            .show();
                    saveGames();
                    return;
                }

                saveGames();

                GamesListFragment gamesListFragment = (GamesListFragment) getFragmentManager().findFragmentByTag(GAMES_LIST_FRAGMENT_TAG);
                gamesListFragment.setGamesList(controller.getGamesList());
            }
        });

        gamesListFragment.setOnNewGameButtonPressedListener(new GamesListFragment.OnNewGameButtonPressedListener() {
            @Override
            public void onNewGameButtonPressed(String tag) {
                GamesListFragment gamesListFragment = (GamesListFragment) getFragmentManager().findFragmentByTag(GAMES_LIST_FRAGMENT_TAG);
                GameFragment gameFragment = (GameFragment) getFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG);

                controller.startNewGame(tag);

                gamesListFragment.setGamesList(controller.getGamesList());
                gamesListFragment.setCurrentGame(controller.getCurrentGame());
                gameFragment.setPlayerGrids(controller.getCurrentPlayerGrid(), controller.getCurrentEnemyGrid());
            }
        });

        gamesListFragment.setOnItemSelectedListener(new GamesListFragment.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                GamesListFragment gamesListFragment = (GamesListFragment) getFragmentManager().findFragmentByTag(GAMES_LIST_FRAGMENT_TAG);
                GameFragment gameFragment = (GameFragment) getFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG);

                controller.setCurrentGame(position);

                gamesListFragment.setGamesList(controller.getGamesList());
                gamesListFragment.setCurrentGame(controller.getCurrentGame());
                gameFragment.setPlayerGrids(controller.getCurrentPlayerGrid(), controller.getCurrentEnemyGrid());

                Toast.makeText(GameActivity.this, controller.getCurrentPlayerName() + "'s turn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveGames();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void saveGames(){
        try {
            controller.saveGames(new File(getFilesDir(), FILE_NAME).getPath());
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void loadGames(){
        try {
            controller.loadGames(new File(getFilesDir(), FILE_NAME).getPath());
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
