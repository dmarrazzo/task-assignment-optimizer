package domain;

import java.io.Serializable;

public class Skill implements Serializable {

	private static final long serialVersionUID = -6463704897223188538L;
	private String name;

	public Skill(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%s", name);
	}
}
