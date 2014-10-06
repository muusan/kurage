package com.example.kurage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends Activity {
	Button menu, play, set, note, half, quarter, eighth, rest, half_rest, quarter_rest, eighth_rest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		menu = (Button) findViewById(R.id.menu);
		play = (Button) findViewById(R.id.play);
		set = (Button) findViewById(R.id.set);
		note = (Button) findViewById(R.id.note);
		half = (Button) findViewById(R.id.half);
		quarter = (Button) findViewById(R.id.quarter);
		eighth = (Button) findViewById(R.id.eighth);
		rest = (Button) findViewById(R.id.rest);
		half_rest = (Button) findViewById(R.id.half_rest);
		quarter_rest = (Button) findViewById(R.id.quarter_rest);
		eighth_rest = (Button) findViewById(R.id.eighth_rest);

		note.setVisibility(View.INVISIBLE);
		rest.setVisibility(View.INVISIBLE);
		play.setVisibility(View.INVISIBLE);
		set.setVisibility(View.INVISIBLE);
		half.setVisibility(View.INVISIBLE);
		quarter.setVisibility(View.INVISIBLE);
		eighth.setVisibility(View.INVISIBLE);
		half_rest.setVisibility(View.INVISIBLE);
		quarter_rest.setVisibility(View.INVISIBLE);
		eighth_rest.setVisibility(View.INVISIBLE);

	}

	public void onMenu(View v) {
		if (note.getVisibility() == View.VISIBLE) {
			note.setVisibility(View.INVISIBLE);
			rest.setVisibility(View.INVISIBLE);
			play.setVisibility(View.INVISIBLE);
			set.setVisibility(View.INVISIBLE);
			half.setVisibility(View.INVISIBLE);
			quarter.setVisibility(View.INVISIBLE);
			eighth.setVisibility(View.INVISIBLE);
			half_rest.setVisibility(View.INVISIBLE);
			quarter_rest.setVisibility(View.INVISIBLE);
			eighth_rest.setVisibility(View.INVISIBLE);
		} else if (note.getVisibility() == View.INVISIBLE) {
			note.setVisibility(View.VISIBLE);
			rest.setVisibility(View.VISIBLE);
			play.setVisibility(View.VISIBLE);
			set.setVisibility(View.VISIBLE);
		}
	}

	public void onNote(View v) {
		if (half.getVisibility() == View.VISIBLE) {
			half.setVisibility(View.INVISIBLE);
			quarter.setVisibility(View.INVISIBLE);
			eighth.setVisibility(View.INVISIBLE);
		} else if (half.getVisibility() == View.INVISIBLE) {
			half.setVisibility(View.VISIBLE);
			quarter.setVisibility(View.VISIBLE);
			eighth.setVisibility(View.VISIBLE);
		}
	}

	public void onRest(View v) {
		if (half_rest.getVisibility() == View.VISIBLE) {
			half_rest.setVisibility(View.INVISIBLE);
			quarter_rest.setVisibility(View.INVISIBLE);
			eighth_rest.setVisibility(View.INVISIBLE);
		} else if (half_rest.getVisibility() == View.INVISIBLE) {
			half_rest.setVisibility(View.VISIBLE);
			quarter_rest.setVisibility(View.VISIBLE);
			eighth_rest.setVisibility(View.VISIBLE);
		}
	}
}
