package domain;

import java.io.Serializable;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public abstract class TaskOrEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@InverseRelationShadowVariable(sourceVariableName = "previousTaskOrEmployee")
	private Task nextTask;

	public abstract String getId();

	/**
	 * @return sometimes null
	 */
	public abstract Integer getEndTime();

	/**
	 * @return sometimes null
	 */
	public abstract Employee getEmployee();

	public Task getNextTask() {
		return nextTask;
	}

	public void setNextTask(Task nextTask) {
		this.nextTask = nextTask;
	}

}
