package hu.fallen.countitbaby;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hu.fallen.countitbaby.helpers.Dim;
import hu.fallen.countitbaby.model.Canvas;
import hu.fallen.countitbaby.model.Controls;
import hu.fallen.countitbaby.model.Settings;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.toString();

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

    Canvas mCanvas;
    Controls mControls;

    List<Button> mSolutionButtons;

    ConstraintLayout mLayoutCanvas;
    List<ImageView> mIcons;

    int[] mImageIds = new int[] { R.drawable.ic_house,
                                  R.drawable.ic_pretzel };

    Toast mSolutionToast = null;

    private class SolutionOnClickListener implements View.OnClickListener {
        private final int mNumber;

        SolutionOnClickListener(int number) {
            super();
            mNumber = number;
        }

        @Override
        public void onClick(View view) {
            if (mCanvas.checkSolution(mNumber)) {
                showToast("Button clicked: " + mNumber + " - OK");
                mCanvas.generateQuestion();
                drawCanvas();
            } else {
                showToast("Button clicked: " + mNumber + " - try again!");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout solutionContainer = findViewById(R.id.ll_solution_container);
        int mCountSolutions = solutionContainer.getChildCount();
        mSolutionButtons = new ArrayList<>(mCountSolutions);
        for (int i = 0; i < mCountSolutions; ++i) {
            Button button = (Button) solutionContainer.getChildAt(i);
            int solution = i+1;
            mSolutionButtons.add(button);
            button.setOnClickListener(new SolutionOnClickListener(solution));
            button.setText(String.format(Locale.getDefault(),"%d", solution));
        }

        mLayoutCanvas = findViewById(R.id.cl_canvas);
        mIcons = new ArrayList<>(mCountSolutions);
        int imageViewCount = 0;
        for (int i = 0; i < mLayoutCanvas.getChildCount(); ++i) {
            View nextChild = mLayoutCanvas.getChildAt(i);
            if (!(nextChild instanceof ImageView)) continue;
            mIcons.add((ImageView) nextChild);
            imageViewCount++;
        }
        if (imageViewCount != mCountSolutions) {
            Log.e(TAG, "imageViewCount " + imageViewCount + "!= mCountSolutions " + mCountSolutions);
        }

        mLayoutCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayoutCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Dim canvasDim = new Dim(mLayoutCanvas.getWidth(), mLayoutCanvas.getHeight());
                Dim iconsDim = new Dim(mIcons.get(0).getWidth(), mIcons.get(0).getHeight());
                mCanvas = new Canvas(canvasDim, iconsDim, mImageIds.length);
                drawCanvas();
                drawButtons();
            }
        });
    }

    private void drawButtons() {
        for (int i = 0; i < mSolutionButtons.size(); ++i) {
            if (i < Settings.instance().max()) {
                mSolutionButtons.get(i).setVisibility(View.VISIBLE);
            } else {
                mSolutionButtons.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void drawCanvas() {
        for (int i = 0; i < mIcons.size(); ++i) {
            moveImage(mIcons.get(i), mCanvas.getCoordinate(i), mCanvas.getIconsDim(), mImageIds[mCanvas.getImageId()]);
        }
    }

    private void moveImage(ImageView imageView, Dim iconCoordinate, Dim iconDim, int imageId) {
        if (iconCoordinate == null) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        }

        imageView.setVisibility(View.VISIBLE);
        Log.d(TAG, "Moving icon to " + iconCoordinate);
        imageView.setPadding(iconCoordinate.getX() - iconDim.getX() / 2,
                iconCoordinate.getY() - iconDim.getY() / 2, 0, 0);
                // mCanvasWidth - (iconCoordinate.getX() + mIconWidth / 2),
                // mCanvasHeight - (iconCoordinate.getY() + mIconHeight / 2));
        imageView.setImageResource(imageId);
    }

    private void showToast(String msg) {
        if (mSolutionToast != null) mSolutionToast.cancel();
        mSolutionToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
        mSolutionToast.show();
    }
}
