package domain;

import java.io.Serializable;

import org.optaplanner.core.api.domain.lookup.PlanningId;

public class Skill implements Serializable {

	private static final long serialVersionUID = -6463704897223188538L;

	private String name;

	public Skill(String name) {
		this.name = name;
	}

	@PlanningId
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
	
	/**
	 * Tweak to make work {@link java.util.Set#contains(Object)}
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	/**
	 * Tweak to make work {@link java.util.Set#contains(Object)}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Skill)
			return getName().equals(((Skill) o).getName());
		else
			return super.equals(o);
	}

}
