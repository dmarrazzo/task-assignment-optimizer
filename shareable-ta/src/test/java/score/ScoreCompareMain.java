package score;

import java.time.LocalTime;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;

import app.ProblemBuilder;
import domain.Employee;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;
import domain.TaskPartOrEmployee;

public class ScoreCompareMain {

	public static void main(String[] args) {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml");
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution orig = ProblemBuilder.load("data/emp-3-task-8.xml");
		TaskAssagnmentSolution mod; // = ProblemBuilder.load("data/emp-3-task-8-mod.xml");

		ScoreDirectorFactory<TaskAssagnmentSolution> scoreDirectorFactory = solver.getScoreDirectorFactory();
		try (ScoreDirector<TaskAssagnmentSolution> guiScoreDirector = scoreDirectorFactory.buildScoreDirector()) {
			guiScoreDirector.setWorkingSolution(orig);
			ProblemBuilder.printSolution(orig);
			System.out.println("orig: " + guiScoreDirector.calculateScore());

			mod = orig;

			swap(mod, "07-2", "08-0");
			ProblemBuilder.printSolution(mod);
			guiScoreDirector.setWorkingSolution(mod);
			System.out.println("mod:  " + guiScoreDirector.calculateScore());
		}

	}

	private static void swap(TaskAssagnmentSolution solution, String taskId1, String taskId2) {

		TaskPart taskPart1 = findTaskPart(solution, taskId1);
		TaskPart taskPart2 = findTaskPart(solution, taskId2);

		TaskPartOrEmployee previousTaskPartOrEmployee = taskPart1.getPreviousTaskPartOrEmployee();
		LocalTime startTime = taskPart1.getStartTime();
		Employee employee = taskPart1.getEmployee();
		TaskPart nextTaskPart = taskPart1.getNextTaskPart();

		taskPart1.setPreviousTaskPartOrEmployee(taskPart2.getPreviousTaskPartOrEmployee());
		taskPart1.setStartTime(taskPart2.getStartTime());
		taskPart1.setEmployee(taskPart2.getEmployee());
		taskPart1.setNextTaskPart(taskPart2.getNextTaskPart());
		taskPart1.getPreviousTaskPartOrEmployee()
		         .setNextTaskPart(taskPart1);
		if (taskPart1.getNextTaskPart() != null)
			taskPart1.getNextTaskPart()
			         .setPreviousTaskPartOrEmployee(taskPart1);

		taskPart2.setPreviousTaskPartOrEmployee(previousTaskPartOrEmployee);
		taskPart2.setStartTime(startTime);
		taskPart2.setEmployee(employee);
		taskPart2.setNextTaskPart(nextTaskPart);
		taskPart2.getPreviousTaskPartOrEmployee()
		         .setNextTaskPart(taskPart2);
		if (taskPart2.getNextTaskPart() != null)
			taskPart2.getNextTaskPart()
			         .setPreviousTaskPartOrEmployee(taskPart2);

	}

	private static TaskPart findTaskPart(TaskAssagnmentSolution solution, String taskId) {
		for (TaskPart taskPart : solution.getTaskPartList()) {
			if (taskPart.getId()
			            .equals(taskId)) {
				return taskPart;
			}
		}
		return null;
	}

}
