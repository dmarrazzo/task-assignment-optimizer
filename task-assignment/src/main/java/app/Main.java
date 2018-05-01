package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import domain.Employee;
import domain.Skill;
import domain.Task;
import domain.TaskAssagnmentSolution;

public class Main {
	public void solve() {
		SolverFactory<TaskAssagnmentSolution> solverFactory = SolverFactory
				.createFromKieContainerXmlResource("solver/taskAssignmentSolverConfig.xml");
		Solver<TaskAssagnmentSolution> solver = solverFactory.buildSolver();

		TaskAssagnmentSolution unsolved = readProblemFacts();
		TaskAssagnmentSolution solved = solver.solve(unsolved);
		printSolution(solved);
	}

	private TaskAssagnmentSolution readProblemFacts() {
		TaskAssagnmentSolution taskAssagnmentSolution = new TaskAssagnmentSolution();
		Path inputFile = Paths.get("data/employees-3.txt");

		try (Stream<String> stream = Files.lines(inputFile)) {
			List<Employee> employees = stream.filter((line) -> !line.isEmpty() && !line.startsWith("#")).map((line) -> {
				String[] tokens = line.split(",");
				if (tokens.length < 2) {
					throw new IllegalStateException("The line (" + line + ") has less than 2 tokens.");
				}
				HashSet<Skill> skills = new HashSet<>();
				for (int i = 1; i < tokens.length; i++) {
					skills.add(new Skill(tokens[i]));
				}
				return new Employee(tokens[0], skills);
			}).collect(Collectors.toList());

			taskAssagnmentSolution.setEmployeeList(employees);

		} catch (IOException | NumberFormatException e) {
			throw new IllegalStateException("Reading inputFile (" + inputFile + ") failed.", e);
		}

		inputFile = Paths.get("data/tasks-8.txt");
		
		try (Stream<String> stream = Files.lines(inputFile)) {
			List<Task> tasks = stream.filter((line) -> !line.isEmpty() && !line.startsWith("#")).map((line) -> {
				String[] tokens = line.split(",");
				if (tokens.length < 4) {
					throw new IllegalStateException("The line (" + line + ") has less than 4 tokens.");
				}
				HashSet<Skill> skills = new HashSet<>();
				for (int i = 3; i < tokens.length; i++) {
					skills.add(new Skill(tokens[i]));
				}
				return new Task(tokens[0], Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), skills);
			}).collect(Collectors.toList());

			taskAssagnmentSolution.setTaskList(tasks);

		} catch (IOException | NumberFormatException e) {
			throw new IllegalStateException("Reading inputFile (" + inputFile + ") failed.", e);
		}

		return taskAssagnmentSolution;
	}
	
	private void printSolution(TaskAssagnmentSolution solution) {
		List<Employee> employees = solution.getEmployeeList();
		
		for (Employee employee : employees) {
			System.out.print(employee.getFullName() + " - ");
			Task task = employee.getNextTask();
			while (task != null) {
				System.out.print(task.getTaskType().getId()+ ", ");
				task = task.getNextTask();
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		
		main.solve();
	}
}
