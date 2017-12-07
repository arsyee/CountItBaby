package hu.fallen.countitbaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public Random mRandom = new Random();

    TextView mQuestion;

    Button mSolution1;
    Button mSolution2;
    Button mSolution3;

    Toast mSolutionToast = null;

    int numberOfOptions = 3;

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
        int newSolution = mRandom.nextInt(3)+1;
        mQuestion.setText(Integer.toString(newSolution));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestion = findViewById(R.id.question);

        mSolution1 = findViewById(R.id.solution1);
        mSolution2 = findViewById(R.id.solution2);
        mSolution3 = findViewById(R.id.solution3);

        mSolution1.setOnClickListener(new SolutionOnClickListener(1));
        mSolution2.setOnClickListener(new SolutionOnClickListener(2));
        mSolution3.setOnClickListener(new SolutionOnClickListener(3));

        newRandomQuestion();
    }
}
