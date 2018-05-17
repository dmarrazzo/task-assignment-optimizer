package app;

import java.io.File;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import domain.Employee;
import domain.Interval;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class Main {
	public void solve() {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml");
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution unsolved = ProblemBuilder.readProblemFacts("data/employees-3.txt", "data/tasks-8.txt");
		
		Set<Interval> gaps = new HashSet<>();
		Interval brief = new Interval(LocalTime.parse("06:30"), LocalTime.parse("07:00"));
		gaps.add(brief);
		unsolved.setGaps(gaps);
		
		TaskAssagnmentSolution solved = solver.solve(unsolved);
		printSolution(solved);
	}

	private void printSolution(TaskAssagnmentSolution solution) {
		List<Employee> employees = solution.getEmployeeList();

		for (Employee employee : employees) {
			System.out.print(employee.getFullName() + " - ");
			TaskPart taskPart = employee.getNextTaskPart();
			while (taskPart != null) {
				StringBuffer taskStr = new StringBuffer();

				taskStr.append(taskPart.getId());
				
				taskStr.append("!" + taskPart.getTask()
				                             .getPriority());
				
				taskStr.append(StringUtils.repeat("#", (int) taskPart.getDuration()
				                                                     .dividedBy(15)
				                                                     .toMinutes()));
				taskStr.append("[" + taskPart.getStartTime());

				taskStr.append("," + taskPart.getEndTime());

				taskStr.append("] ");
				System.out.print(taskStr);
				taskPart = taskPart.getNextTaskPart();
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
