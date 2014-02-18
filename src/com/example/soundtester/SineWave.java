package com.example.soundtester;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class SineWave {
	/** Bagian audio */

	int intenData1[] = {2, 4, 6, 11, 20, 36, 64, 114, 203, 362, 646, 1150, 2049, 3651, 6504, 11588, 20645}; 
	int intenData2[] = {4, 7, 13, 23, 40, 72, 128, 228, 407, 725, 1291, 2300, 4098, 7301, 13007, 23172};
	int intenData3[] = {18, 32, 57, 102, 181, 323, 575, 1025, 1826, 3253, 5795, 10324, 18393};

	private boolean first = true;
	private int duration = 1; // seconds
	private int sampleRate = 44100; //Hz
	private int numSamples = (int) (duration *  sampleRate);
	private int numOfFadedSample = numSamples / 100;
	private double deltaCoef = 1.00 / (numOfFadedSample - 1.00);
	private double sample[] = new double[numSamples];
	private double sampleVal[] = new double[numSamples];
	private byte generatedSnd[] = new byte[2 * numSamples];
	private AudioTrack audioTrack;

	private boolean partOfFadeInSample(int i) {
		return i <= numOfFadedSample;
	}

	private boolean partOfFadeOutSample(int i) {
		return i + numOfFadedSample > numSamples;
	}

	public View drawGraph(Context context) {
		// draw sin curve
		GraphViewData[] data = new GraphViewData[numSamples];
		double v=0;
		for (int i=0; i < numSamples; i++) {
			data[i] = new GraphViewData(i, sampleVal[i]);
		}
		// graph with dynamically genereated horizontal and vertical labels
		GraphView graphView = new LineGraphView(context, "GraphViewDemo");

		// add data
		graphView.addSeries(new GraphViewSeries(data));
		// set view port, start=2, size=40
		graphView.setViewPort(0, 1000);
		graphView.setScrollable(true);
		return graphView;
	}

	public void genTone(int n_freq, int inten, int duration, int numOfFadedSample, boolean leftEar) {
		numSamples = (int) (duration *  sampleRate);
		this.numOfFadedSample = numOfFadedSample;
		deltaCoef = 1.00 / (numOfFadedSample - 1.00);
		sample = new double[numSamples];
		sampleVal = new double[numSamples];
		generatedSnd = new byte[2 * numSamples];
		
		Log.e("Num of faded sample", "" + numOfFadedSample);

		int n_inten, idInten = inten / 5;

		if (n_freq == 125) {
			n_inten = intenData3[idInten];
		}
		else if (n_freq == 250 || n_freq == 8000) {
			n_inten = intenData2[idInten];
		}
		else {
			n_inten = intenData1[idInten];
		}

		n_freq /= 2;

		for (int i = 0; i < numSamples; ++i) {
			sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/n_freq));
		}

		int idx = 0;
		int id = 1;
		double fadeInCoef = 0.00, fadeOutCoef = 1.00;
		for (final double dVal : sample) {
			short val = (short) ((dVal * n_inten));
			if (partOfFadeInSample(id)) {
				val *= fadeInCoef;
				fadeInCoef += deltaCoef;
			}
			else if (partOfFadeOutSample(id)) {
				val *= fadeOutCoef;
				fadeOutCoef -= deltaCoef;
			}
			sampleVal[id - 1] = val;
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
			id++;
		}

		playSound(leftEar);
	}

	void playSound(boolean leftEar){
		if(!first)
			audioTrack.release();
		else
			first = false;
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRate, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, numSamples,
				AudioTrack.MODE_STATIC);

		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		if(leftEar == true) {
			audioTrack.setStereoVolume(1.0f, 0.0f);
		} else {
			audioTrack.setStereoVolume(0.0f, 1.0f);
		}
		try {
			audioTrack.play();
		} catch(Exception e) {}
	}
}
