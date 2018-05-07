package app;

import java.io.File;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import domain.TaskAssagnmentSolution;

public class Benchmark {

	public static void main(String[] args) {

		PlannerBenchmarkFactory plannerBenchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource(
				"benchmark/config.xml");
		PlannerBenchmark plannerBenchmark = plannerBenchmarkFactory.buildPlannerBenchmark();

		plannerBenchmark.benchmark();
	}
}
