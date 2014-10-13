package com.example.kurage;

public class Onpu {
	int length;// 音符の長さ
	int type;// 音があるかないか（音符か休符か）
	String pitch;// 音符の高さ

	// LinearLayout.LayoutParams params;

	public Onpu(int length, int type, String pitch) {
		this.length = length;
		this.type = type;
		this.pitch = pitch;

	}
}
