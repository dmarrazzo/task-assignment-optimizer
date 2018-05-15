package score;

import java.util.List;

import org.junit.Test;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import app.ProblemBuilder;
import domain.Employee;
import domain.Task;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;
import solver.score.TAEasyScoreCalculator;

public class EasyScoreTest {
	
	@Test
	public void checkPriority() {
		TAEasyScoreCalculator calculator = new TAEasyScoreCalculator();
		BendableScore score;

		TaskAssagnmentSolution solution = ProblemBuilder.readProblemFacts("data/employees-3.txt", "data/tasks-8.txt");

		List<Task> taskList = solution.getTaskList();
		TaskPart taskPart1 = taskList.get(0).getTaskParts()[0];
		TaskPart taskPart4 = taskList.get(3).getTaskParts()[0];
		TaskPart taskPart5 = taskList.get(4).getTaskParts()[0];
		Employee emp1 = solution.getEmployeeList().get(0);

		// 1-0!1>05:30## 5-0!5>06:00## 4-0!4>07:30######

		taskPart1.setPreviousTaskPartOrEmployee(emp1);
		taskPart1.setEmployee(emp1);
		taskPart1.getTask().setStartTime(emp1.getEndTime());
		taskPart1.setNextTaskPart(taskPart5);
		
		taskPart5.setPreviousTaskPartOrEmployee(taskPart1);
		taskPart5.setEmployee(emp1);
		taskPart5.getTask().setStartTime(taskPart5.getPreviousTaskPartOrEmployee().getEndTime());
		taskPart5.setNextTaskPart(taskPart4);

		taskPart4.setPreviousTaskPartOrEmployee(taskPart5);
		taskPart4.setEmployee(emp1);
		taskPart4.getTask().setStartTime(taskPart4.getPreviousTaskPartOrEmployee().getEndTime());
		taskPart4.setNextTaskPart(null);

		score = calculator.calculateScore(solution);
		System.out.println(score);

		// 1-0!1>05:30## 5-0!5>06:00## 4-0!4>07:30######

		taskPart1.setPreviousTaskPartOrEmployee(emp1);
		taskPart1.setEmployee(emp1);
		taskPart1.getTask().setStartTime(emp1.getEndTime());
		taskPart1.setNextTaskPart(taskPart4);

		taskPart4.setPreviousTaskPartOrEmployee(taskPart1);
		taskPart4.setEmployee(emp1);
		taskPart4.getTask().setStartTime(taskPart4.getPreviousTaskPartOrEmployee().getEndTime());
		taskPart4.setNextTaskPart(taskPart5);

		taskPart5.setPreviousTaskPartOrEmployee(taskPart4);
		taskPart5.setEmployee(emp1);
		taskPart5.getTask().setStartTime(taskPart5.getPreviousTaskPartOrEmployee().getEndTime());
		taskPart5.setNextTaskPart(null);

		score = calculator.calculateScore(solution);
		System.out.println(score);

	}
}
