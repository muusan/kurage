package com.example.kurage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	HorizontalScrollView scrollView;
	LinearLayout score;
	LinearLayout.LayoutParams params, params1;
	int measureWidth;
	ImageButton shibu, hatibu;

	// score　譜面,　measure 一小節,　line　五線譜

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		score = (LinearLayout) findViewById(R.id.LinearLayout8);
		scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		shibu = (ImageButton) findViewById(R.id.imageButton1);
		hatibu = (ImageButton) findViewById(R.id.imageButton4);
		// 音符の画像のパラメータを作成
		params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 0;

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// measureWidth = score.getChildAt(0).getWidth();
	}

	// 四分音符ボタンを押したときの処理
	public void onShibu(View v) {
		// 音符の画像のパラメータをセット
		params = new LayoutParams(params);
		params.weight = 4;
		shibu.setImageResource(R.drawable.shibu_check);
		hatibu.setImageResource(R.drawable.hatibu);
	}

	// 八分音符を押したときの処理
	public void onHatibu(View v) {
		params = new LayoutParams(params);
		params.weight = 2;
		shibu.setImageResource(R.drawable.shibu);
		hatibu.setImageResource(R.drawable.hatibu_check);
	}

	// scoreを押したときの処理
	public void onOnpu(View v) {
		if (params.weight == 0) {// 音符を選択していないとき
			Toast.makeText(this, "音符を選択してください", Toast.LENGTH_SHORT).show();
		} else {// 音符を選択しているとき

			// imageViewを作成
			ImageView imageView = new ImageView(this);
			if (params.weight == 4) {// 四分音符が選択されているとき
				imageView.setImageResource(R.drawable.shibu);
			} else if (params.weight == 2) {// 八分音符が選択されているとき
				imageView.setImageResource(R.drawable.hatibu);
			}
			// パラメータをセットする
			imageView.setLayoutParams(params);
			// onClickListenerをセットする
			imageView.setOnClickListener(this);
			// viewとしてとってきたvをLinearLayout型に変更する
			LinearLayout line = (LinearLayout) v;
			// line(五線譜の一線)に追加
			line.addView(imageView);

			// 五線譜のラインの親（つまり小節）をもってくる
			LinearLayout measure = (LinearLayout) line.getParent();
			// for(小節に入った、音符の数がiより大きいとき)
			for (int i = 0; i < measure.getChildCount(); i++) {

				// 空のimageViewを作成
				ImageView view = new ImageView(this);
				view.setImageResource(R.drawable.kara);
				view.setLayoutParams(params);
				view.setOnClickListener(this);

				LinearLayout row = (LinearLayout) measure.getChildAt(i);
				// もしrowが(v)ではないとき
				if (!row.equals(v)) {
					// 空のviewをrowに入れる
					row.addView(view);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// クリックした場所(たて(column))を取得する
		// 左から数えて何番目の位置か、を取得する
		LinearLayout line = (LinearLayout) v.getParent();
		int columnNumber = line.indexOfChild(v);

		LinearLayout measure = (LinearLayout) line.getParent();

		// すべての五線譜に対して
		// TODO すべて => 1小節に含まれる子どもの五線譜の数
		for (int i = 0; i < measure.getChildCount(); i++) {

			// たての位置のImageViewを取得
			LinearLayout row = (LinearLayout) measure.getChildAt(i);
			// すべての五線譜に対して左から数えてcolumnNumber番目の位置の
			ImageView image = (ImageView) row.getChildAt(columnNumber);
			// // ImageViewの画像を空の画像に変更する
			image.setImageResource(R.drawable.kara);
			// Log.d("画像", "画像は" + image.getDrawable());
		}

		// Viewとしてとってきたvを、ImageViewに型に変更する
		ImageView image = (ImageView) v;
		// クリックした場所の音符のパラメータを呼んでおく
		LayoutParams p = (LinearLayout.LayoutParams) image.getLayoutParams();
		// クリックした場所の空の画像を音符の画像にする
		if (params.weight == 4) {// 四分音符が選択されているとき

			if (p.weight == 2) {// 押した音符が八分音符のとき
				// ImageView imageView = (ImageView)
				// line.getChildAt(columnNumber +
				// 1);
				// imageView.setImageResource(R.drawable.hatibu);
				// params = new LayoutParams(params);
				// params.weight = 2;
				// imageView.setLayoutParams(params);
				// line.addView(imageView, columnNumber + 2);
			}

			image.setImageResource(R.drawable.shibu);
			params.weight = 4;

		} else if (params.weight == 2) {// 八分音符が選択されているとき

			if (p.weight == 4) {// 押した音符が四分音符のとき
				// 八分休符をつくる
				ImageView imageView = new ImageView(this);
				imageView.setImageResource(R.drawable.kyuhu_hatibu);
				params = new LayoutParams(params);
				params.weight = 2;
				imageView.setLayoutParams(params);
				// 押したlineの五線譜にcolumnumber+1番目の位置(八分音符の右隣)に休符を追加する
				line.addView(imageView, columnNumber + 1);
			}
			// 押した音符の画像を
			image.setImageResource(R.drawable.hatibu);
			params.weight = 2;

		}

	}

	public void plus(View v) {

		// 一小節のパラメータを作成
		params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 新しい一小節をつくる
		LinearLayout measure = new LinearLayout(this);
		// いろんな設定
		measure.setOrientation(LinearLayout.VERTICAL); // 縦
		measure.setLayoutParams(params1);
		// 小節に一小節を追加する
		score.addView(measure);

		// 一小節に五線譜を追加する
		for (int i = 0; i < 5; i++) {
			System.out.println("aaa");

			LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
			p.weight = 1;

			// 新しく五線譜を作る
			LinearLayout line = new LinearLayout(this);
			line.setWeightSum(16);
			line.setLayoutParams(p);
			line.setOrientation(LinearLayout.HORIZONTAL);
			line.setBackgroundResource(R.drawable.sen); // 背景
			line.setOnClickListener(new OnPlusClickListener());

			// 小節に五線譜を追加する
			measure.addView(line);
		}
	}

	int count = 0;

	public void play(View v) {
		measureWidth = score.getChildAt(0).getWidth();
		System.out.println("measureWidth: " + measureWidth);

		// ハンドラー
		final Handler h = new Handler();
		// ハンドラーを実行(Runnableのrunメソッドを実行する)
		h.post(new Runnable() {

			@Override
			public void run() {
				// スムーススクロールを行いつつ、countの値を増やす
				scrollView.smoothScrollBy(1, 0);
				System.out.println("scrollX: " + scrollView.getScrollX());
				if (scrollView.getScrollX() < score.getWidth() - scrollView.getWidth()) {
					// 10ms後にもう一回このrunメソッドを実行
					h.postDelayed(this, 5);
				}
			}
		});

	}

	class OnPlusClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			// imageViewを作成
			ImageView imageView = new ImageView(MainActivity.this);

			if (params.weight == 4) {// 四分音符が選択されているとき
				imageView.setImageResource(R.drawable.shibu);
			} else if (params.weight == 2) {// 八分音符が選択されているとき
				imageView.setImageResource(R.drawable.hatibu);
			}

			imageView.setLayoutParams(params);
			imageView.setOnClickListener(MainActivity.this);

			// syosetsuに追加
			LinearLayout line = (LinearLayout) v;
			line.addView(imageView);

			LinearLayout measure = (LinearLayout) line.getParent();
			for (int i = 0; i < measure.getChildCount(); i++) {
				// imageViewを作成
				ImageView view = new ImageView(MainActivity.this);
				view.setImageResource(R.drawable.kara);
				view.setLayoutParams(params);
				view.setOnClickListener(MainActivity.this);
				// syosetsuに追加
				LinearLayout row = (LinearLayout) measure.getChildAt(i);
				if (!row.equals(v)) {
					row.addView(view);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
