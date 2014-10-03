package com.example.kurage;

public enum NoteType {

	QUARTER("四分音符") {
		@Override
		float getWeight() {
			return 4;
		}

		@Override
		int getResourceId() {
			return R.drawable.shibu;
		}
	},
	EIGHTH("八分音符") {
		@Override
		float getWeight() {
			return 2;
		}

		@Override
		int getResourceId() {
			return R.drawable.hatibu;
		}
	};

	String typeString;

	NoteType(String typeString) {
		this.typeString = typeString;
	}

	// 小数をつかうかもだからint型じゃなくてfloat型。
	abstract float getWeight();

	abstract int getResourceId();

}
