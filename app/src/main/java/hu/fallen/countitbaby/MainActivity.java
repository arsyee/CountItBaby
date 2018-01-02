package hu.fallen.countitbaby;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
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

    // TODO [Experience] Add more game modes
    // TODO [Experience]   -> Add a counter (collect bronze-silver-gold-diamond coins in the top row)
    // TODO [Experience]   -> Different effects when 5 coins collected
    // TODO [Settings]   -> Auto-advance on 5 coins (increment number of possibilities)
    // TODO [Layout] Improve layout management
    // TODO [Layout]   -> dynamic placement of result buttons (maybe a grid)
    // TODO [Layout]   -> place buttons before calculating image positions (resizing the button grid affects the canvas size)
    // TODO [Layout]   -> lower limit for button size (should be easy to press) - this will be hardcoded to 5 per row for now
    // TODO [Visual] Polish
    // TODO [Visual]   -> Have a background
    // TODO [Visual]   -> Animation for win and loose
    // TODO [Visual]   -> Nicer button visuals
    // TODO [Visual]   -> Can we animate SVG?
    // TODO [Visual]   -> Moar drawings!

    Game mGame;

    private Toast mSolutionToast = null;
    private LinearLayout mSolutionContainer;
    private List<Button> mSolutionButtons;
    private List<ImageView> mImages;

    private int[] mImageIds = new int[] { R.drawable.ic_rectangle,
                                          R.drawable.ic_house,
                                          R.drawable.ic_pretzel };

    // TODO How to separate out DEBUG code like Debug and Release build?
    private static int DEBUG_COLOR = 0x00000000;
    private static int HIGHLIGHT_COLOR = 0x00000000;

    // Life-cycle handlers

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
            Button button = createSolutionButton(i);
            mSolutionContainer.addView(button);
            mSolutionButtons.add(button);
        }

        final ConstraintLayout layoutCanvas = findViewById(R.id.cl_canvas);
        mImages = new ArrayList<>(Settings.MAXIMUM);
        for (int i = 0; i < Settings.MAXIMUM; ++i) {
            ImageView image = createImage(i);
            layoutCanvas.addView(image);
            mImages.add(image);
        }
        layoutCanvas.addOnLayoutChangeListener(new CanvasOnLayoutChangeListener());
        layoutCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new CanvasOnGlobalLayoutListener(layoutCanvas));
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

    // Event handlers

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
                playSound(R.raw.correct_answer_notification_01_soundeffectsplus_com);
                long startTime = System.currentTimeMillis();
                showToast(String.format("Button clicked: %d - OK", mNumber));
                mGame.generateQuestion();
                drawCanvas();
                drawButtons();
                Log.i(TAG, String.format("Answer (%d) OK, Game Area has been redrawn in %dms", mNumber, (System.currentTimeMillis() - startTime)));
            } else {
                playSound(R.raw.wrong_answer_notification_03_soundeffectsplus_com);
                showToast(String.format("Button clicked: %d - try again!", mNumber));
            }
        }
    }

    private class CanvasOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final ViewGroup mViewGroup;

        private CanvasOnGlobalLayoutListener(ViewGroup viewGroup) {
            mViewGroup = viewGroup;
        }

        @Override
        public void onGlobalLayout() {
            mViewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            float density = getResources().getDisplayMetrics().density;
            Log.d(TAG, String.format("density: %f; canvasSize: %dpx,%dpx", density, mViewGroup.getWidth(), mViewGroup.getHeight()));
            Dim canvasDim = new Dim((int) (mViewGroup.getWidth() / density), (int) (mViewGroup.getHeight() / density));
            mGame = new Game(canvasDim, mImageIds.length);
            drawCanvas();
            drawButtons();
        }
    }

    private class CanvasOnLayoutChangeListener implements View.OnLayoutChangeListener {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            Log.d(TAG, String.format("Canvas has been resized to %d,%d,%d,%d from %d,%d,%d,%d - new size: %d,%d",
                    left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom, v.getWidth(), v.getHeight()));
            // TODO resize Model's Canvas when this happens
            // what's the difference between these two listeners and why am I using the other?
        }
    }

    private class ImageOnClickListener implements View.OnClickListener {
        private boolean highlighted = false;

        @Override
        public void onClick(View v) {
            Log.d(TAG, String.format("image clicked: %s, padding: %d,%d", v.getTag(), v.getPaddingLeft(), v.getPaddingTop()));
            highlighted = !highlighted;
            v.setBackgroundColor(highlighted ? HIGHLIGHT_COLOR : DEBUG_COLOR);
        }
    }

    public void canvasOnClick(View v) {
        Log.d(TAG, String.format("Canvas size: %d,%d", v.getWidth(), v.getHeight()));
    }

    // Helper functions

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

    private void playSound(int soundResource) {
        // TODO Create two AudioPlayers to play Music and FX separately? see also: https://stackoverflow.com/a/18255129/1409960
        // TODO will the music continue to play if I navigate to another Activity?
        if (Settings.MUSIC_ON) {
            MediaPlayer.create(MainActivity.this, soundResource).start();
        }
    }

    private void showToast(String msg) {
        if (!Settings.DEBUG_MODE) return;
        if (mSolutionToast != null) mSolutionToast.cancel();
        mSolutionToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
        mSolutionToast.show();
    }

    @NonNull
    private Button createSolutionButton(int i) {
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new SolutionOnClickListener(i+1));
        button.setText(String.format("%d", i+1));
        return button;
    }

    @NonNull
    private ImageView createImage(int i) {
        ImageView image = new ImageView(this);
        image.setTag(i);
        image.setOnClickListener(new ImageOnClickListener());
        return image;
    }

}
