package hu.fallen.countitbaby;

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
    // TODO [Layout]   -> lower limit for button size (should be easy to press)
    // TODO [Visual] Polish
    // TODO [Visual]   -> Have a background
    // TODO [Visual]   -> Have music and sound effects for win and loose (should be possible to turn these off separately)
    // TODO [Visual]   -> Animation for win and loose
    // TODO [Visual]   -> Full-screen (sticky immersion) mode
    // TODO [Visual]   -> Nicer button visuals
    // TODO [Visual]   -> Can we animate SVG?
    // TODO [Visual]   -> Moar drawings!

    Game mGame;

    List<Button> mSolutionButtons;

    List<ImageView> mImages;

    int[] mImageIds = new int[] { R.drawable.ic_house,
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
                showToast("Button clicked: " + mNumber + " - OK");
                mGame.generateQuestion();
                drawCanvas();
                drawButtons();
            } else {
                showToast("Button clicked: " + mNumber + " - try again!");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            image.setBackgroundColor(0x11111111);
            layoutCanvas.addView(image);
            mImages.add(image);
        }

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

    private void drawButtons() {
        mSolutionContainer.removeAllViews();
        for (int i = 0; i < mSolutionButtons.size(); ++i) {
            if (mGame.isButtonVisible(i)) {
                mSolutionContainer.addView(mSolutionButtons.get(i));
            }
        }
    }

    private void drawCanvas() {
        for (int i = 0; i < mImages.size(); ++i) {
            moveImage(mImages.get(i), mGame.getCoordinate(i), mImageIds[mGame.getImageId()]);
        }
    }

    private void moveImage(ImageView imageView, Dim iconCoordinate, int imageId) {
        if (iconCoordinate == null) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        }

        imageView.setVisibility(View.VISIBLE);
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconCoordinate.getX(), getResources().getDisplayMetrics());
        int top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconCoordinate.getY(), getResources().getDisplayMetrics());
        imageView.setPadding(left, top,0,0);
        imageView.setImageResource(imageId);
    }

    private void showToast(String msg) {
        if (mSolutionToast != null) mSolutionToast.cancel();
        mSolutionToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
        mSolutionToast.show();
    }
}
