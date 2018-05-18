package app;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import domain.Interval;
import domain.TaskAssagnmentSolution;

public class Main {
	public void solve() {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml");
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution unsolved = ProblemBuilder.readProblemFacts("data/employees-3.txt", "data/tasks-8.txt");

		Set<Interval> gaps = new HashSet<>();
		Interval brief = new Interval(LocalTime.parse("08:00"), LocalTime.parse("08:30"));
		gaps.add(brief);
		unsolved.setGaps(gaps);

		TaskAssagnmentSolution solved = solver.solve(unsolved);
		ProblemBuilder.printSolution(solved);
		
		// ProblemBuilder.save(solved, "data/emp-3-task-8.xml");
	}

	public static void main(String[] args) {
		Main main = new Main();

		main.solve();
	}
}
