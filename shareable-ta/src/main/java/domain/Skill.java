package domain;

import java.io.Serializable;
import java.util.Comparator;

import org.optaplanner.core.api.domain.lookup.PlanningId;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TaSkill")
public class Skill implements Serializable, Comparable<Skill> {

	private static final long serialVersionUID = -6463704897223188538L;

	private String category;
	
	/**
	 * The task priority indicates how soon it should be done compared to other tasks. 
	 * 1 is the most important priority, 6 is the lower. 0 means the task has no defined priority 
	 */
	private int priority;

	public Skill(String category, int priority) {
		this.category = category;
		this.priority = priority;
	}

	@PlanningId
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return category + " - " + priority;
	}
	
	/**
	 * Tweak to make work {@link java.util.Set#contains(Object)}
	 */
	@Override
	public int hashCode() {
		return getCategory().hashCode();
	}
	
	/**
	 * Tweak to make work {@link java.util.Set#contains(Object)}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Skill)
			return getCategory().equals(((Skill) o).getCategory());
		else
			return super.equals(o);
	}

	@Override
	public int compareTo(Skill o) {
		return this.getCategory().compareTo(o.getCategory());
	} 
}
