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

public class MainActivity extends Activity implements OnClickListener {

	HorizontalScrollView scrollView;
	LinearLayout score;
	LinearLayout.LayoutParams params, params1;
	int measureWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		score = (LinearLayout) findViewById(R.id.LinearLayout8);
		scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);

		// layoutParamsの初期化(初期は4分)
		onShibu(null);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		measureWidth = score.getChildAt(0).getWidth();
	}

	public void onShibu(View v) {
		// 音符の画像のパラメータを作成
		params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 4;
	}

	public void onOnpu(View v) {
		// imageViewを作成
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.shibu);
		imageView.setLayoutParams(params);
		imageView.setOnClickListener(this);
		// syosetsuに追加
		LinearLayout line = (LinearLayout) v;
		line.addView(imageView);

		LinearLayout measure = (LinearLayout) line.getParent();
		for (int i = 0; i < measure.getChildCount(); i++) {

			// imageViewを作成
			ImageView view = new ImageView(this);
			view.setImageResource(R.drawable.ic_launcher);
			view.setLayoutParams(params);
			view.setOnClickListener(this);
			// syosetsuに追加
			LinearLayout row = (LinearLayout) measure.getChildAt(i);

			if (!row.equals(v)) {
				row.addView(view);
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
			// ImageViewの画像をドロイド君に変更する
			image.setImageResource(R.drawable.ic_launcher);
		}

		// Viewとしてとってきたvを、ImageViewに型変更する
		ImageView image = (ImageView) v;
		// クリックした場所のドロイド君の画像をを音符の画像にする
		image.setImageResource(R.drawable.shibu);
	}

	public void plus(View v) {

		// 一小節のパラメータを作成
		params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 新しい一小節をつくる
		LinearLayout measure = new LinearLayout(this);
		// いろんな設定
		measure.setOrientation(LinearLayout.VERTICAL); // 縦
		measure.setBackgroundResource(R.drawable.syosetsu); // 背景
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
			line.setOnClickListener(new OnPlusClickListener());
			
			// 小節に五線譜を追加する
			measure.addView(line);
		}
	}

	int count = 0;

	public void play(View v) {
		// ハンドラー
		final Handler h = new Handler();
		// ハンドラーを実行(Runnableのrunメソッドを実行する)
		h.post(new Runnable() {

			@Override
			public void run() {
				// スムーススクロールを行いつつ、countの値を増やす
				count = count + 1;
				scrollView.smoothScrollTo(count, 0);
				if (count + measureWidth < score.getWidth()) {
					// 10ms後にもう一回このrunメソッドを実行
					h.postDelayed(this, 10);
				} else {
					count = 0;
				}
			}
		});

	}

	class OnPlusClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			// imageViewを作成
			ImageView imageView = new ImageView(MainActivity.this);
			imageView.setImageResource(R.drawable.shibu);
			imageView.setLayoutParams(params);
			imageView.setOnClickListener(MainActivity.this);

			// syosetsuに追加
			LinearLayout line = (LinearLayout) v;
			line.addView(imageView);

			LinearLayout measure = (LinearLayout) line.getParent();
			for (int i = 0; i < measure.getChildCount(); i++) {
				// imageViewを作成
				ImageView view = new ImageView(MainActivity.this);
				view.setImageResource(R.drawable.ic_launcher);
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
