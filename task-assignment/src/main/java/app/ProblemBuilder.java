package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import domain.Employee;
import domain.Skill;
import domain.Task;
import domain.TaskAssagnmentSolution;

public class ProblemBuilder {
	public static TaskAssagnmentSolution readProblemFacts(String employeeFile, String taskFile) {
		TaskAssagnmentSolution taskAssagnmentSolution = new TaskAssagnmentSolution();
		Path inputFile = Paths.get(employeeFile);

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

		inputFile = Paths.get(taskFile);
		
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

}
