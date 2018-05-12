package domain;

public enum Priority {
	URGENT(3), NORMAL(2), LOW(1);
	
	private final int value;
	
	Priority(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static Priority fromValue(int value) {
		for (Priority priority : Priority.values()) {
			if (priority.value == value)
				return priority;
		}
		throw new IllegalArgumentException("Invalid Priority value: " + value);
	}
}
