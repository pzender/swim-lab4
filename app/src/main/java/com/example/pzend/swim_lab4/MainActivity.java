package com.example.pzend.swim_lab4;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.StringCharacterIterator;

public class MainActivity extends AppCompatActivity {
    SensorManager sMgr;
    Sensor acc;
    TextView val;
    TextView sides_info;
    Button reset;
    double THRESHOLD = 1.3;
    int DELAY = 1000000;
    int rolledValue = 1;
    int diceSet = 20;
    ImageView diceDrawing;
    Toolbar myToolbar;
    public static final double G_FORCE = 9.81;
    DecimalFormat df = new DecimalFormat("#0");
    Vibrator v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        val = (TextView)findViewById(R.id.rolled);
        sMgr.registerListener(new SensorListener(), acc, DELAY);
        df.setRoundingMode(RoundingMode.HALF_UP);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        diceDrawing = (ImageView)findViewById(R.id.dice);
        sides_info = (TextView)findViewById(R.id.sides_info);
        sides_info.setText(buildSidesInfo(diceSet));
        v = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    class SensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float acc_x = sensorEvent.values[0];
            float acc_y = sensorEvent.values[1];
            float acc_z = sensorEvent.values[2];
            double acc_total = Math.sqrt(acc_x*acc_x + acc_y*acc_y + acc_z*acc_z)/G_FORCE;
            if (acc_total > THRESHOLD) {
                val.setText("");
                if (diceSet > 0)
                    rolledValue = new Dice(diceSet).roll();
                    v.vibrate(100);
            }
            else {
                val.setText(""+rolledValue);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        rolledValue = 1;
        switch (item.getItemId()) {
            case R.id.d_4:
                diceSet = 4;
                diceDrawing.setImageResource(R.drawable.d4);
                break;
            case R.id.d_6:
                diceSet = 6;
                diceDrawing.setImageResource(R.drawable.d6);
                break;
            case R.id.d_8:
                diceSet = 8;
                diceDrawing.setImageResource(R.drawable.d8);
                break;
            case R.id.d_10:
                diceSet = 10;
                diceDrawing.setImageResource(R.drawable.d10);
                break;
            case R.id.d_12:
                diceSet = 12;
                diceDrawing.setImageResource(R.drawable.d12);
                break;
            case R.id.d_20:
                diceSet = 20;
                diceDrawing.setImageResource(R.drawable.d20);
                break;
            case R.id.other:
                setDiceToValFromDialog();
                diceDrawing.setImageResource(R.drawable.d20);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        sides_info.setText(buildSidesInfo(diceSet));
        return true;
    }

    private void setDiceToValFromDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Roll a custom dice: ");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                String s = input.getText().toString();
                diceSet = Integer.valueOf(s);
                sides_info.setText(buildSidesInfo(diceSet));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diceSet = 0;
                dialog.cancel();
            }
        });

        builder.show();
    }
    private String buildSidesInfo(int nOfSides){
        return String.format("Rolling a %d - sided die!", nOfSides);
    }
}
