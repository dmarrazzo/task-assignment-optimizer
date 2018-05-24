package domain;

import java.io.Serializable;
import java.time.LocalTime;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public abstract class TaskPartOrEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@InverseRelationShadowVariable(sourceVariableName = "previousTaskPartOrEmployee")
	private TaskPart nextTaskPart;

	public abstract String getId();

	/**
	 * @return sometimes null
	 */
	public abstract LocalTime getReadyTime();

	/**
	 * @return sometimes null
	 */
	public abstract Employee getEmployee();

	public TaskPart getNextTaskPart() {
		return nextTaskPart;
	}

	public void setNextTaskPart(TaskPart nextTaskPart) {
		this.nextTaskPart = nextTaskPart;
	}

}
