package domain.solver;

import java.util.Objects;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import domain.Task;
import domain.TaskOrEmployee;

@SuppressWarnings("rawtypes")
public class StartTimeUpdatingVariableListener implements VariableListener<Task> {

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, Task task) {
        updateStartTime(scoreDirector, task);
	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, Task task) {
		// NOP
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, Task task) {
        updateStartTime(scoreDirector, task);
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, Task task) {
		// NOP
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, Task task) {
		// NOP
	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, Task task) {
		// NOP
	}

	/**
	 * Update the start time of the tasks that follow the sourceTask in the chain
	 * 
	 * @param scoreDirector
	 * @param sourceTask
	 */
    protected void updateStartTime(ScoreDirector scoreDirector, Task sourceTask) {
        TaskOrEmployee previous = sourceTask.getPreviousTaskOrEmployee();
        
        // get the end time of the previous Task 
        Integer previousEndTime = (previous == null ? null : previous.getEndTime());
        
        Task shadowTask = sourceTask;
        Integer startTime = calculateStartTime(shadowTask, previousEndTime);
        
        // loop on the chain following nextTask relantionship
        while (shadowTask != null && !Objects.equals(shadowTask.getStartTime(), startTime)) {
            scoreDirector.beforeVariableChanged(shadowTask, "startTime");
            shadowTask.setStartTime(startTime);
            scoreDirector.afterVariableChanged(shadowTask, "startTime");
            previousEndTime = shadowTask.getEndTime();
            shadowTask = shadowTask.getNextTask();
            startTime = calculateStartTime(shadowTask, previousEndTime);
        }
    }

    private Integer calculateStartTime(Task task, Integer previousEndTime) {
        return previousEndTime;
    }
}
