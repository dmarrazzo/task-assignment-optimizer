package domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import domain.solver.PreviousTaskPartOrEmployeeStrengthComparator;
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

	private String id;

	private Task task;

	// TODO Shadow
	private Duration duration;

	// TODO when active
	private boolean active;

	public TaskPart() {
	}

	@Override
	public LocalTime getEndTime() {
		if (getTask().getStartTime() == null)
			return null;
		return getTask().getStartTime()
		                .plus(getDuration());
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
	public Employee getEmployee() {
		return previousTaskPartOrEmployee.getEmployee();
	}

	public int getOutOfTime() {
		int between = (int) ChronoUnit.MINUTES.between(getEndTime(), task.getCompletionTime());
		if (between < 0)
			return between;
		else
			return 0;
	}

	@Override
	public String toString() {
		return "TaskPart [id=" + id + ", employee=" + employee + ", task=" + task
		        + ", duration=" + duration + ", active=" + active + "]";
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

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
		this.getTask().setStartTime(previousTaskPartOrEmployee.getEndTime());
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

}
