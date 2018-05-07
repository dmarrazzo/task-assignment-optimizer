package app;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.solver.SolverFactory;

import domain.TaskAssagnmentSolution;

public class Benchmark {

	public static void main(String[] args) {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml");
        PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
        
		TaskAssagnmentSolution dataset1 = ProblemBuilder.readProblemFacts("data/employees-3.txt","data/tasks-8.txt");
		TaskAssagnmentSolution dataset2 = ProblemBuilder.readProblemFacts("data/employees-10.txt","data/tasks-40.txt");

        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(dataset1, dataset2);
        plannerBenchmark.benchmark();
	}
}
