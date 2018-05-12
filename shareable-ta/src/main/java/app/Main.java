package app;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import domain.Employee;
import domain.TaskPart;
import domain.TaskAssagnmentSolution;

public class Main {
	public void solve() {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml");
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution unsolved = ProblemBuilder.readProblemFacts("data/employees-10.txt", "data/tasks-100.txt");
		TaskAssagnmentSolution solved = solver.solve(unsolved);
		printSolution(solved);
	}

	private void printSolution(TaskAssagnmentSolution solution) {
		List<Employee> employees = solution.getEmployeeList();

		for (Employee employee : employees) {
			System.out.print(employee.getFullName() + " - ");
			TaskPart taskPart = employee.getNextTask();
			while (taskPart != null) {
				StringBuffer taskStr = new StringBuffer();

				taskStr.append("!" + taskPart.getTask()
				                             .getPriority());
				taskStr.append(StringUtils.repeat("#", (int) taskPart.getTask()
				                                                     .getEffort()
				                                                     .dividedBy(15)
				                                                     .toMinutes()));
				taskStr.append(" ");
				System.out.print(taskStr);
				taskPart = taskPart.getNextTask();
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Main main = new Main();

		main.solve();
	}

	@SuppressWarnings("unused")
	private void serialize() {
		TaskAssagnmentSolution unsolved = ProblemBuilder.readProblemFacts("data/employees-10.txt", "data/tasks-100.txt");
		SolutionFileIO<TaskAssagnmentSolution> fileIO = new XStreamSolutionFileIO<TaskAssagnmentSolution>();
		fileIO.write(unsolved, new File("data/emp-10-task-100.xml"));
	}
}
