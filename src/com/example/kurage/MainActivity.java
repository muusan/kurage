package com.example.kurage;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	HorizontalScrollView scrollView;
	LinearLayout score;
	int measureWidth;
	ArrayList<onpu> onpuArray = new ArrayList<onpu>();
	SoundPlayer sp;
	NoteType selectNoteType;
	Button play, set, note[] = new Button[6], rest[] = new Button[6];

	// score　譜面,　measure 一小節,　line　五線譜

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		score = (LinearLayout) findViewById(R.id.scoreLinearLayout);
		scrollView = (HorizontalScrollView) findViewById(R.id.scoreScrollView);
		LinearLayout firstMeasure = (LinearLayout) findViewById(R.id.firstMeasure);

		play = (Button) findViewById(R.id.play);
		set = (Button) findViewById(R.id.set);
		note[0] = (Button) findViewById(R.id.note);
		note[1] = (Button) findViewById(R.id.whole);
		note[2] = (Button) findViewById(R.id.half);
		note[3] = (Button) findViewById(R.id.quarter);
		note[4] = (Button) findViewById(R.id.eighth);
		note[5] = (Button) findViewById(R.id.sixteenth);

		rest[0] = (Button) findViewById(R.id.rest);
		rest[1] = (Button) findViewById(R.id.whole_rest);
		rest[2] = (Button) findViewById(R.id.half_rest);
		rest[3] = (Button) findViewById(R.id.quarter_rest);
		rest[4] = (Button) findViewById(R.id.eighth_rest);
		rest[5] = (Button) findViewById(R.id.sixteenth_rest);

		play.setVisibility(View.INVISIBLE);
		set.setVisibility(View.INVISIBLE);

		for (int i = 0; i < 6; i++) {
			note[i].setVisibility(View.INVISIBLE);
		}
		for (int i = 0; i < 6; i++) {
			rest[i].setVisibility(View.INVISIBLE);
		}

		// 音鳴らす用のクラスのインスタンス生成(1つめの引数はおまじない(contextっていうものだけど、今は気にしない),
		// 2つめの引数がbpm, 3つめの引数が音源ファイル)
		sp = new SoundPlayer(getApplicationContext(), 120, R.raw.nibu);

		// 音符の初期設定(空っぽ)
		selectNoteType = NoteType.NULL;

		// 初めの小節にonClickを実装する
		setOnLineClickListener(firstMeasure, mOnLineClickListener);

		for (int i = 0; i < score.getChildCount(); i++) {
			// scoreの子(つまりmeasure)にmOnLineClickListenerをセットする
		}
		score.addView(createMeasure(this));
	}

	// 全音符を選択したときの処理
	public void onZen(View v) {
		selectNoteType = NoteType.WHOLE;
	}

	// 二分音符を選択したときの処理
	public void onNibu(View v) {
		selectNoteType = NoteType.HALF;
	}

	// 四分音符を選択したときの処理
	public void onShibu(View v) {
		selectNoteType = NoteType.QUARTER;
	}

	// 八分音符を選択したときの処理
	public void onHatibu(View v) {
		selectNoteType = NoteType.EIGHTH;
	}

	// 一六分音符を選択したときの処理
	public void onJurokubu(View v) {
		selectNoteType = NoteType.SIXTEENTH;
	}

	// 全休符を選択したときの処理
	public void onZenRest(View v) {
		selectNoteType = NoteType.WHOLE_REST;
	}

	// 二分休符を選択したときの処理
	public void onNibuRest(View v) {
		selectNoteType = NoteType.HALF_REST;
	}

	// 四分休符を選択したときの処理
	public void onShibuRest(View v) {
		selectNoteType = NoteType.QUARTER_REST;
	}

	// 八分休符を選択したときの処理
	public void onHatibuRest(View v) {
		selectNoteType = NoteType.EIGHTH_REST;
	}

	// 一六分休符を選択したときの処理
	public void onJurokubuRest(View v) {
		selectNoteType = NoteType.SIXTEENTH_REST;
	}

	// 再生する
	public void play(View v) {
		measureWidth = score.getChildAt(0).getWidth();
		// System.out.println("measureWidth: " + measureWidth);

		// 初めから再生
		scrollView.scrollTo(0, 0);

		// ハンドラー
		final Handler h = new Handler();
		// ハンドラーを実行(Runnableのrunメソッドを実行する)
		h.post(new Runnable() {

			@Override
			public void run() {
				// スムーススクロールを行いつつ、countの値を増やす
				scrollView.smoothScrollBy(1, 0);
				// System.out.println("scrollX: " + scrollView.getScrollX());
				if (scrollView.getScrollX() < score.getWidth() - scrollView.getWidth()) {
					// 10ms後にもう一回このrunメソッドを実行
					h.postDelayed(this, 5);
				}
			}
		});
		// 音の再生
		sp.play();// 再生開始

		// 音符あれいにはいってる音符の情報(typeとlength)を一つずつ読み込む
		for (int i = 0; i < onpuArray.size(); i++) {
			onpu oto = onpuArray.get(i);
			sp.write(oto.type, oto.length);
		}

		sp.stop(); // 再生終了

	}

	public float getWeight(View v) {
		// 取得したvのlayoutParamsのweightをもってくる
		return getParams(v).weight;
	}

	public LinearLayout.LayoutParams getParams(View v) {
		// 取得したvのlayoutParamsをもってくる
		return (LayoutParams) v.getLayoutParams();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void setOnLineClickListener(LinearLayout measure, OnClickListener listener) {
		for (int i = 0; i < measure.getChildCount(); i++) {
			measure.getChildAt(i).setOnClickListener(listener);
		}
	}

	public LinearLayout createMeasure(Context context) {
		// measureのViewを作成
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout measure = (LinearLayout) inflater.inflate(R.layout.measure, null);

		measure.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setOnLineClickListener(measure, mOnLineClickListener);

		return measure;
	}

	// 小節を増やす
	public void addMeasure(View v) {

		final LinearLayout measure = createMeasure(v.getContext());
		score.addView(measure);

		new Handler().post(new Runnable() {

			@Override
			public void run() {
				// スクロールが一番右にいくようにしたい(●'×'●)
				scrollView.smoothScrollBy(score.getWidth(), 0);
			}
		});
	}

	public void onRelativeLayout(View v) {
		if (note[0].getVisibility() == View.VISIBLE) {
			play.setVisibility(View.INVISIBLE);
			set.setVisibility(View.INVISIBLE);
			for (int i = 0; i < 6; i++) {
				note[i].setVisibility(View.INVISIBLE);
			}
			for (int i = 0; i < 6; i++) {
				rest[i].setVisibility(View.INVISIBLE);
			}
		}

		// Context context = v.getContext();
		// Toast.makeText(context, "かくにん", Toast.LENGTH_SHORT).show();
	}

	// menuを押す
	public void onMenu(View v) {

		if (note[0].getVisibility() == View.INVISIBLE) {
			play.setVisibility(View.VISIBLE);
			set.setVisibility(View.VISIBLE);
			note[0].setVisibility(View.VISIBLE);
			rest[0].setVisibility(View.VISIBLE);

			TranslateAnimation t = new TranslateAnimation(60, 0, 0, 0);
			// (0,0)から(100,100)に移動
			t.setDuration(150); // 3000msかけてアニメーションする
			play.startAnimation(t); // アニメーション適用

			TranslateAnimation t1 = new TranslateAnimation(120, 0, 0, 0);
			// (0,0)から(100,100)に移動
			t1.setDuration(200); // 3000msかけてアニメーションする
			set.startAnimation(t1); // アニメーション適用

			TranslateAnimation t2 = new TranslateAnimation(0, 0, 60, 0);
			// (0,0)から(100,100)に移動
			t2.setDuration(150); // 3000msかけてアニメーションする
			note[0].startAnimation(t2); // アニメーション適用

			TranslateAnimation t3 = new TranslateAnimation(0, 0, 120, 0);
			// (0,0)から(100,100)に移動
			t3.setDuration(200); // 3000msかけてアニメーションする
			rest[0].startAnimation(t3); // アニメーション適用

		} else if (note[0].getVisibility() == View.VISIBLE) {
			play.setVisibility(View.INVISIBLE);
			set.setVisibility(View.INVISIBLE);
			for (int i = 0; i < 6; i++) {
				note[i].setVisibility(View.INVISIBLE);
			}
			for (int i = 0; i < 6; i++) {
				rest[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	public void onNote(View v) {
		if (note[1].getVisibility() == View.VISIBLE) {
			for (int i = 1; i < 6; i++) {
				note[i].setVisibility(View.INVISIBLE);
			}
		} else if (note[1].getVisibility() == View.INVISIBLE) {
			for (int i = 1; i < 6; i++) {
				note[i].setVisibility(View.VISIBLE);
			}
			for (int i = 1; i < 6; i++) {
				rest[i].setVisibility(View.INVISIBLE);
			}

			for (int i = 1; i < 6; i++) {
				TranslateAnimation t = new TranslateAnimation(i * 60, 0, 0, 0);
				// (0,0)から(100,100)に移動
				t.setDuration(100 + i * 50); // 3000msかけてアニメーションする
				note[i].startAnimation(t); // アニメーション適用
			}
		}
	}

	public void onRest(View v) {
		if (rest[1].getVisibility() == View.VISIBLE) {
			for (int i = 1; i < 6; i++) {
				rest[i].setVisibility(View.INVISIBLE);
			}
		} else if (rest[1].getVisibility() == View.INVISIBLE) {
			for (int i = 1; i < 6; i++) {
				note[i].setVisibility(View.INVISIBLE);
			}
			for (int i = 1; i < 6; i++) {
				rest[i].setVisibility(View.VISIBLE);
			}
			for (int i = 1; i < 6; i++) {
				TranslateAnimation t = new TranslateAnimation(i * 60, 0, 0, 0);
				// (0,0)から(100,100)に移動
				t.setDuration(100 + i * 50); // 3000msかけてアニメーションする
				rest[i].startAnimation(t); // アニメーション適用
			}
		}
	}

	// 音符を追加する
	OnClickListener mOnLineClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			LinearLayout selectLine = (LinearLayout) v;
			LinearLayout measure = (LinearLayout) v.getParent();

			// 音符の総数を数える(16分音符が何個分あるか)
			int weightSum = 0;
			for (int i = 0; i < selectLine.getChildCount(); i++) {
				weightSum += getWeight(selectLine.getChildAt(i));
			}

			// 音符が置けるかチェック
			float weight = selectNoteType.getWeight();
			if (weight > 16 - weightSum) {
				Toast.makeText(v.getContext(), "この音符はここには置けません", Toast.LENGTH_SHORT).show();
				return;
			}

			// 音符を選択していないとき
			if (selectNoteType.getWeight() == 0) {
				Toast.makeText(context, "音符を選択してください", Toast.LENGTH_SHORT).show();
			}
			// 音符を選択しているとき
			else if (selectNoteType.getWeight() > 0) {

				// 音符用のLayoutParamsを作成
				LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
				params.weight = weight;

				// すべてのlineに対して音符を追加
				for (int i = 0; i < measure.getChildCount(); i++) {
					// 五線譜だから、measure.getChildCount() は 5。
					// i番目のlineを取得
					LinearLayout line = (LinearLayout) measure.getChildAt(i);

					// 追加する音符を作成
					ImageView note = new ImageView(context);
					note.setScaleType(ImageView.ScaleType.FIT_START);

					// 音符の大きさを指定
					note.setLayoutParams(params);
					// 音符にonClickListenerをセット
					note.setOnClickListener(mOnNoteClickListener);

					// 選択したlineの音符には画像をつける
					if (line == v) {
						note.setImageResource(selectNoteType.getResourceId());
					}
					// lineに音符(note)を追加
					line.addView(note);
				}
				// weightSumを増やす。
				weightSum += weight;

			}
		}
	};
	// 音符を変更する
	OnClickListener mOnNoteClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ImageView clickNote = (ImageView) v;
			LinearLayout clickLine = (LinearLayout) v.getParent();
			LinearLayout clickMeasure = (LinearLayout) clickLine.getParent();

			// 置けるかどうかチェックする
			float restWeight = NoteType.MAX_SIZE;// つまり16
			// 左から数えて、押した音符番目　分の回数をfor文でまわす
			for (int i = 0; i < clickLine.indexOfChild(clickNote); i++) {
				// restWeight = 16 - 押した音符の五線譜の音符の一番目からi番目までのweight
				restWeight -= getWeight(clickLine.getChildAt(i));
			}

			if (restWeight < selectNoteType.getWeight()) {
				Toast.makeText(v.getContext(), "ここにはこの音符は置けません", Toast.LENGTH_SHORT).show();
				return;
			}

			float selectNoteTypeWeight = selectNoteType.getWeight();
			float clickNoteWeight = getWeight(clickNote);

			if (clickNoteWeight < selectNoteTypeWeight) {
				Toast.makeText(v.getContext(), "ここにはこの音符は置けません", Toast.LENGTH_SHORT).show();
				return;
			} else if (clickNoteWeight >= selectNoteTypeWeight) {
				int rate = (int) (clickNoteWeight / selectNoteTypeWeight);

				getParams(clickNote).weight = selectNoteTypeWeight;

				// line内のpositionを取得
				// 押した音符の五線譜の中で、押した音符はpos番目。
				int pos = clickLine.indexOfChild(clickNote);

				// 音符用のLayoutParamsを作成
				LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
				params.weight = selectNoteTypeWeight;

				// すべてのlineに対して
				for (int i = 0; i < clickMeasure.getChildCount(); i++) {
					LinearLayout line = (LinearLayout) clickMeasure.getChildAt(i);
					{// pos番目のlineに入ってる音符をnoteって名付ける
						ImageView note = (ImageView) line.getChildAt(pos);
						if (note == clickNote) {
							// もし押した音符とnoteが左から数えて縦の番目と、上から数えて横の番目が同じとき
							note.setImageResource(selectNoteType.getResourceId());
						} else {
							// もし押した音符とnoteが左から数えて縦の番目が同じとき(五線譜の高さだけ違うとき)
							note.setImageBitmap(null);
						}
					}

					// rate =　押した音符のweigth / 選択中の音符のweight
					// (rate - 1)個分新しく音符をつくる
					for (int j = 1; j < rate; j++) {
						// 追加する音符を作成
						ImageView note = new ImageView(v.getContext());
						note.setScaleType(ImageView.ScaleType.FIT_START);

						// 音符の大きさを指定
						note.setLayoutParams(params);
						note.setOnClickListener(mOnNoteClickListener);

						// 選択したlineの音符には画像をつける
						if (line == clickLine) {
							note.setImageResource(selectNoteType.getResourceId());
						}

						line.addView(note, pos);
					}
				}
			}

		}

	};

}
