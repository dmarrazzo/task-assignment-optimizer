package app;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import domain.Interval;
import domain.TaskAssagnmentSolution;

public class Main {
	public void solve(String employees, String tasks, boolean save) {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml");
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution unsolved = ProblemBuilder.readProblemFacts(String.format("data/employees-%s.txt", employees), String.format("data/tasks-%s.txt", tasks));

		Set<Interval> gaps = new HashSet<>();
		Interval brief = new Interval(LocalTime.parse("08:00"), LocalTime.parse("08:30"));
		gaps.add(brief);
		unsolved.setGaps(gaps);

		ProblemBuilder.save(unsolved, String.format("data/emp-%s-task-%s.xml",employees,tasks));

		
		TaskAssagnmentSolution solved = solver.solve(unsolved);
		ProblemBuilder.printSolution(solved);

		ProblemBuilder.save(solved, String.format("data/emp-%s-task-%s-solved.xml",employees,tasks));
	}

	public static void main(String[] args) {
		Main main = new Main();
		boolean save = false;
		if (args.length == 3)
			save=args[2].equalsIgnoreCase("save");
		
		if (args.length >= 2)
			main.solve(args[0], args[1], save);
		else
			main.solve("3", "8", save);

	}
}
