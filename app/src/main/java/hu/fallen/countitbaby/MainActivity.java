package hu.fallen.countitbaby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button mSolution1;
    Button mSolution2;
    Button mSolution3;

    Toast mSolutionToast = null;

    private class SolutionOnClickListener implements View.OnClickListener {
        private final int mNumber;

        SolutionOnClickListener(int number) {
            super();
            mNumber = number;
        }

        @Override
        public void onClick(View view) {
            if (mSolutionToast != null) mSolutionToast.cancel();
            mSolutionToast = Toast.makeText(MainActivity.this, "Button clicked: "+mNumber, Toast.LENGTH_LONG);
            mSolutionToast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSolution1 = findViewById(R.id.solution1);
        mSolution2 = findViewById(R.id.solution2);
        mSolution3 = findViewById(R.id.solution3);

        mSolution1.setOnClickListener(new SolutionOnClickListener(1));
        mSolution2.setOnClickListener(new SolutionOnClickListener(2));
        mSolution3.setOnClickListener(new SolutionOnClickListener(3));
    }
}
