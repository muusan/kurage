package com.example.kurage;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	HorizontalScrollView scrollView;
	LinearLayout score;
	int measureWidth;
	ArrayList<Onpu> onpuArray = new ArrayList<Onpu>();
	SoundPlayer sp;
	NoteType selectNoteType;
	Button set, note[] = new Button[5], rest[] = new Button[5], buttons[] = new Button[4];
	ImageView[] image = new ImageView[10];
	LinearLayout ll;
	String[] color = new String[10];

	// score　譜面,　measure 一小節,　line　五線譜

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		score = (LinearLayout) findViewById(R.id.scoreLinearLayout);
		scrollView = (HorizontalScrollView) findViewById(R.id.scoreScrollView);
		LinearLayout firstMeasure = (LinearLayout) findViewById(R.id.firstMeasure);
		ll = (LinearLayout) findViewById(R.id.LinearLayout2);

		buttons[0] = (Button) findViewById(R.id.play);
		buttons[1] = (Button) findViewById(R.id.set);
		buttons[2] = (Button) findViewById(R.id.note);
		buttons[3] = (Button) findViewById(R.id.rest);

		note[0] = (Button) findViewById(R.id.whole);
		note[1] = (Button) findViewById(R.id.half);
		note[2] = (Button) findViewById(R.id.quarter);
		note[3] = (Button) findViewById(R.id.eighth);
		note[4] = (Button) findViewById(R.id.sixteenth);

		rest[0] = (Button) findViewById(R.id.whole_rest);
		rest[1] = (Button) findViewById(R.id.half_rest);
		rest[2] = (Button) findViewById(R.id.quarter_rest);
		rest[3] = (Button) findViewById(R.id.eighth_rest);
		rest[4] = (Button) findViewById(R.id.sixteenth_rest);

		for (int i = 0; i < 4; i++) {
			buttons[i].setVisibility(View.VISIBLE);
			buttons[i].setAlpha(0f);
		}
		for (int i = 0; i < 5; i++) {
			note[i].setVisibility(View.VISIBLE);
			note[i].setAlpha(0f);
		}
		for (int i = 0; i < 5; i++) {
			rest[i].setVisibility(View.VISIBLE);
			rest[i].setAlpha(0f);
		}

		for (int i = 0; i < 10; i++) {
			image[i] = (ImageView) findViewById(getResources().getIdentifier("imageView" + (i + 1), "id",
					getPackageName()));

		}

		color[0] = "ff00f8";// ピンク
		color[1] = "ff00b7";// 赤ピンク
		color[2] = "ff0000";// 赤
		color[3] = "ff8f00";// オレンジ
		color[4] = "ffde00";// 黄色
		color[5] = "9cff00";// 黄緑
		color[6] = "00ffc4";// 青緑
		color[7] = "00ebff";// 水色
		color[8] = "008fff";// 青
		color[9] = "b700ff";// 紫

		// 音鳴らす用のクラスのインスタンス生成(1つめの引数はおまじない(contextっていうものだけど、今は気にしない),
		// 2つめの引数がbpm, 3つめの引数が音源ファイル)
		sp = new SoundPlayer(getApplicationContext(), 120, R.raw.nibu);

		// 音符の初期設定(空っぽ)
		selectNoteType = NoteType.NULL;

		// 初めの小節にonClickを実装する
		setOnLineClickListener(firstMeasure, mOnLineClickListener);

		// for (int i = 0; i < score.getChildCount(); i++) {
		// // scoreの子(つまりmeasure)にmOnLineClickListenerをセットする
		// }
		score.addView(createMeasure(this));
	}

	// 全音符を選択したときの処理
	public void onZen(View v) {
		selectNoteType = NoteType.WHOLE;

		// よこのイメージビューをすべて初期の色にする
		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		// 押した音符と同じ音符のイメージビューだけ画像をいれる
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

		//
		// final CompoundButton button = (CompoundButton) v;
		// ll.removeViewAt(selectNoteType.getNumber());
		// ImageView image = new ImageView(this);
		// LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(10,
		// 25);
		// param1.bottomMargin = 10;
		// image.setLayoutParams(param1);
		//
		// if (button.isChecked()) {
		// image.setBackgroundResource(selectNoteType.getResourceId());
		// } else {
		// image.setBackgroundColor(Color.parseColor("#ff00f8"));
		// }
		// ll.addView(image, selectNoteType.getNumber());

	}

	// 二分音符を選択したときの処理
	public void onNibu(View v) {
		selectNoteType = NoteType.HALF;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 四分音符を選択したときの処理
	public void onShibu(View v) {
		selectNoteType = NoteType.QUARTER;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 八分音符を選択したときの処理
	public void onHatibu(View v) {
		selectNoteType = NoteType.EIGHTH;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 一六分音符を選択したときの処理
	public void onJurokubu(View v) {
		selectNoteType = NoteType.SIXTEENTH;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());
	}

	// 全休符を選択したときの処理
	public void onZenRest(View v) {
		selectNoteType = NoteType.WHOLE_REST;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 二分休符を選択したときの処理
	public void onNibuRest(View v) {
		selectNoteType = NoteType.HALF_REST;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 四分休符を選択したときの処理
	public void onShibuRest(View v) {
		selectNoteType = NoteType.QUARTER_REST;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 八分休符を選択したときの処理
	public void onHatibuRest(View v) {
		selectNoteType = NoteType.EIGHTH_REST;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 一六分休符を選択したときの処理
	public void onJurokubuRest(View v) {
		selectNoteType = NoteType.SIXTEENTH_REST;

		for (int i = 0; i < 10; i++) {
			image[i].setBackgroundColor(Color.parseColor("#" + color[i]));
		}
		image[selectNoteType.getNumber()].setBackgroundResource(selectNoteType.getResourceId());

	}

	// 再生する
	public void play(View v) {
		// for (int i = 0; i < score.getChildCount(); i++) {
		// // scoreの子(つまりmeasure)の個数分繰り返す
		// LinearLayout measure = (LinearLayout) score.getChildAt(i);
		// for (int i1 = 0; i1 < measure.getChildCount(); i1++) {
		// ImageView note = (ImageView) measure.getChildAt(i1);
		//
		// }
		// }

		measureWidth = score.getChildAt(0).getWidth();
		// System.out.println("measureWidth: " + measureWidth);
		// 初めから再生
		scrollView.scrollTo(0, 0);

		// ハンドラー
		final Handler h = new Handler();

		// スレッド起動

		(new Thread(new Runnable() {
			@Override
			public void run() {
				// 通常バックグランドをここに記述します

				h.post(new Runnable() {
					@Override
					public void run() {
						// スムーススクロールを行いつつ、countの値を増やす
						scrollView.smoothScrollBy(1, 0);
						// System.out.println("scrollX: " +
						// scrollView.getScrollX());
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
					Onpu oto = onpuArray.get(i);
					sp.write(oto.type, oto.length);
				}

				sp.stop(); // 再生終了
			}
		})).start();

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

	// menuを押す
	@SuppressLint("NewApi")
	public void onMenu(View v) {

		final CompoundButton radio = (CompoundButton) v;
		float width = v.getWidth();

		for (int i = 0; i < buttons.length - 2; i++) {

			PropertyValuesHolder alpha;
			PropertyValuesHolder trans;

			if (radio.isChecked()) {
				alpha = PropertyValuesHolder.ofFloat("alpha", buttons[i].getAlpha(), 1f);
				if (buttons[i].getTranslationX() <= 0) {
					trans = PropertyValuesHolder.ofFloat("translationX", width, 0f);
				} else {
					trans = PropertyValuesHolder.ofFloat("translationX", buttons[i].getTranslationX(), 0f);
				}

			} else {
				alpha = PropertyValuesHolder.ofFloat("alpha", buttons[i].getAlpha(), 0f);
				trans = PropertyValuesHolder.ofFloat("translationX", buttons[i].getTranslationX(), width);
			}

			ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(buttons[i], alpha, trans);
			anim.setDuration(300);

			final int index = i;
			anim.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					buttons[index].setClickable(radio.isChecked());
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub

				}
			});

			anim.start();

			int j = i + 2;
			if (radio.isChecked()) {
				alpha = PropertyValuesHolder.ofFloat("alpha", buttons[j].getAlpha(), 1f);
				if (buttons[i].getTranslationY() <= 0) {
					trans = PropertyValuesHolder.ofFloat("translationY", width, 0f);
				} else {
					trans = PropertyValuesHolder.ofFloat("translationY", buttons[j].getTranslationY(), 0f);
				}
			} else {
				alpha = PropertyValuesHolder.ofFloat("alpha", buttons[j].getAlpha(), 0f);
				trans = PropertyValuesHolder.ofFloat("translationY", buttons[j].getTranslationY(), width);
			}

			anim = ObjectAnimator.ofPropertyValuesHolder(buttons[j], alpha, trans);
			anim.setDuration(300);

			final int jndex = j;
			anim.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					buttons[jndex].setClickable(radio.isChecked());
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub

				}
			});

			anim.start();

			width += buttons[i].getWidth();
		}
		// // noteやrestが見えてるとき
		// for (int i = 0; i < note.length; i++) {
		//
		// PropertyValuesHolder alpha;
		// PropertyValuesHolder trans;
		//
		// if (!radio.isChecked()) {
		// if (note[i].getVisibility() == View.VISIBLE) {
		// alpha = PropertyValuesHolder.ofFloat("alpha", note[i].getAlpha(),
		// 0f);
		// trans = PropertyValuesHolder.ofFloat("translationX",
		// note[i].getTranslationX(), width);
		// ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(note[i],
		// alpha, trans);
		// anim.setDuration(300);
		//
		// final int index = i;
		// anim.addListener(new AnimatorListener() {
		//
		// @Override
		// public void onAnimationStart(Animator animation) {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animator animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animator animation) {
		// // TODO Auto-generated method stub
		// note[index].setClickable(radio.isChecked());
		// }
		//
		// @Override
		// public void onAnimationCancel(Animator animation) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// anim.start();
		//
		// note[i].setVisibility(View.INVISIBLE);
		// note[i].setAlpha(1f);
		// System.out.println("onMenu.note.getAlpha(): " + note[i].getAlpha());
		// } else {
		// }
		// }
		//
		// }

	}

	@SuppressLint("NewApi")
	public void onNote(View v) {
		System.out.println("onNoteできるよ");

		final CompoundButton radio = (CompoundButton) v;
		float width = v.getWidth();

		for (int i = 0; i < note.length; i++) {

			PropertyValuesHolder alpha;
			PropertyValuesHolder trans;

			if (radio.isChecked()) {
				alpha = PropertyValuesHolder.ofFloat("alpha", note[i].getAlpha(), 1f);
				if (note[i].getTranslationX() <= 0) {
					trans = PropertyValuesHolder.ofFloat("translationX", width, 0f);
				} else {
					trans = PropertyValuesHolder.ofFloat("translationX", note[i].getTranslationX(), 0f);
				}

			} else {
				alpha = PropertyValuesHolder.ofFloat("alpha", note[i].getAlpha(), 0f);
				trans = PropertyValuesHolder.ofFloat("translationX", note[i].getTranslationX(), width);
			}

			ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(note[i], alpha, trans);
			anim.setDuration(300);

			final int index = i;
			anim.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					note[index].setClickable(radio.isChecked());
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub

				}
			});

			anim.start();

			width += note[i].getWidth();
		}
		// if (note[1].getVisibility() == View.VISIBLE) {
		// for (int i = 0; i < 5; i++) {
		// note[i].setVisibility(View.INVISIBLE);
		//
		// TranslateAnimation t = new TranslateAnimation(0, i * 100, 0, 0);
		// // (0,0)から(100,100)に移動
		// t.setDuration(100 + i * 50); // 3000msかけてアニメーションする
		// note[i].startAnimation(t); // アニメーション適用
		// }
		//
		// } else if (note[1].getVisibility() == View.INVISIBLE) {
		// for (int i = 0; i < 5; i++) {
		// note[i].setVisibility(View.VISIBLE);
		// rest[i].setVisibility(View.INVISIBLE);
		//
		// TranslateAnimation t = new TranslateAnimation(i * 100, 0, 0, 0);
		// // (0,0)から(100,100)に移動
		// t.setDuration(100 + i * 50); // 3000msかけてアニメーションする
		// note[i].startAnimation(t); // アニメーション適用
		//
		// // System.out.println("note.getAlpha(): " + note[i].getAlpha());
		//
		// }
		//
		// }
	}

	@SuppressLint("NewApi")
	public void onRest(View v) {

		final CompoundButton radio = (CompoundButton) v;
		float width = v.getWidth();

		for (int i = 0; i < rest.length; i++) {

			PropertyValuesHolder alpha;
			PropertyValuesHolder trans;

			if (radio.isChecked()) {
				System.out.println("rest[i]: " + rest[i]);
				alpha = PropertyValuesHolder.ofFloat("alpha", rest[i].getAlpha(), 1f);
				if (rest[i].getTranslationX() <= 0) {
					trans = PropertyValuesHolder.ofFloat("translationX", width, 0f);
				} else {
					trans = PropertyValuesHolder.ofFloat("translationX", rest[i].getTranslationX(), 0f);
				}

			} else {
				alpha = PropertyValuesHolder.ofFloat("alpha", rest[i].getAlpha(), 0f);
				trans = PropertyValuesHolder.ofFloat("translationX", rest[i].getTranslationX(), width);
			}

			ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(rest[i], alpha, trans);
			anim.setDuration(300);

			final int index = i;
			anim.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					rest[index].setClickable(radio.isChecked());
				}

				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub

				}
			});

			anim.start();

			width += rest[i].getWidth();

			// if (rest[1].getVisibility() == View.VISIBLE) {
			// for (int i = 0; i < 5; i++) {
			// rest[i].setVisibility(View.INVISIBLE);
			//
			// TranslateAnimation t = new TranslateAnimation(0, i * 100, 0, 0);
			// // (0,0)から(100,100)に移動
			// t.setDuration(100 + i * 50); // 3000msかけてアニメーションする
			// rest[i].startAnimation(t); // アニメーション適用
			// }
			// } else if (rest[1].getVisibility() == View.INVISIBLE) {
			// for (int i = 0; i < 5; i++) {
			// note[i].setVisibility(View.INVISIBLE);
			// rest[i].setVisibility(View.VISIBLE);
			//
			// TranslateAnimation t = new TranslateAnimation(i * 100, 0, 0, 0);
			// // (0,0)から(100,100)に移動
			// t.setDuration(100 + i * 50); // 3000msかけてアニメーションする
			// rest[i].startAnimation(t); // アニメーション適用
			// }
			// }
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

				// selectNoteType.getWeight()をfloatのままでいさせたい....
				Onpu onpu = new Onpu((int) selectNoteType.getWeight(), selectNoteType.getType(), "C");
				onpuArray.add(onpu);

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

							onpuArray.remove(pos);

							Onpu onpu = new Onpu((int) selectNoteType.getWeight(), selectNoteType.getType(), "C");
							onpuArray.add(onpu);

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
							Onpu onpu = new Onpu((int) selectNoteType.getWeight(), selectNoteType.getType(), "C");
							onpuArray.add(onpu);
						}
						line.addView(note, pos);

					}
				}
			}

		}

	};

}
