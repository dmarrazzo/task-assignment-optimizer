package domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import domain.solver.PreviousTaskPartOrEmployeeStrengthComparator;
import domain.solver.StartTimeUpdatingVariableListener;
import domain.solver.TaskPartDifficultyComparator;

@XStreamAlias("TaTaskPart")
@PlanningEntity(difficultyComparatorClass = TaskPartDifficultyComparator.class)
public class TaskPart extends TaskPartOrEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@PlanningVariable(valueRangeProviderRefs = { "employeeRange",
	        "taskPartRange" }, graphType = PlanningVariableGraphType.CHAINED, strengthComparatorClass = PreviousTaskPartOrEmployeeStrengthComparator.class)
	private TaskPartOrEmployee previousTaskPartOrEmployee;

	@AnchorShadowVariable(sourceVariableName = "previousTaskPartOrEmployee")
	private Employee employee;

	@CustomShadowVariable(variableListenerClass = StartTimeUpdatingVariableListener.class, sources = {
	        @PlanningVariableReference(variableName = "previousTaskPartOrEmployee") })
	private LocalTime startTime; // e.g. 08:20

	private String id;

	private Task task;

	private Duration duration;

	public TaskPart() {
	}

	@Override
	public LocalTime getEndTime() {
		if (getStartTime() == null)
			return null;
		return getStartTime().plus(getDuration());
	}

	/**
	 * it returns the minutes worked by the employee when he finish the task part
	 * 
	 * @return
	 */
	public int getElapsed() {
		int between = 0;

		if (employee != null && getEndTime() != null)
			between = (int) ChronoUnit.MINUTES.between(employee.getStartTime(), getEndTime());
		return between;
	}

	/**
	 * If the task part is performed on time it return 0. Otherwise it returns the
	 * number of minutes between the end time of the task part ant the completion
	 * time.
	 * 
	 * @return minutes of out of time (negative value)
	 */
	public int getOutOfTime() {
		int between = 0;

		if (getEndTime() != null && task.getCompletionTime() != null)
			between = (int) ChronoUnit.MINUTES.between(getEndTime(), task.getCompletionTime());

		if (between < 0)
			return between;
		else
			return 0;
	}

	/**
	 * 
	 * @param taskPart
	 * @return the difference in minutes between task part <code>taskPart</code> and <code>this</code>
	 */
	public int offSet(TaskPart taskPart) {
		return (int) Math.abs(ChronoUnit.MINUTES.between(startTime, taskPart.getStartTime()));
	}
	
	public int getMissingSkillCount() {
		if (employee == null) {
			return 0;
		}
		int count = 0;
		for (Skill skill : getTask().getRequiredSkillList()) {
			if (!employee.getSkillSet()
			             .contains(skill)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String toString() {
		return "TaskPart [" + id + ", prev=" + getPreviousTaskPartOrEmployee().getId() +", d=" + duration + ", task=" + task + "]";
	}

	// ************************************************************************
	// Getters / Setters
	// ************************************************************************

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public TaskPartOrEmployee getPreviousTaskPartOrEmployee() {
		return previousTaskPartOrEmployee;
	}

	public void setPreviousTaskPartOrEmployee(TaskPartOrEmployee previousTaskPartOrEmployee) {
		this.previousTaskPartOrEmployee = previousTaskPartOrEmployee;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

}
