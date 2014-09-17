package com.example.kurage;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Rhythm implements OnCompletionListener {
	private MediaPlayer shibu, hatibu, nibu, zen;
	private int soundId;
	int count = 0;
	int ope = 0;
	int[][] array;

	int i, j;

	public Rhythm(Context context) {
		shibu = MediaPlayer.create(context, R.raw.shibu);
		hatibu = MediaPlayer.create(context, R.raw.hatibu);
		nibu = MediaPlayer.create(context, R.raw.nibu);
		zen = MediaPlayer.create(context, R.raw.nibu);
		shibu.setOnCompletionListener(this);
		hatibu.setOnCompletionListener(this);
		nibu.setOnCompletionListener(this);
		zen.setOnCompletionListener(this);
	}

	/*
	 * public void repeatPlay(int repeat) {
	 * 
	 * while (count < repeat) { play(); count++; try { Thread.sleep(500); //
	 * 500ミリ秒待つ } catch (InterruptedException e) { e.printStackTrace(); } }
	 * 
	 * }
	 */

	public void rhythmPlay(int[][] opeArray) {

		array = opeArray;

		i = j = 0;
		if (opeArray[i + 1][j] == 4) {// 四分音符
			shibu.start();
		} else if (opeArray[i + 1][j] == 2) {// 八分音符
			hatibu.start();
		} else if (opeArray[i + 1][j] == 8) {// 二分音符
			nibu.start();
		} else if (opeArray[i + 1][j] == 16) {// 全音符
			zen.start();
		}

		//
		// for (int i = 0; i < opeArray.length; i++) {
		// for (int j = 0; j < opeArray[i].length; j++) {
		// try {
		// if (opeArray[i][j] == 4) {
		// shibu.start();
		// Thread.sleep(100);
		// // 100�~���b�҂�
		// } else if (opeArray[i][j] == 2) {
		// hatibu.start();
		// Thread.sleep(250);
		// }
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		// MediaPlayer�I������Ƃ�

		if (array[i + 1][j + 1] == 0) {
			i++;
			j = 0;
		} else {
			j++;
		}

		if (array[i + 1][j] == 4) {
			shibu.start();
		} else if (array[i + 1][j] == 2) {
			hatibu.start();
		} else if (array[i + 1][j] == 8) {
			nibu.start();
		} else if (array[i + 1][j] == 16) {
			zen.start();
		}
	}
}
