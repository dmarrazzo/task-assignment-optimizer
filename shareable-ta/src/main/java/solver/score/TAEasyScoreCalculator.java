package solver.score;

import java.time.LocalTime;
import java.util.List;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import domain.Task;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class TAEasyScoreCalculator implements EasyScoreCalculator<TaskAssagnmentSolution> {

	@Override
	public BendableScore calculateScore(TaskAssagnmentSolution solution) {
		int[] hardScores = new int[3];
		int[] softScores = new int[3];

		// init scores
		hardScores[0] = 0;
		hardScores[1] = 0;
		hardScores[2] = 0;
		softScores[0] = 0;
		softScores[1] = 0;
		softScores[2] = 0;

		//Hard constraint 0: Parts of a task mush have the same start time
		
		// loops on Tasks
		for (Task task : solution.getTaskList()) {
			if (task.getTaskParts().length > 1) {
				LocalTime startTimeP0 = task.getTaskParts()[0].getStartTime();
				for (int i = 1; i < task.getTaskParts().length; i++) {
					TaskPart taskPart = task.getTaskParts()[i];
					if (taskPart.getStartTime()!= null && taskPart.getStartTime().equals(startTimeP0))
						hardScores[0] -= 1;
				}
			}
		}

		// loop on the planning entities TaskPart

		List<TaskPart> taskPartList = solution.getTaskPartList();

		for (TaskPart taskPart : taskPartList) {
			
			// evaluate assigned task parts
			if (taskPart.getEmployee() != null) {
				
				//High priority task must be accomplished on time
				if (taskPart.getTask().getPriority() == 1)
					hardScores[2] += taskPart.getOutOfTime(); // outOfTime is negative number

				//Meet complention time for other priorities
				if (taskPart.getTask().getPriority() > 1)
					softScores[0] += taskPart.getOutOfTime();
				
				//Priority order (max priority is 6)
				// TODO the math stresses the int limits... it would be better to switch on BendableLongScore
				softScores[1] -= Math.pow(10, (7-taskPart.getTask().getPriority())) * taskPart.getElapsed()/15;

				//Minimze makespan (starting with the latest ending employee first)
				if (taskPart.getNextTaskPart() == null)
					softScores[2] -= taskPart.getElapsed() * taskPart.getElapsed();

			}
		}		

		return BendableScore.valueOf(hardScores, softScores);
	}

}
