package hu.fallen.countitbaby;

import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hu.fallen.countitbaby.helpers.Dim;
import hu.fallen.countitbaby.model.Game;
import hu.fallen.countitbaby.model.Settings;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    // TODO [Layout] Improve layout management
    // TODO [Layout]   -> dynamic placement of result buttons (maybe a grid)
    // TODO [Layout]   -> place buttons before calculating image positions (resizing the button grid affects the canvas size)
    // TODO [Layout]   -> lower limit for button size (should be easy to press) - this will be hardcoded to 5 per row for now
    // TODO [Visual] Polish
    // TODO [Visual]   -> Have a background
    // TODO [Visual]   -> Have music and sound effects for win and loose (should be possible to turn these off separately)
    // TODO [Visual]   -> Animation for win and loose
    // TODO [Visual]   -> Nicer button visuals
    // TODO [Visual]   -> Can we animate SVG?
    // TODO [Visual]   -> Moar drawings!

    Game mGame;

    List<Button> mSolutionButtons;

    List<ImageView> mImages;

    int[] mImageIds = new int[] { R.drawable.ic_rectangle,
                                  R.drawable.ic_house,
                                  R.drawable.ic_pretzel };

    Toast mSolutionToast = null;
    private LinearLayout mSolutionContainer;

    private class SolutionOnClickListener implements View.OnClickListener {
        private final int mNumber;

        SolutionOnClickListener(int number) {
            super();
            mNumber = number;
        }

        @Override
        public void onClick(View view) {
            if (mGame.checkSolution(mNumber)) {
                // TODO delay new game until sound effect plays
                MediaPlayer.create(MainActivity.this, R.raw.correct_answer_notification_01_soundeffectsplus_com).start();
                long startTime = System.currentTimeMillis();
                showToast("Button clicked: " + mNumber + " - OK");
                mGame.generateQuestion();
                drawCanvas();
                drawButtons();
                Log.d(TAG, "Answer was OK, Game Area has been redrawn in " + (System.currentTimeMillis() - startTime) + "ms");
            } else {
                MediaPlayer.create(MainActivity.this, R.raw.wrong_answer_notification_03_soundeffectsplus_com).start();
                showToast("Button clicked: " + mNumber + " - try again!");
            }
        }
    }

    public void debugSize(View v) {
        Log.d(TAG, "Canvas size: " + v.getWidth() + ", " + v.getHeight());
    }

    private static int DEBUG_COLOR = 0x00000000;
    private static int HIGHLIGHT_COLOR = 0x00000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Settings.DEBUG_MODE) {
            DEBUG_COLOR = 0x40808080;
            HIGHLIGHT_COLOR = 0x40ff0000;
        }

        mSolutionContainer = findViewById(R.id.ll_solution_container);
        mSolutionButtons = new ArrayList<>(Settings.MAXIMUM);
        for (int i = 0; i < Settings.MAXIMUM; ++i) {
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
            button.setGravity(Gravity.CENTER);
            button.setOnClickListener(new SolutionOnClickListener(i+1));
            button.setText(String.format(Locale.getDefault(),"%d", i+1));
            mSolutionContainer.addView(button);
            mSolutionButtons.add(button);
        }

        final ConstraintLayout layoutCanvas = findViewById(R.id.cl_canvas);
        mImages = new ArrayList<>(Settings.MAXIMUM);
        for (int i = 0; i < Settings.MAXIMUM; ++i) {
            ImageView image = new ImageView(this);
            image.setTag(i);
            image.setOnClickListener(new View.OnClickListener() {
                private boolean highlighted = false;
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "image clicked: " + v.getTag() + " - " + v.getPaddingLeft() + "," + v.getPaddingTop());
                    highlighted = !highlighted;
                    v.setBackgroundColor(highlighted ? HIGHLIGHT_COLOR : DEBUG_COLOR);
                }
            });
            layoutCanvas.addView(image);
            mImages.add(image);
        }

        layoutCanvas.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "Canvas has been resized to " + left + "," + top + "," + right + "," + bottom + " from " + oldLeft + "," + oldTop + "," + oldRight + "," + oldBottom
                + " (new size: " + v.getWidth() + "," + v.getHeight() + ")");
            }
        });

        layoutCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float density = getResources().getDisplayMetrics().density;
                Log.d(TAG, "density: " + density + "; canvasSize: " + layoutCanvas.getWidth() + "px x " + layoutCanvas.getHeight() + "px");
                Dim canvasDim = new Dim((int) (layoutCanvas.getWidth() / density), (int) (layoutCanvas.getHeight() / density));
                mGame = new Game(canvasDim, mImageIds.length);
                drawCanvas();
                drawButtons();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void drawButtons() {
        mSolutionContainer.removeAllViews();
        for (int i : mGame.getVisibleButtonIndexes()) {
            mSolutionContainer.addView(mSolutionButtons.get(i - 1));
        }
    }

    private void drawCanvas() {
        for (int i = 0; i < mImages.size(); ++i) {
            moveImage(mImages.get(i), mGame.getCoordinate(Settings.MAXIMUM - i - 1), mImageIds[mGame.getImageId()]);
        }
    }

    private void moveImage(ImageView imageView, Dim iconCoordinate, int imageId) {
        if (iconCoordinate == null) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        }

        imageView.setVisibility(View.VISIBLE);
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconCoordinate.X(), getResources().getDisplayMetrics());
        int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconCoordinate.Y(), getResources().getDisplayMetrics());
        imageView.setPadding(left, top,0,0);
        imageView.setImageResource(imageId);
        imageView.setBackgroundColor(DEBUG_COLOR);
        imageView.setTag(iconCoordinate.tag);
    }

    private void showToast(String msg) {
        if (!Settings.DEBUG_MODE) return;
        if (mSolutionToast != null) mSolutionToast.cancel();
        mSolutionToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
        mSolutionToast.show();
    }
}
