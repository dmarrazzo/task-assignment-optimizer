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

	@CustomShadowVariable(variableListenerClass = StartTimeUpdatingVariableListener.class,
	        // Arguable, to adhere to API specs (although this works), nextTask
	        // and employee should also be a source,
	        // because this shadow must be triggered after nextTask and employee
	        // (but there is no need to be triggered by those)
	        sources = { @PlanningVariableReference(variableName = "previousTaskPartOrEmployee") })
	private LocalTime startTime;

	private String id;

	private Task task;

	// TODO Shadow
	private Duration duration;

	// TODO when active
	private boolean active;

	public TaskPart() {
	}

	// @Override
	// public String toString() {
	// String previousId = "NA";
	// if (previousTaskOrEmployee != null)
	// previousId = previousTaskOrEmployee.getId();
	// return String.format("Task [ %s, employee=%s, startTime=%s,
	// previousTaskOrEmployee=%s]", taskType, employee,
	// startTime, previousId);
	// }

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

	public long getOutOfTime() {
		long between = ChronoUnit.MINUTES.between(getEndTime(), task.getCompletionTime());
		if (between < 0)
			return between;
		else
			return 0L;
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

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

}
