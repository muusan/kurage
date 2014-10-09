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

		@Override
		int getType() {
			return 0;
		}
	},
	WHOLE("全音符") {
		@Override
		float getWeight() {
			return 16;
		}

		@Override
		int getResourceId() {
			return R.drawable.zen;
		}

		@Override
		int getType() {
			return NOTE;
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

		@Override
		int getType() {
			return NOTE;
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

		@Override
		int getType() {
			return NOTE;
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

		@Override
		int getType() {
			return NOTE;
		}
	},
	SIXTEENTH("一六分音符") {
		@Override
		float getWeight() {
			return 1;
		}

		@Override
		int getResourceId() {
			return R.drawable.jurokubu;
		}

		@Override
		int getType() {
			return NOTE;
		}
	},
	WHOLE_REST("全休符") {
		@Override
		float getWeight() {
			return 16;
		}

		@Override
		int getResourceId() {
			return R.drawable.zen_rest;
		}

		@Override
		int getType() {
			return REST;
		}
	},
	HALF_REST("二分休符") {
		@Override
		float getWeight() {
			return 8;
		}

		@Override
		int getResourceId() {
			return R.drawable.nibu_rest;
		}

		@Override
		int getType() {
			return REST;
		}
	},
	QUARTER_REST("四分休符") {
		@Override
		float getWeight() {
			return 4;
		}

		@Override
		int getResourceId() {
			return R.drawable.shibu_rest;
		}

		@Override
		int getType() {
			return REST;
		}
	},
	EIGHTH_REST("八分休符") {
		@Override
		float getWeight() {
			return 2;
		}

		@Override
		int getResourceId() {
			return R.drawable.hatibu_rest;
		}

		@Override
		int getType() {
			return REST;
		}
	},
	SIXTEENTH_REST("一六分休符") {
		@Override
		float getWeight() {
			return 1;
		}

		@Override
		int getResourceId() {
			return R.drawable.jurokubu_rest;
		}

		@Override
		int getType() {
			return REST;
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

	abstract int getType();

	public static float MAX_SIZE = 16.0f;

	public final static int NOTE = 10001; // タイプ：音符
	public final static int REST = 10002; // タイプ：休符

}
