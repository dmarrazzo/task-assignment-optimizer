package domain.solver;

import java.time.LocalTime;
import java.util.Objects;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import domain.Interval;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;
import domain.TaskPartOrEmployee;

@SuppressWarnings("rawtypes")
public class StartTimeUpdatingVariableListener implements VariableListener<TaskPart> {

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, TaskPart task) {
		updateStartTime(scoreDirector, task);
	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, TaskPart task) {
		// NOP
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, TaskPart task) {
		updateStartTime(scoreDirector, task);
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, TaskPart task) {
		// NOP
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, TaskPart task) {
		// NOP
	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, TaskPart task) {
		// NOP
	}

	/**
	 * Update the start time of the tasks that follow the sourceTask in the chain
	 * 
	 * @param scoreDirector
	 * @param sourceTaskPart
	 */
	protected void updateStartTime(ScoreDirector scoreDirector, TaskPart sourceTaskPart) {
		TaskPartOrEmployee previous = sourceTaskPart.getPreviousTaskPartOrEmployee();

		// get the end time of the previous Task
		LocalTime previousReadyTime = (previous == null ? null : previous.getReadyTime());
		LocalTime startTime = previousReadyTime;

		TaskPart shadowTaskPart = sourceTaskPart;

		// loop on the chain following nextTaskPart relantionship
		while (shadowTaskPart != null && !Objects.equals(shadowTaskPart.getStartTime(), startTime)) {
			scoreDirector.beforeVariableChanged(shadowTaskPart, "startTime");
			setGapsAwareStartTime(scoreDirector, shadowTaskPart, startTime);
			scoreDirector.afterVariableChanged(shadowTaskPart, "startTime");
			previousReadyTime = shadowTaskPart.getReadyTime();
			shadowTaskPart = shadowTaskPart.getNextTaskPart();
			startTime = previousReadyTime;
		}
	}

	/**
	 * This method set the start time of the task considering the gap constraint.
	 * Gap constraint: the task cannot be scheduled during a scheduled gap
	 * 
	 * Differently from other constraints that are backed by the scoring funtion, here the constraint is enforced in the model.
	 * This has two benefits:
	 * 1. Avoiding to introduce a special entity in the model to fill the gap
	 * 2. Left an empty space before the gap (the space between the previous task and the gap). Potentially, the solver can fit another task to fill it. 
	 * 
	 * @param scoreDirector
	 * @param shadowTaskPart
	 * @param startTime
	 */
	private void setGapsAwareStartTime(ScoreDirector scoreDirector, TaskPart shadowTaskPart, LocalTime startTime) {
		if (startTime != null) {
			TaskAssagnmentSolution solution = (TaskAssagnmentSolution) scoreDirector.getWorkingSolution();

			shadowTaskPart.setStartTime(startTime);
			// for each gap
			for (Interval gap : solution.getGaps()) {
				// if the task overlap the gap move it at the end of the gap 
				if (gap != null && gap.overlap(startTime, shadowTaskPart.getDuration()))
					shadowTaskPart.setStartTime(gap.getEnd());
			}
		}
	}

}
