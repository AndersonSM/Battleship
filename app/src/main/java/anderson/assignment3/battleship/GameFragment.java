package anderson.assignment3.battleship;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by anderson on 10/26/15.
 */
public class GameFragment extends Fragment {
    public interface OnSpaceSelectedListener {
        void onSpaceSelected(int[] points);
    }

    int DENSITY;
    private OnSpaceSelectedListener onSpaceSelectedListener = null;
    private static final String PLAYER_GRID_KEY = "PLAYER_GRID_KEY";
    private static final String ENEMY_GRID_KEY = "ENEMY_GRID_KEY";
    private SeaView playerView;
    private SeaView enemyView;

    public static GameFragment newInstance(int[][] playerGrid, int[][] enemyGrid){
        GameFragment fragment = new GameFragment();

        Bundle arguments = new Bundle();
        if(playerGrid != null && enemyGrid != null) {
            arguments.putSerializable(PLAYER_GRID_KEY, playerGrid);
            arguments.putSerializable(ENEMY_GRID_KEY, enemyGrid);
        }
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DENSITY = (int) getResources().getDisplayMetrics().density;

        LinearLayout rootLayout = new LinearLayout(getActivity());
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout seaLayout = new LinearLayout(getActivity());
        seaLayout.setOrientation(LinearLayout.HORIZONTAL);

        playerView = new SeaView(getActivity());
        playerView.setPadding(10 * DENSITY, 5 * DENSITY, 0, 0);
        if(getArguments() != null && getArguments().containsKey(PLAYER_GRID_KEY)) {
            int[][] playerGrid = (int[][]) getArguments().getSerializable(PLAYER_GRID_KEY);
            playerView.setGrid(playerGrid);
        }
        //playerView.setBackgroundColor(Color.LTGRAY);
        seaLayout.addView(playerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2));

        enemyView = new SeaView(getActivity(), true);
        enemyView.setPadding(10 * DENSITY, 5 * DENSITY, 0, 0);
        if(getArguments() != null && getArguments().containsKey(ENEMY_GRID_KEY)) {
            int[][] enemyGrid = (int[][]) getArguments().getSerializable(ENEMY_GRID_KEY);
            enemyView.setGrid(enemyGrid);
        }
        //enemyView.setBackgroundColor(Color.CYAN);
        seaLayout.addView(enemyView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        seaLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        rootLayout.addView(seaLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        /*LinearLayout bottomLayout = new LinearLayout(getActivity());
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
        bottomLayout.setBackgroundColor(Color.LTGRAY);

        TextView yourGridText = new TextView(getActivity());
        yourGridText.setTextColor(Color.BLACK);
        yourGridText.setText("Your grid");
        yourGridText.setTextSize(5 * DENSITY);
        yourGridText.setGravity(Gravity.LEFT);
        yourGridText.setPadding(25 * DENSITY, 0, 0, 2 * DENSITY);
        bottomLayout.addView(yourGridText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        TextView enemyGridText = new TextView(getActivity());
        enemyGridText.setTextColor(Color.BLACK);
        enemyGridText.setText("Your enemy's grid");
        enemyGridText.setTextSize(5 * DENSITY);
        enemyGridText.setGravity(Gravity.RIGHT);
        enemyGridText.setPadding(0, 0, 25 * DENSITY, 2 * DENSITY);
        bottomLayout.addView(enemyGridText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        bottomLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        bottomLayout.setPadding(0,0,0,DENSITY * 4);
        rootLayout.addView(bottomLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 13));*/

        enemyView.setOnGridPointCalculatedListener(new SeaView.OnGridPointCalculatedListener() {
            @Override
            public void onGridPointCalculated(int[] points) {
                if(onSpaceSelectedListener != null){
                    onSpaceSelectedListener.onSpaceSelected(points);
                }
            }
        });

        return rootLayout;
    }

    public void setOnSpaceSelectedListener(OnSpaceSelectedListener listener){
        onSpaceSelectedListener = listener;
    }

    public void setPlayerGrids(int[][] playerGrid, int[][] enemyGrid){
        playerView.setGrid(playerGrid);
        playerView.invalidate();
        enemyView.setGrid(enemyGrid);
        enemyView.invalidate();
    }

}
