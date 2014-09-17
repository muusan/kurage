package com.example.kurage;


public class onpu {
	int length;// 音符の長さ
	int type;// 音があるかないか（音符か休符か）
	String pitch;// 音符の高さ

	// LinearLayout.LayoutParams params;

	public onpu(int length, int type, String pitch /* ,int weight */) {
		this.length = length;
		this.type = type;
		this.pitch = pitch;
		// this.params.weight = weight;

	}
}
