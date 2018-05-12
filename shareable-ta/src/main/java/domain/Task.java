package domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

// @PlanningEntity
public class Task implements Serializable {

	private static final long serialVersionUID = 7195728232431116103L;

	private String id;
	// effort estimation expressed in minutes and refers to the time spent by one
	// employee to perform the task.
	private Duration effort;
	private LocalTime completionTime;
	private Set<Skill> requiredSkillList;
	private int priority;
	private LocalTime startTime; // e.g. 08:20
	private TaskPart[] taskParts;
	private int maxParts;

	// @PlanningVariable(valueRangeProviderRefs="partsRange")
	private int parts;

	// @ValueRangeProvider(id = "partsRange")
	// public CountableValueRange<Integer> getStockAmountRange() {
	// // Range: 0..maxparts
	// return ValueRangeFactory.createIntValueRange(1, getMaxParts());
	// }

	public Task() {
	}

	public Task(String id, Duration effort, LocalTime completionTime, int priority, int maxParts, Set<Skill> requiredSkillList) {
		super();
		this.id = id;
		this.effort = effort;
		this.completionTime = completionTime;
		this.priority = priority;
		this.maxParts = maxParts;
		this.requiredSkillList = requiredSkillList;

		taskParts = new TaskPart[maxParts];
		for (int i = 0; i < maxParts; i++) {
			taskParts[i] = new TaskPart();
			taskParts[i].setId(id+ "-"+ i);
			taskParts[i].setTask(this);
			taskParts[i].setDuration(effort.dividedBy(maxParts));
			taskParts[i].setActive(true);
		}
//		taskParts[0].setDuration(effort);
//		taskParts[0].setActive(true);
	}

	// ************************************************************************
	// Getters / Setters
	// ************************************************************************

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Skill> getRequiredSkillList() {
		return requiredSkillList;
	}

	public void setRequiredSkillList(Set<Skill> requiredSkillList) {
		this.requiredSkillList = requiredSkillList;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public TaskPart[] getTaskParts() {
		return taskParts;
	}

	public void setTaskParts(TaskPart[] taskParts) {
		this.taskParts = taskParts;
	}

	public int getMaxParts() {
		return maxParts;
	}

	public void setMaxParts(int maxParts) {
		this.maxParts = maxParts;
	}

	public int getParts() {
		return parts;
	}

	public void setParts(int parts) {
		this.parts = parts;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public Duration getEffort() {
		return effort;
	}

	public void setEffort(Duration effort) {
		this.effort = effort;
	}

	public LocalTime getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(LocalTime completionTime) {
		this.completionTime = completionTime;
	}
}
