package com.example.kurage;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	HorizontalScrollView scrollView;
	LinearLayout score;
	LinearLayout.LayoutParams /* params, */params1;
	int measureWidth;
	ImageButton shibu, hatibu;
	ArrayList<onpu> onpuArray = new ArrayList<onpu>();
	SoundPlayer sp;
	NoteType selectNoteType;
	Button note;

	// score　譜面,　measure 一小節,　line　五線譜

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		score = (LinearLayout) findViewById(R.id.scoreLinearLayout);
		scrollView = (HorizontalScrollView) findViewById(R.id.scoreScrollView);
		shibu = (ImageButton) findViewById(R.id.imageButton1);
		hatibu = (ImageButton) findViewById(R.id.imageButton4);
		LinearLayout firstMeasure = (LinearLayout) findViewById(R.id.firstMeasure);

		note = (Button) findViewById(R.id.note);
		note.setVisibility(View.INVISIBLE);

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
	}

	// 四分音符を選択したときの処理
	public void onShibu(View v) {
		shibu.setImageResource(R.drawable.shibu_check);
		hatibu.setImageResource(R.drawable.hatibu);

		selectNoteType = NoteType.QUARTER;
	}

	// 八分音符を選択したときの処理
	public void onHatibu(View v) {
		shibu.setImageResource(R.drawable.shibu);
		hatibu.setImageResource(R.drawable.hatibu_check);

		selectNoteType = NoteType.EIGHTH;
	}

	// 再生する
	public void play(View v) {
		measureWidth = score.getChildAt(0).getWidth();
		// System.out.println("measureWidth: " + measureWidth);

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
				scrollView.smoothScrollBy(measure.getWidth(), 0);
			}
		});
	}

	// menuを押す
	public void onMenu(View v) {

		if (note.getVisibility() == View.INVISIBLE) {
			note.setVisibility(View.VISIBLE);
			// TranslateAnimation translate = new TranslateAnimation(100, 0, 0,
			// 0);
			// // (0,0)から(100,100)に移動
			// translate.setDuration(1000); // 3000msかけてアニメーションする
			// note.startAnimation(translate); // アニメーション適用

		} else if (note.getVisibility() == View.VISIBLE) {
			note.setVisibility(View.INVISIBLE);
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

				// // imageViewを作成
				// ImageView imageView = new ImageView(context);
				// imageView.setImageResource(selectNoteType.getResourceId());
				//
				// // パラメータをセットする
				//
				// // if (params.weight == 4) {// 四分音符が選択されているとき
				// // imageView.setImageResource(R.drawable.shibu);
				// // onpu shibu = new onpu(4, SoundPlayer.NOTE, "C");
				// // onpuArray.add(shibu);
				// // } else if (params.weight == 2) {// 八分音符が選択されているとき
				// // imageView.setImageResource(R.drawable.hatibu);
				// // onpu hatibu = new onpu(8, SoundPlayer.NOTE, "C");
				// // onpuArray.add(hatibu);
				// // }
				// // // パラメータをセットする
				// // imageView.setLayoutParams(params);
				//
				// // onClickListenerをセットする
				// imageView.setOnClickListener(this);
				//
				// // viewとしてとってきたvをLinearLayout型に変更する
				// LinearLayout Line = (LinearLayout) v;
				// // line(五線譜の一線)に追加
				// Line.addView(imageView);
				//
				// // 押した音符をclickNoteと名付ける
				// ImageView clickNote = (ImageView) v;
				// // 押した音符の親をclickLineと名付ける
				// LinearLayout clickLine = (LinearLayout)
				// clickNote.getParent();
				// // 押した音符の親である、五線譜の線(line)の親をclickMeasure(一小節)と名付ける
				// LinearLayout clickMeasure = (LinearLayout)
				// clickLine.getParent();
				//
				// // // for(小節に入った、音符の数がiより大きいとき)
				// // for (int i = 0; i < clickLine.getChildCount(); i++) {
				// //
				// // // 空のimageViewを作成
				// // ImageView view = new ImageView(this);
				// // view.setImageResource(R.drawable.kara);
				// // view.setLayoutParams(params);
				// // view.setOnClickListener(this);
				// //
				// // LinearLayout row = (LinearLayout) clickLine.getChildAt(i);
				// // // もしrowが(v)ではないとき
				// // if (!row.equals(v)) {
				// // // 空のviewをrowに入れる
				// // row.addView(view);
				// // }
				// // }

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
		// @Override
		// public void onClick(View v) {
		// ImageView clickNote = (ImageView) v;
		// LinearLayout clickLine = (LinearLayout) v.getParent();
		// LinearLayout clickMeasure = (LinearLayout) clickLine.getParent();
		//
		// // クリックした場所(たて(column))を取得する
		// // 左から数えて何番目の位置か、を取得する
		// LinearLayout line = (LinearLayout) v.getParent();
		// int columnNumber = line.indexOfChild(v);
		//
		// LinearLayout measure = (LinearLayout) line.getParent();
		//
		// // すべての五線譜に対して
		// // TODO すべて => 1小節に含まれる子どもの五線譜の数
		// for (int i = 0; i < measure.getChildCount(); i++) {
		//
		// // たての位置のImageViewを取得
		// LinearLayout row = (LinearLayout) measure.getChildAt(i);
		// // すべての五線譜に対して左から数えてcolumnNumber番目の位置の
		// ImageView image = (ImageView) row.getChildAt(columnNumber);
		// // // ImageViewの画像を空の画像に変更する
		// image.setImageResource(R.drawable.kara);
		// }
		//
		// // Viewとしてとってきたvを、ImageViewに型に変更する
		// ImageView image = (ImageView) v;
		// // クリックした場所の音符のパラメータを呼んでおく
		// LayoutParams p = (LinearLayout.LayoutParams) image.getLayoutParams();
		//
		// // クリックした場所の空の画像を音符の画像にする
		// if (selectNoteType.getWeight() < p.weight) {
		// // 選択した音符.weight < 押した音符.weight　のとき
		//
		// image.setImageResource(R.drawable.hatibu);
		// LayoutParams hatibu = new LinearLayout.LayoutParams(0,
		// LayoutParams.MATCH_PARENT);
		// hatibu.weight = 2;
		// image.setLayoutParams(hatibu);
		//
		// for (int i = 0; i < selectNoteType.getWeight() / p.weight; i++) {
		// ImageView newImage = new ImageView(v.getContext());
		// newImage.setImageResource(R.drawable.kyuhu_hatibu);
		// newImage.setLayoutParams(hatibu);
		// line.addView(newImage, columnNumber + 1);
		//
		// }
		// }

		// if (params.weight == 4) {// 四分音符が選択されているとき
		//
		// if (p.weight == 2) {// 押した音符が八分音符のとき
		// // 押した音符の次の音符をnext(Imageview型)と名付ける〜〜。
		// ImageView next = (ImageView) line.getChildAt(columnNumber + 1);
		// // クリックした場所の次の音符のパラメータを呼んでおく
		// LayoutParams a = (LinearLayout.LayoutParams)
		// next.getLayoutParams();
		//
		// if (a.weight == 2/* 押した音符の次の音符が八分音符のとき */) {
		//
		// // 一小節に入ってるline(measureの子ども)の数の分だけ繰り返す(すべての五線譜に対して)
		// for (int i = 0; i < measure.getChildCount(); i++) {
		// // 左から数えてcolumnNumber+1番目の位置の(押した音符の次の)音符を削除
		// LinearLayout row = (LinearLayout) measure.getChildAt(i);
		// row.removeViewAt(columnNumber + 1);
		// // columnNumber+1番目の位置の(押した音符の次の)音を削除
		// onpuArray.remove(columnNumber + 1);
		//
		// // 四分音符分の長さの音を押した音符の位置に入れる( 'ω')
		// onpu shibu = new onpu(4, SoundPlayer.NOTE, "C");
		// onpuArray.add(columnNumber, shibu);
		// p.weight = 4;
		//
		// }
		// }
		// if (a.weight == 4/* 押した音符の次の音符が四分音符のとき */) {
		// // 押した音符の画像を変更する
		// image.setImageResource(R.drawable.shibu);
		// p.weight = 4;
		//
		// // 押した音符の次の音符を八分音符にする
		// next.setImageResource(R.drawable.hatibu);
		// a.weight = 2;
		// // next.setLayoutParams(a);
		//
		// }
		//
		// }
		//
		// image.setImageResource(R.drawable.shibu);
		// params.weight = 4;
		//
		// } else if (params.weight == 2) {// 八分音符が選択されているとき
		//
		// if (p.weight == 4) {// 押した音符が四分音符のとき
		// // 新しく八分休符をつくる
		// ImageView imageView = new ImageView(this);
		// imageView.setImageResource(R.drawable.kyuhu_hatibu);
		// params = new LayoutParams(params);
		// params.weight = 2;
		// imageView.setLayoutParams(params);
		// // 押したlineの五線譜にcolumnumber+1番目の位置(八分音符の右隣)に休符を追加する
		// line.addView(imageView, columnNumber + 1);
		// }
		// // 押した音符の画像を変更する
		// image.setImageResource(R.drawable.hatibu);
		// // 押した音符のパラメータ(長さ)を八分音符の長さにする
		// params.weight = 2;
		//
		// }

	};

}
