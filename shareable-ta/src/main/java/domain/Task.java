package domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

public class Task implements Serializable {

	private static final long serialVersionUID = 7195728232431116103L;

	private String id;
	// effort estimation refers to the time spent by one employee to perform the task.
	private Duration effort;
	private LocalTime completionTime;
	private Set<Skill> requiredSkillList;
	private int priority;
	private TaskPart[] taskParts;
	private int maxParts;

	public Task() {
	}

	public Task(String id, Duration effort, LocalTime completionTime, int priority, int maxParts,
	        Set<Skill> requiredSkillList) {
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
			taskParts[i].setId(id + "-" + i);
			taskParts[i].setTask(this);
			taskParts[i].setDuration(effort.dividedBy(maxParts));
		}
	}

	@Override
	public String toString() {
		return String.format("Task [%s, eff=%s, cT=%s, pr=%s, maxP=%s]", id, effort, completionTime, priority, maxParts);
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
