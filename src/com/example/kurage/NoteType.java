package com.example.kurage;

public enum NoteType {
	NULL("空っぽ") {
		@Override
		float getWeight() {
			return 0;
		}

		@Override
		int getResourceId() {
			return 0;
		}
	},
	HALF("二分音符") {
		@Override
		float getWeight() {
			return 8;
		}

		@Override
		int getResourceId() {
			return R.drawable.nibu;
		}
	},
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

	// どゆこと(●'×'●)？
	public static NoteType valueOf(float weight) {
		for (NoteType type : values()) {
			if (type.getWeight() == weight) {
				return type;
			}
		}
		return null;
	}

	abstract float getWeight();

	abstract int getResourceId();

	public static float MAX_SIZE = 16.0f;

}
