package anderson.assignment3.battleship;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import anderson.assignment3.battleship.models.Game;

/**
 * Created by anderson on 10/26/15.
 */
public class GamesListFragment extends Fragment implements ListAdapter{
    public interface OnNewGameButtonPressedListener {
        void onNewGameButtonPressed(String tag);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    private static final String GAMES_LIST_KEY = "GAMES_LIST_KEY";
    private OnNewGameButtonPressedListener onNewGameButtonPressedListener = null;
    private OnItemSelectedListener onItemSelectedListener = null;
    private ArrayList<Game> games;
    private Game currentGame;
    private ListView listView;

    public static GamesListFragment newInstance(List<Game> games){
        GamesListFragment fragment = new GamesListFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(GAMES_LIST_KEY, (Serializable) games);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout rootLayout = new LinearLayout(getActivity());
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        Button newGameButton = new Button(getActivity());
        newGameButton.setText("Create new game");
        newGameButton.setTextSize(5 * getResources().getDisplayMetrics().density);
        rootLayout.addView(newGameButton);

        games = new ArrayList<>();
        if(getArguments() != null && getArguments().containsKey(GAMES_LIST_KEY)){
            games = (ArrayList<Game>) getArguments().getSerializable(GAMES_LIST_KEY);
        }

        listView = new ListView(getActivity());
        listView.setAdapter(this);
        rootLayout.addView(listView);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Tag your game:");

                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onNewGameButtonPressedListener != null) {
                            onNewGameButtonPressedListener.onNewGameButtonPressed(input.getText().toString());
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(games != null && currentGame != null && games.get(position).equals(currentGame)){
                    return;
                }

                if(onItemSelectedListener != null){
                    onItemSelectedListener.onItemSelected(position);
                }
            }
        });

        return rootLayout;
    }

    public void setGamesList(ArrayList<Game> games){
        if(games != null) {
            this.games = games;
            listView.invalidateViews();
        }
    }

    public void setCurrentGame(Game game){
        if(game != null){
            currentGame = game;
        }
    }

    public void setOnNewGameButtonPressedListener(OnNewGameButtonPressedListener listener){
        onNewGameButtonPressedListener = listener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        onItemSelectedListener = listener;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !games.get(position).isGameOver();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(getActivity());

        Game game = games.get(position);

        String text = game.getTag() + "\n";

        if(game.isGameOver()){
            text += "Winner: " + game.getWinnerName() + "\n";
        }

        text += "Missiles launched:\n"
                + game.getPlayer1Name() + ": " + game.getPlayer1MissilesLaunched() + "\n"
                + game.getPlayer2Name() + ": " + game.getPlayer2MissilesLaunched();

        textView.setText(text);
        textView.setTextSize(4 * getResources().getDisplayMetrics().density);

        if(currentGame != null && game.equals(currentGame)){
            textView.setBackgroundColor(Color.DKGRAY);
        }

        return textView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
