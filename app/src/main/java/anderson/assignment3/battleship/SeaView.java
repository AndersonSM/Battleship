package anderson.assignment3.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import anderson.assignment3.battleship.models.Player;

/**
 * Created by anderson on 10/23/15.
 */
public class SeaView extends View {
    public interface OnGridPointCalculatedListener {
        void onGridPointCalculated(int[] points);
    }

    private final float DENSITY = getResources().getDisplayMetrics().density;
    private OnGridPointCalculatedListener onGridPointCalculatedListener = null;
    private int viewLeftPoint, viewTopPoint, viewRightPoint, viewBottomPoint, sqrOffset;
    private boolean isEnemy = false;
    private int[][] grid;

    public SeaView(Context context){
        super(context);
        grid = new int[10][10];
    }

    public SeaView(Context context, boolean isEnemy){
        this(context);
        this.isEnemy = isEnemy;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            float x = event.getX();
            float y = event.getY();

            if(x > viewRightPoint || x < viewLeftPoint || y < viewTopPoint || y > viewBottomPoint){
                return true;
            }

            int[] points = calculateGridSpace(x, y);
            if(onGridPointCalculatedListener != null) {
                onGridPointCalculatedListener.onGridPointCalculated(points);
            }
        }

        return true;
    }

    private int[] calculateGridSpace(float x, float y){
        // result[0] -> x | result[1] ->  y
        int[] result = new int[2];

        for (int i = 0; i < 10; i++) {
            if(viewLeftPoint + (sqrOffset * i) < x){
                result[0] = i;
            }
            if(viewTopPoint + (sqrOffset * i) < y){
                result[1] = i;
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintFill = new Paint();
        paintFill.setColor(Color.BLUE);
        paintFill.setStyle(Paint.Style.FILL);

        Paint paintStroke = new Paint();
        paintStroke.setAntiAlias(true);
        paintStroke.setColor(Color.BLACK);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setStrokeWidth(1.5f * DENSITY);

        RectF outRect = new RectF();
        int minVal = Math.min(getWidth() - getPaddingRight() - (int)(13 * DENSITY), getHeight() - getPaddingBottom() - (int)(13 * DENSITY));
        int outLeft, outTop, outRight, outBottom;
        outLeft = getPaddingLeft();
        outTop = getPaddingTop();
        outRight = minVal;
        outBottom = getPaddingTop() + minVal - getPaddingLeft();
        outRect.set(outLeft, outTop, outRight, outBottom);

        viewLeftPoint = outLeft;
        viewTopPoint = outTop;
        viewRightPoint = outRight;
        viewBottomPoint = outBottom;

        canvas.drawRect(outRect, paintStroke);

        int sqrOffset = (outRight - outLeft) / 10;
        this.sqrOffset = sqrOffset;
        RectF gridSqr = new RectF();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridSqr.set(outLeft + (sqrOffset * i), outTop + (sqrOffset * j),
                        outLeft + (sqrOffset * (i + 1)), outTop + (sqrOffset * (j + 1)));
                canvas.drawRect(gridSqr, paintStroke);
                if(isEnemy) {
                    switch (grid[i][j]) {
                        case Player.EMPTY:
                            paintFill.setColor(Color.BLUE);
                            break;
                        case Player.SHIP:
                            paintFill.setColor(Color.BLUE);
                            break;
                        case Player.MISS:
                            paintFill.setColor(Color.LTGRAY);
                            break;
                        case Player.HIT:
                            paintFill.setColor(Color.RED);
                            break;
                    }
                } else {
                    switch (grid[i][j]) {
                        case Player.EMPTY:
                            paintFill.setColor(Color.BLUE);
                            break;
                        case Player.SHIP:
                            paintFill.setColor(Color.DKGRAY);
                            break;
                        case Player.MISS:
                            paintFill.setColor(Color.LTGRAY);
                            break;
                        case Player.HIT:
                            paintFill.setColor(Color.RED);
                            break;
                    }
                }
                canvas.drawRect(gridSqr, paintFill);
            }
        }

        paintFill.setTextSize(12 * DENSITY);
        paintFill.setColor(Color.WHITE);

        if(isEnemy) {
            canvas.drawText("Your enemy's grid", outLeft + (15 * DENSITY), outBottom + (12 * DENSITY), paintFill);
        } else {
            canvas.drawText("Your grid", outLeft + (15 * DENSITY), outBottom + (12 * DENSITY), paintFill);
        }
    }

    public void setOnGridPointCalculatedListener(OnGridPointCalculatedListener listener){
        onGridPointCalculatedListener = listener;
    }

    public OnGridPointCalculatedListener getOnGridPointCalculatedListener(){
        return onGridPointCalculatedListener;
    }

    public void setGrid(int[][] grid){
        this.grid = grid;
    }
}
