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

	ImageView imageView, view;
	ImageButton shibu;
	// LinearLayout gosenhu;
	LinearLayout syosetsu;
	LinearLayout.LayoutParams params, params1;
	HorizontalScrollView sv;
	int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		shibu = (ImageButton) findViewById(R.id.imageButton1);

		syosetsu = (LinearLayout) findViewById(R.id.LinearLayout8);

		// for (int i = 0; i < 5; i++) {
		// gosenhu[i] = (LinearLayout) findViewById(R.id.LinearLayout3 + i);
		// }

		sv = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);

	} 

	public void onShibu(View v) {
		// 音符の画像のパラメータを作成
		params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 4;
		// すこーぷ
	}

	public void onOnpu(View v) {
		// imageViewを作成
		imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.shibu);
		imageView.setLayoutParams(params);
		imageView.setOnClickListener(this);
		// syosetsuに追加
		LinearLayout gosenhu = (LinearLayout) v;
		gosenhu.addView(imageView);

		LinearLayout issyosetsu = (LinearLayout) gosenhu.getParent();
		for (int i = 0; i < issyosetsu.getChildCount(); i++) {

			// imageViewを作成
			view = new ImageView(this);
			view.setImageResource(R.drawable.ic_launcher);
			view.setLayoutParams(params);
			view.setOnClickListener(this);
			// syosetsuに追加
			LinearLayout row = (LinearLayout) issyosetsu.getChildAt(i);

			if (!row.equals(v)) {
				row.addView(view);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// クリックした場所(たて(column))を取得する
		// 左から数えて何番目の位置か、を取得する
		LinearLayout gosenhu = (LinearLayout) v.getParent();
		int columnNumber = gosenhu.indexOfChild(v);

		LinearLayout issyosetsu = (LinearLayout) gosenhu.getParent();

		// すべての五線譜に対して
		// TODO すべて => 1小節に含まれる子どもの五線譜の数
		for (int i = 0; i < issyosetsu.getChildCount(); i++) {

			// たての位置のImageViewを取得
			LinearLayout row = (LinearLayout) issyosetsu.getChildAt(i);
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

		// // TODO ImageViewが含まれている1小節を、gosenhuから取得(gosenhuの親)
		// LinearLayout issyosetsu = (LinearLayout) v.getParent();
		// int rowNumber = issyosetsu.indexOfChild(v);
		//
		// 　
		// if (rowNumber <= 5) {
		// // TODO 1小節から、ひとつずつ五線譜を取得
		//
		// LinearLayout newIssyosetsu = (LinearLayout)
		// this.issyosetsu.getChildAt(rowNumber);
		// syosetsu.addView(newIssyosetsu);
		// }

		// 一小節のパラメータを作成
		params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 新しい一小節をつくる
		LinearLayout issyosetsu = new LinearLayout(this);
		// いろんな設定
		issyosetsu.setWeightSum(16); // ウェイト
		issyosetsu.setOrientation(LinearLayout.VERTICAL); // 縦
		issyosetsu.setBackgroundResource(R.drawable.syosetsu); // 背景
		issyosetsu.setOnClickListener(new plusClickListener()); // onClick
		issyosetsu.setLayoutParams(params1);
		// 小節に一小節を追加する
		syosetsu.addView(issyosetsu);

		// 一小節に五線譜を追加する
		for (int i = 0; i < issyosetsu.getChildCount(); i++) {
			// 新しく五線譜を作る
			LinearLayout gosenhu = new LinearLayout(this);
			gosenhu.setOrientation(LinearLayout.HORIZONTAL);
			issyosetsu.addView(gosenhu);
		}
		width = syosetsu.getChildAt(0).getWidth();
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
				sv.smoothScrollTo(count, 0);
				if (count + width < syosetsu.getWidth()) {
					// 10ms後にもう一回このrunメソッドを実行
					h.postDelayed(this, 10);
				} else {
					count = 0;
				}
			}
		});

	}

	class plusClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// imageViewを作成
			imageView = new ImageView(MainActivity.this);
			imageView.setImageResource(R.drawable.shibu);
			imageView.setLayoutParams(params);
			imageView.setOnClickListener(MainActivity.this);

			// syosetsuに追加
			LinearLayout gosenhu = (LinearLayout) v;
			gosenhu.addView(imageView);

			LinearLayout issyosetsu = (LinearLayout) gosenhu.getParent();
			for (int i = 0; i < issyosetsu.getChildCount(); i++) {
				// imageViewを作成
				view = new ImageView(MainActivity.this);
				view.setImageResource(R.drawable.ic_launcher);
				view.setLayoutParams(params);
				view.setOnClickListener(MainActivity.this);
				// syosetsuに追加
				LinearLayout row = (LinearLayout) issyosetsu.getChildAt(i);
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
