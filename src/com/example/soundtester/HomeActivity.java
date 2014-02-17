package com.example.soundtester;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class HomeActivity extends Activity {

	private Spinner freqSpinner;
	private Spinner intenSpinner;
	private Spinner durationSpinner;
	private Spinner samplesSpinner;
	private Spinner earChoiceSpinner;
	private Button playButton;

	private SineWave sinewave;

	private String [] fadedSampleList = new String[]{"441", (441 * 2) + "", (441 * 3) + "", (441 * 4) + "", (441 * 5) + ""};

	private int getFreq() {
		int freq = 125;

		int id = freqSpinner.getSelectedItemPosition();
		for (int i = 0; i < id; i++) {
			freq *= 2;
		}

		return freq;
	}

	private int getInten() {
		return intenSpinner.getSelectedItemPosition() * 5;
	}

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
		addFreqSpinner();
		addIntenSpinner();
		addDurationSpinner();
		addFadedSamplesSpinner();
		addEarSpinner();

		sinewave = new SineWave();

		playButton = (Button)findViewById(R.id.play_button);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sinewave.genTone(getFreq(), getInten(), getDuration(), getFadedSamples(), getEarChoice());
			}
		});
	}

	private void addFreqSpinner() {
		freqSpinner = (Spinner)findViewById(R.id.freq_spinner);

		String[] freqs = new String[7];
		int freq = 125;
		for(int i = 0; i < 7; i++) {
			freqs[i] = freq + " Hz";
			freq *= 2;
		}

		ArrayAdapter<String> spinnerArrayAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freqs);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		freqSpinner.setAdapter(spinnerArrayAdapter);
	}

	private void addIntenSpinner() {
		intenSpinner = (Spinner)findViewById(R.id.inten_spinner);

		String[] intens = new String[15];

		for (int i = 0; i < 15; i++) {
			intens[i] = (i * 5) + "dB";
		}

		ArrayAdapter<String> spinnerArrayAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intens);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww

		intenSpinner.setAdapter(spinnerArrayAdapter);
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
