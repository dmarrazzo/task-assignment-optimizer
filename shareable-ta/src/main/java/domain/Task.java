package domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TaTask")
public class Task implements Serializable {

	private static final long serialVersionUID = 7195728232431116103L;

	// effort estimation refers to the time spent by one employee to perform the task.
	private Duration effort;
	private LocalTime completionTime;
	private Skill requiredSkill;
	private int priority;
	private TaskPart[] taskParts;
	private int maxParts;
	
	 /** The aisle. */
    private String aisle;
    
	/**
	 * The category the task belongs to 
	 */
	private String category;

	

	public Task() {
	}

	public Task(final String aisle, final String category, final Float taskDuration, final Integer priority, final LocalTime timeConstraint,final int maxParts) {
		
		super();
		
		this.aisle = aisle;
		this.category = category;
		
		
		//Cast taskDuration to an actual Duration objet
		long numberOfHours = taskDuration.longValue();
		Float numberOfMinutes =  (taskDuration - numberOfHours) * 60;
		this.effort = Duration.ofMinutes(numberOfHours*60 + numberOfMinutes.longValue());
		
		
		this.completionTime = timeConstraint;
		
		
		
		this.priority = priority;
		this.maxParts = maxParts;
		this.requiredSkill = new Skill (category,priority);

		taskParts = new TaskPart[maxParts];
		for (int i = 0; i < maxParts; i++) {
			taskParts[i] = new TaskPart();
			taskParts[i].setId(category + "-" + i);
			taskParts[i].setTask(this);
			taskParts[i].setDuration(effort.dividedBy(maxParts));
		}
	}

	@Override
	public String toString() {
		return String.format("Task [%s, eff=%s, cT=%s, pr=%s, maxP=%s]", category, effort, completionTime, priority, maxParts);
	}

	// ************************************************************************
	// Getters / Setters
	// ************************************************************************

	public Skill getRequiredSkill() {
		return requiredSkill;
	}

	public void setRequiredSkillList(Skill requiredSkill) {
		this.requiredSkill = requiredSkill;
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
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getAisle() {
		return aisle;
	}

	public void setAisle(String aisle) {
		this.aisle = aisle;
	}

}
