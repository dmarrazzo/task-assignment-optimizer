package domain.solver;

import java.time.LocalTime;
import java.util.Objects;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

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
	 * @param sourceTask
	 */
    protected void updateStartTime(ScoreDirector scoreDirector, TaskPart sourceTask) {
        TaskPartOrEmployee previous = sourceTask.getPreviousTaskPartOrEmployee();
        
        // get the end time of the previous Task 
        LocalTime previousEndTime = (previous == null ? null : previous.getEndTime());
        LocalTime startTime = previousEndTime;
        
        TaskPart shadowTask = sourceTask;
        
        // loop on the chain following nextTask relantionship
        while (shadowTask != null && !Objects.equals(shadowTask.getTask().getStartTime(), startTime)) {
            scoreDirector.beforeVariableChanged(shadowTask, "startTime");
            shadowTask.getTask().setStartTime(startTime);
            scoreDirector.afterVariableChanged(shadowTask, "startTime");
            previousEndTime = shadowTask.getEndTime();
            shadowTask = shadowTask.getNextTask();
            startTime = previousEndTime;
        }
    }

}
