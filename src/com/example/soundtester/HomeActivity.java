package com.example.soundtester;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class HomeActivity extends Activity {
	private Spinner durationSpinner;
	private Spinner samplesSpinner;
	private Spinner earChoiceSpinner;
	private Button playButton;
	LinearLayout graph;

	private SineWave sinewave;

	private String [] fadedSampleList = new String[]{"441", (441 * 2) + "", (441 * 3) + "", (441 * 4) + "", (441 * 5) + ""};

	private int getDuration() {
		return durationSpinner.getSelectedItemPosition() + 1;
	}

	private int getFadedSamples() {
		return Integer.parseInt(fadedSampleList[samplesSpinner.getSelectedItemPosition()]);
	}

	private boolean getEarChoice() {
		return earChoiceSpinner.getSelectedItemPosition() == 0;
	}

	private void init() {
		addDurationSpinner();
		addFadedSamplesSpinner();
		addEarSpinner();

		graph = (LinearLayout)findViewById(R.id.graph1);
		sinewave = new SineWave();

		playButton = (Button)findViewById(R.id.play_button);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.e("Sample number", "" + getFadedSamples());
				sinewave.genTone(1000, 60, getDuration(), getFadedSamples(), getEarChoice());
			
				View graphView = sinewave.drawGraph(getApplicationContext());
				graph.removeAllViews();
				graph.addView(graphView);
			}
		});
		
		
	}

	private void addDurationSpinner() {
		durationSpinner = (Spinner)findViewById(R.id.duration);

		String[] freqs = new String[]{"1", "2", "3", "4", "5"};

		ArrayAdapter<String> spinnerArrayAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freqs);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		durationSpinner.setAdapter(spinnerArrayAdapter);
	}

	private void addFadedSamplesSpinner() {
		samplesSpinner = (Spinner)findViewById(R.id.num_of_faded_samples);

		ArrayAdapter<String> spinnerArrayAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fadedSampleList);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		samplesSpinner.setAdapter(spinnerArrayAdapter);
	}

	private void addEarSpinner() {
		earChoiceSpinner = (Spinner)findViewById(R.id.ear_choice);

		String[] ears = new String[]{"Kiri", "Kanan"};

		ArrayAdapter<String> spinnerArrayAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ears);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		earChoiceSpinner.setAdapter(spinnerArrayAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
