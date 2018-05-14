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
	 * @param sourceTaskPart
	 */
    protected void updateStartTime(ScoreDirector scoreDirector, TaskPart sourceTaskPart) {
        TaskPartOrEmployee previous = sourceTaskPart.getPreviousTaskPartOrEmployee();
        
        // get the end time of the previous Task 
        LocalTime previousEndTime = (previous == null ? null : previous.getEndTime());
        LocalTime startTime = previousEndTime;
        
        TaskPart shadowTaskPart = sourceTaskPart;
        
        // loop on the chain following nextTaskPart relantionship
        while (shadowTaskPart != null && !Objects.equals(shadowTaskPart.getTask().getStartTime(), startTime)) {
            scoreDirector.beforeVariableChanged(shadowTaskPart, "startTime");
            shadowTaskPart.getTask().setStartTime(startTime);
            scoreDirector.afterVariableChanged(shadowTaskPart, "startTime");
            previousEndTime = shadowTaskPart.getEndTime();
            shadowTaskPart = shadowTaskPart.getNextTaskPart();
            startTime = previousEndTime;
        }
    }

}
