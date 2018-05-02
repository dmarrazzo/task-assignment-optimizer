package domain;

import java.io.Serializable;
import java.util.HashSet;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import domain.solver.StartTimeUpdatingVariableListener;
import domain.solver.TaskDifficultyComparator;

@PlanningEntity(difficultyComparatorClass = TaskDifficultyComparator.class)
public class Task extends TaskOrEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@PlanningVariable(valueRangeProviderRefs = { "employeeRange",
			"taskRange" }, graphType = PlanningVariableGraphType.CHAINED)
	private TaskOrEmployee previousTaskOrEmployee;

	@AnchorShadowVariable(sourceVariableName = "previousTaskOrEmployee")
	private Employee employee;

	private TaskType taskType;

	@CustomShadowVariable(variableListenerClass = StartTimeUpdatingVariableListener.class,
			// Arguable, to adhere to API specs (although this works), nextTask and employee
			// should also be a source,
			// because this shadow must be triggered after nextTask and employee (but there
			// is no need to be triggered by those)
			sources = { @PlanningVariableReference(variableName = "previousTaskOrEmployee") })
	private Integer startTime; // In minutes

	public Task() {
	}
	
	public Task(String id, int priority, int duration, HashSet<Skill> skills) {
		taskType = new TaskType();
		taskType.setId(id);
		taskType.setPriority(Priority.fromValue(priority));
		taskType.setDuration(duration);
		taskType.setRequiredSkillList(skills);
	}

	// ************************************************************************
	// Getters / Setters
	// ************************************************************************

	public TaskOrEmployee getPreviousTaskOrEmployee() {
		return previousTaskOrEmployee;
	}

	public void setPreviousTaskOrEmployee(TaskOrEmployee previousTaskOrEmployee) {
		this.previousTaskOrEmployee = previousTaskOrEmployee;
	}

	@Override
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}

	public void setPriority(Priority priority) {
		this.taskType.setPriority(priority);
	}
	
	public Priority getPriority() {
		return this.taskType.getPriority();
	}

	// ************************************************************************
	// Complex methods
	// ************************************************************************

	@Override
	public String toString() {
		String previousId = "NA";
		if (previousTaskOrEmployee != null)
			previousId = previousTaskOrEmployee.getId();
		return String.format("Task [ %s, employee=%s, startTime=%s, previousTaskOrEmployee=%s]",
				taskType, employee, startTime, previousId);
	}
	
	@Override
	public Integer getEndTime() {
		if (getStartTime() == null)
			return null;
		return getStartTime() + taskType.getDuration();
	}

	public int getMissingSkillCount() {
        if (employee == null) {
            return 0;
        }
        int count = 0;
        for (Skill skill : taskType.getRequiredSkillList()) {
            if (!employee.getSkillSet().contains(skill)) {
                count++;
            }
        }
        return count;
	}

	@Override
	public String getId() {
		return "task "+taskType.getId();
	}

}
