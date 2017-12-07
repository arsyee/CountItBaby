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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.toString();
    public Random mRandom = new Random();

    TextView mQuestion;

    LinearLayout mSolutionContainer;
    int mCountSolutions;
    List<Button> mSolutionButtons;

    ConstraintLayout mCanvas;
    int mCanvasWidth;
    int mCanvasHeight;
    List<ImageView> mIcons;
    int mIconWidth;
    int mIconHeight;

    Toast mSolutionToast = null;

    int numberOfOptions = 5;

    private class SolutionOnClickListener implements View.OnClickListener {
        private final int mNumber;

        SolutionOnClickListener(int number) {
            super();
            mNumber = number;
        }

        @Override
        public void onClick(View view) {
            if (mSolutionToast != null) mSolutionToast.cancel();
            boolean solutionOK = Integer.toString(mNumber).equals(mQuestion.getText().toString());
            StringBuilder toastText = new StringBuilder("Button clicked: ").append(mNumber);
            if (solutionOK) {
                toastText.append(" - OK");
            } else {
                toastText.append(" - try again!");
            }
            mSolutionToast = Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_LONG);
            mSolutionToast.show();
            if (solutionOK) newRandomQuestion();
        }
    }

    private void newRandomQuestion() {
        int newSolution = mRandom.nextInt(numberOfOptions)+1;
        mQuestion.setText(String.format(Locale.getDefault(),"%d", newSolution));

        for (int i = 0; i < mIcons.size(); ++i) {
            if (i < newSolution) {
                mIcons.get(i).setVisibility(View.VISIBLE);
                int iconCenterX = mRandom.nextInt(mCanvasWidth);
                int iconCenterY = mRandom.nextInt(mCanvasHeight);
                Log.d(TAG, "Moving icon " + (i+1) + " to " + iconCenterX + "," + iconCenterY);
                mIcons.get(i).setPadding(iconCenterX - mIconWidth / 2,
                        iconCenterY - mIconHeight / 2,
                        mCanvasWidth - (iconCenterX + mIconWidth / 2),
                        mCanvasHeight - (iconCenterY + mIconHeight / 2));
            } else {
                mIcons.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestion = findViewById(R.id.question);

        mSolutionContainer = findViewById(R.id.ll_solution_container);
        mCountSolutions = mSolutionContainer.getChildCount();
        mSolutionButtons = new ArrayList<>(mCountSolutions);
        for (int i = 0; i < mCountSolutions; ++i) {
            Button button = (Button) mSolutionContainer.getChildAt(i);
            int solution = i+1;
            mSolutionButtons.add(button);
            button.setOnClickListener(new SolutionOnClickListener(solution));
            button.setText(String.format(Locale.getDefault(),"%d", solution));
            if (solution > numberOfOptions) {
                button.setVisibility(View.GONE);
            }
        }

        mCanvas = findViewById(R.id.cl_canvas);
        mIcons = new ArrayList<>(mCountSolutions);
        int imageViewCount = 0;
        for (int i = 0; i < mCanvas.getChildCount(); ++i) {
            View nextChild = mCanvas.getChildAt(i);
            if (!(nextChild instanceof ImageView)) continue;
            mIcons.add((ImageView) nextChild);
            imageViewCount++;
        }
        if (imageViewCount != mCountSolutions) {
            Log.e(TAG, "imageViewCount " + imageViewCount + "!= mCountSolutions " + mCountSolutions);
        }

        mCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mCanvasWidth = mCanvas.getWidth();
                mCanvasHeight = mCanvas.getHeight();
                Log.d(TAG, "Canvas dimensions: " + mCanvasWidth + "x" + mCanvasHeight);
                mIconWidth = mIcons.get(0).getWidth();
                mIconHeight = mIcons.get(0).getHeight();
                Log.d(TAG, "Icon dimensions: " + mIconWidth + "x" + mIconHeight);
                newRandomQuestion();
            }
        });
    }
}
