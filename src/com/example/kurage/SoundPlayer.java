package com.example.kurage;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundPlayer {
	public final static int NOTE = 10001; // タイプ：音符
	public final static int REST = 10002; // タイプ：休符

	Context context; // 音源読み込み用

	int tempo; // 速度 buffer_size * (60 / bpm)
	byte[] note = new byte[44100 * 2 * 4]; // bpm 60 の 1小節分
	byte[] rest = new byte[44100 * 2 * 4]; // 空バッファ(休符用)

	AudioTrack at; // 音再生用のクラス

	public SoundPlayer(Context context, int bpm, int musicId) {
		this.context = context; // 音源読み込み用コンテキスト

		setTempo(bpm); // テンポの初期化
		readMusic(musicId); // 音源ファイルの読み込み

		fillEmpty(500); // ブツブツ音対策

		// AudioTrackのインスタンスを生成
		at = new AudioTrack(AudioManager.STREAM_MUSIC, // タイプ選択(とりあえず気にしない！)
				44100, // サンプリング周波数(難しいことは気にしない！)
				AudioFormat.CHANNEL_OUT_MONO, // チャンネル数(モノラルやらステレオやら)
				AudioFormat.ENCODING_DEFAULT, // エンコード(16bitやら8bitやら)
				44100, // バッファサイズ(音源を再生する前に事前に読み込んでおくところ(バッファ)のサイズらしい)
				AudioTrack.MODE_STREAM // モード選択(MODE_STREAM:書き込みながら音源を流す
										// MODE_STATIC: 書き込んでから流す)
		);
	}

	/**
	 * テンポを設定する.
	 * 
	 * @param bpm
	 *            beat per minutes 楽譜とかでよくある ♪=120 ←これ. 要はテンポ. 好きな数字を入れていいけど,
	 *            60以下の数字を入れると爆発する. (未検証)
	 */
	public void setTempo(int bpm) {
		tempo = note.length * 60 / bpm;
	}

	/**
	 * 音源ファイル(wavファイル限定！)を読み込んで、buffer配列に格納 このファイルを再生するよ！
	 * 
	 * @param musicId
	 *            音源ファイルのリソースID
	 */
	public void readMusic(int musicId) {
		// 音源ファイルを読み込み
		InputStream fin = context.getResources().openRawResource(musicId);

		try {
			fin.read(note, 0, note.length); // bufferに音源ファイルを読み込み
			fin.close(); // 音源ファイルを閉じる
		} catch (IOException e) {
			e.printStackTrace(); // 例外処理
		}

	}

	/**
	 * 音源ファイルの先頭に0を入れる. 音と音の繋ぎの際のブツブツ音が軽減される
	 * 
	 * @param n
	 *            先頭からどのくらい0を埋めるか
	 */
	public void fillEmpty(int n) {
		for (int i = 0; i < n; i++) {
			note[i] = (byte) 0; // 音源の最初を無音にするとブツブツが軽減される
		}
	}

	/**
	 * AudioTrackを再生状態へ. 再生が終わったら必ずstop()を呼び出すこと.
	 */
	public void play() {
		at.play();
	}

	/**
	 * 楽譜を書き込み
	 * 
	 * @param noteType
	 *            音符か休符かを選ぶ. SoundPlayer.NOTE 音符 SoundPlayer.REST 休符
	 * @param noteLength
	 *            音の長さを入れる 8分なら8, 4分なら4, ...などなどで入れる.
	 */
	public void write(int noteType, int noteLength) {
		if (noteType == SoundPlayer.NOTE) {
			at.write(note, 0, tempo / noteLength); // 音符の再生
		} else if (noteType == SoundPlayer.REST) {
			at.write(rest, 0, tempo / noteLength); // 休符の再生
		}
	}

	/**
	 * AudioTrackを停止状態へ. play()を呼び出していない状態でやると大変なことに. (←例外処理しておこう)
	 */
	public void stop() {
		at.stop();
	}
}
