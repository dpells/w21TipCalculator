package ca.davidpellegrini.w21tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
    implements TextView.OnEditorActionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        View.OnKeyListener, RadioGroup.OnCheckedChangeListener {

    private Button decButton, incButton;
    private TextView tipPercentTextView;
    private EditText billAmountEditText;
    private SeekBar percentBar;
    private RadioGroup numPeopleGroup;
    private int numPeople;
    private float tipPercent, billAmount, tipAmount, totalAmount, totalPerPerson;
    private String billAmountString;
    private NumberFormat percent = NumberFormat.getPercentInstance();
    private NumberFormat currency = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //method 1
            //set up the variables
            EditText et = findViewById()...
            TextView tv1 = findViewById()...
            TextView tv2 = findViewById()...

            //set up listeners
            et.setOnEditorActionListener()...
            tv1.setOnClickListener()...
            ...
         */

         /*
         //method 2
            // set up EditTexts
            EditText et = findViewById()...
            et.setOnEditorActionListener()...

            // set up TextViews
            TextView tv1 = findViewById()...
            tv1.setOnClickListener()...
            TextView tv2 = findViewById()...

            // set up buttons
            ...
         */

        tipPercent = 0.2f;
        tipPercentTextView = findViewById(R.id.tipPercent);
        tipPercentTextView.setText(percent.format(tipPercent));
        tipPercentTextView.setOnClickListener(this);

        TextView billAmountLabel = findViewById(R.id.billAmountLabel);
        billAmountLabel.setOnClickListener(this);
//        String billAmountLabelText = billAmountLabel.getText().toString();
//        billAmountLabel.setText(billAmountLabelText);

        // need to: calculate total amount

        // how?
        //1. get text from EditText - convert to float
        //x. get the tip percentage (already stored in the default)
        //2. multiply the bill amount by the tip percentage

        // things left
            // seek bar
            //x      change the tip % using the seek bar
            //xx      update seekbar with buttons AND arrow keys**
            //x close the keyboard when we press enter
            //x radio buttons
            // update as we type
            //x hide total per person if there is only 1
            // styles


        billAmountEditText = findViewById(R.id.billAmountEditText);
        billAmountEditText.setOnEditorActionListener(this);
        billAmountEditText.setOnKeyListener(this);

        decButton = findViewById(R.id.tipPercentDec);
        decButton.setOnClickListener(this);
        incButton = findViewById(R.id.tipPercentInc);
        incButton.setOnClickListener(this);

        percentBar = findViewById(R.id.percentSeekBar);
        percentBar.setOnSeekBarChangeListener(this);

        numPeopleGroup = findViewById(R.id.numberOfPeopleGroup);
        numPeopleGroup.setOnCheckedChangeListener(this);

        numPeople = 2;
    }


    public void update(){
        // get the data from the EditText
        billAmountString = billAmountEditText.getText().toString();
        try {
            billAmount = Float.parseFloat(billAmountString);
        }
        catch(Exception e){
            billAmount = 0;
        }
        // calculate results
        tipAmount = billAmount * tipPercent + 0.05f;
        totalAmount = tipAmount + billAmount;
        totalPerPerson = totalAmount / numPeople;

        //update the tip percent
        tipPercentTextView.setText(percent.format(tipPercent));

        // update the tip amount
        TextView tipAmountTextView = findViewById(R.id.tipAmount);
        tipAmountTextView.setText(currency.format(tipAmount));

        TextView totalAmountTextView = findViewById(R.id.totalAmount);
        totalAmountTextView.setText(currency.format(totalAmount));

        TextView totalPerPersonTextView = findViewById(R.id.totalPerPersonAmount);
        totalPerPersonTextView.setText(currency.format(totalPerPerson));
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        switch(actionId){
            case EditorInfo.IME_ACTION_DONE:
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                update();
                break;
        }
        return false; // return false hides the soft keyboard
        //return true; // return true keeps the keyboard available
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tipPercentDec:
                if(tipPercent > 0) {
                    tipPercent -= 0.05f;
                }
                break;
            case R.id.tipPercentInc:
                if(tipPercent < 1) {
                    tipPercent += 0.05f;
                }
                break;
            default:
                Toast.makeText(this, "You clicked a TextView", Toast.LENGTH_LONG).show();
                break;
        }

        if(tipPercent > 1) tipPercent = 1f;
        if(tipPercent < 0) tipPercent = 0f;

        // update the seek bar whenever we press a button
        percentBar.setProgress((int)(tipPercent * 100));

        update();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            tipPercent = progress / 100f;

            update();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(billAmountEditText.getWindowToken(), 0);
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        LinearLayout layout = findViewById(R.id.numberOfPeopleLayout);
        layout.setVisibility(View.VISIBLE);

        switch(checkedId){
            case R.id.twoPeopleRB:
                numPeople = 2;
                break;
            case R.id.threePeopleRB:
                numPeople = 3;
                break;
            case R.id.fourPeopleRB:
                numPeople = 4;
                break;
            case R.id.onePersonRB:
            default:
                numPeople = 1;

                layout.setVisibility(View.GONE);
                break;
        }

        update();
    }
}
