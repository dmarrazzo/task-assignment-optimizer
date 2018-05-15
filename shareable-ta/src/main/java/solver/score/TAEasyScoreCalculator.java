package solver.score;

import java.util.List;

import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class TAEasyScoreCalculator implements EasyScoreCalculator<TaskAssagnmentSolution> {

	@Override
	public BendableScore calculateScore(TaskAssagnmentSolution solution) {
		int[] hardScores = new int[2];
		int[] softScores = new int[3];

		// init scores
		hardScores[0] = 0;
		hardScores[1] = 0;
		softScores[0] = 0;
		softScores[1] = 0;
		softScores[2] = 0;
		
		List<TaskPart> taskPartList = solution.getTaskPartList();
		
		// loop on the planning entities
		for (TaskPart taskPart : taskPartList) {
			
			// evaluate assigned task parts
			if (taskPart.getEmployee() != null) {

				//Skill requirements
				hardScores[0] -= taskPart.getMissingSkillCount();

				//High priority task must be accomplished on time
				if (taskPart.getTask().getPriority() == 1)
					hardScores[1] += taskPart.getOutOfTime(); // outOfTime is negative number

				//Minimze makespan (starting with the latest ending employee first)
				if (taskPart.getNextTaskPart() == null)
					softScores[0] -= taskPart.getElapsed() * taskPart.getElapsed();

				//Meet complention time for other priorities
				if (taskPart.getTask().getPriority() > 1)
					softScores[1] += taskPart.getOutOfTime();
				
				//Priority order (max priority is 6)
				// TODO the math stresses the int limits... it would be better to switch on BendableLongScore
				softScores[2] -= Math.pow(10, (7-taskPart.getTask().getPriority())) * taskPart.getElapsed()/15;
			}
		}		

		return BendableScore.valueOf(hardScores, softScores);
	}

}
