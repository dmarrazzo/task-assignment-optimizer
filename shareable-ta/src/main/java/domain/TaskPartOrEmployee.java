package domain;

import java.io.Serializable;
import java.time.LocalTime;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public abstract class TaskPartOrEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@InverseRelationShadowVariable(sourceVariableName = "previousTaskPartOrEmployee")
	private TaskPart nextTask;

	public abstract String getId();

	/**
	 * @return sometimes null
	 */
	public abstract LocalTime getEndTime();

	/**
	 * @return sometimes null
	 */
	public abstract Employee getEmployee();

	public TaskPart getNextTask() {
		return nextTask;
	}

	public void setNextTask(TaskPart nextTask) {
		this.nextTask = nextTask;
	}

}
