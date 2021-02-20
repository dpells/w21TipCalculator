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
    implements TextView.OnEditorActionListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, View.OnKeyListener, RadioGroup.OnCheckedChangeListener {

    //sets up member variables that will be used in multiple methods
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

        // set up the tipPercent TextView so we can change it
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
        //3. add the tip amount to the bill amount
        //4. update the Views with the correct information


        //set up the EditText so we can get the values from it
        billAmountEditText = findViewById(R.id.billAmountEditText);
        billAmountEditText.setOnEditorActionListener(this); // for when we press "done" or the Action button
        billAmountEditText.setOnKeyListener(this); // for when we pres Enter

        // add event listeners to the Buttons
        decButton = findViewById(R.id.tipPercentDec);
        decButton.setOnClickListener(this);
        incButton = findViewById(R.id.tipPercentInc);
        incButton.setOnClickListener(this);

        // add an event listener to the SeekBar
        percentBar = findViewById(R.id.percentSeekBar);
        percentBar.setOnSeekBarChangeListener(this);

        // add an event listener to the RadioGroup
        numPeopleGroup = findViewById(R.id.numberOfPeopleGroup);
        numPeopleGroup.setOnCheckedChangeListener(this);

        numPeople = 2; // what is the better way to get the "default" number of people?
    }


    /**
     * Collects data and updates the display when Views are interacted with
     */
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

        // update the total amount
        TextView totalAmountTextView = findViewById(R.id.totalAmount);
        totalAmountTextView.setText(currency.format(totalAmount));

        // update the total per person amount
        TextView totalPerPersonTextView = findViewById(R.id.totalPerPersonAmount);
        totalPerPersonTextView.setText(currency.format(totalPerPerson));
    }


    @Override
    /**
     * Listens for the Action Button press and calls the update method.
     *
     * @see     android.widget.TextView.OnEditorActionListener
     * @see     EditorInfo
     */
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
    /**
     * Listens for click events, only performing a meaningful action when the buttons are clicked.
     *
     * @see     android.view.View.OnClickListener
     */
    public void onClick(View view) {
        // which View was clicked?
        switch(view.getId()){
            case R.id.tipPercentDec:
                if(tipPercent > 0) { // only decrease the tip if it's greater than 0
                    tipPercent -= 0.05f;
                }
                break;
            case R.id.tipPercentInc:
                if(tipPercent < 1) { // only increase the tip if it's less  than 1 (100%)
                    tipPercent += 0.05f;
                }
                break;
            default:
                Toast.makeText(this, "You clicked a TextView", Toast.LENGTH_LONG).show();
                break;
        }

        // re-bind the tip between 0 and 1 (100%)
        if(tipPercent > 1) tipPercent = 1f;
        if(tipPercent < 0) tipPercent = 0f;

        // update the seek bar whenever we press a button
        percentBar.setProgress((int)(tipPercent * 100));

        update();
    }

    @Override
    /**
     * Listens for SeekBar changes. In this case we aren't interest in the start and stop, only
     * the continuous update
     *
     * @see     android.widget.SeekBar.OnSeekBarChangeListener
     */
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            tipPercent = progress / 100f;

            update();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { /*auto-gen. Nothing to do*/ }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { /*auto-gen. Nothing to do*/ }

    @Override
    /**
     * Listens for key events. In this case we're only listening for the Enter key for the
     * EditText
     *
     * @see     android.view.View.OnKeyListener
     * @see     KeyEvent
     */
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(billAmountEditText.getWindowToken(), 0);
        }
        return false;
    }

    @Override
    /**
     * Listens for changes to the RadioGroup
     *
     * @see     android.widget.RadioGroup.OnCheckedChangeListener
     */
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        // when the selection is changed, make sure the section for total per person is visible
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
                //when only one person is selected, remove the total per person
                layout.setVisibility(View.GONE);
                break;
        }

        update();
    }
}
