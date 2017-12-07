package hu.fallen.countitbaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    Toast mSolutionToast = null;

    int numberOfOptions = 9;

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

        newRandomQuestion();
    }
}
