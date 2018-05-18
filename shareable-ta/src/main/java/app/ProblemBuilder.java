package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import domain.Employee;
import domain.Skill;
import domain.Task;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class ProblemBuilder {
	public static TaskAssagnmentSolution readProblemFacts(String employeeFile, String taskFile) {
		TaskAssagnmentSolution taskAssagnmentSolution = new TaskAssagnmentSolution();
		Path inputFile = Paths.get(employeeFile);

		try (Stream<String> stream = Files.lines(inputFile)) {
			List<Employee> employees = stream.filter((line) -> !line.isEmpty() && !line.startsWith("#"))
			                                 .map((line) -> {
				                                 String[] tokens = line.split(",");
				                                 if (tokens.length < 2) {
					                                 throw new IllegalStateException(
					                                         "The line (" + line + ") has less than 2 tokens.");
				                                 }
				                                 HashSet<Skill> skills = new HashSet<>();
				                                 for (int i = 1; i < tokens.length; i++) {
					                                 skills.add(new Skill(tokens[i]));
				                                 }
				                                 return new Employee(tokens[0], skills);
			                                 })
			                                 .collect(Collectors.toList());

			taskAssagnmentSolution.setEmployeeList(employees);

		} catch (IOException | NumberFormatException e) {
			throw new IllegalStateException("Reading inputFile (" + inputFile + ") failed.", e);
		}

		inputFile = Paths.get(taskFile);

		try (Stream<String> stream = Files.lines(inputFile)) {
			List<Task> tasks = stream.filter((line) -> !line.isEmpty() && !line.startsWith("#"))
			                         .map((line) -> {
				                         String[] tokens = line.split(",");
				                         if (tokens.length < 6) {
					                         throw new IllegalStateException(
					                                 "The line (" + line + ") has less than 4 tokens.");
				                         }
				                         HashSet<Skill> skills = new HashSet<>();
				                         for (int i = 5; i < tokens.length; i++) {
					                         skills.add(new Skill(tokens[i]));
				                         }
				                         return new Task(tokens[0], Duration.ofMinutes(Long.parseLong(tokens[1])),
				                                 LocalTime.parse(tokens[2]), Integer.parseInt(tokens[3]),
				                                 Integer.parseInt(tokens[4]), skills);
			                         })
			                         .collect(Collectors.toList());

			taskAssagnmentSolution.setTaskList(tasks);

		} catch (IOException | NumberFormatException e) {
			throw new IllegalStateException("Reading inputFile (" + inputFile + ") failed.", e);
		}

		return taskAssagnmentSolution;
	}
	
	public static void printSolution(TaskAssagnmentSolution solution) {
		List<Employee> employees = solution.getEmployeeList();

		for (Employee employee : employees) {
			System.out.print(employee.getFullName() + " - ");
			TaskPart taskPart = employee.getNextTaskPart();
			while (taskPart != null) {
				StringBuffer taskStr = new StringBuffer();

				taskStr.append(taskPart.getId());

				taskStr.append("!" + taskPart.getTask()
				                             .getPriority());

				taskStr.append("[" + taskPart.getStartTime());

				taskStr.append("," + taskPart.getEndTime());

				taskStr.append("] ");
				System.out.print(taskStr);
				taskPart = taskPart.getNextTaskPart();
			}
			System.out.println();
		}
	}

	public static void save(TaskAssagnmentSolution solution, String fileName) {
		SolutionFileIO<TaskAssagnmentSolution> fileIO = new XStreamSolutionFileIO<TaskAssagnmentSolution>();
		fileIO.write(solution, new File(fileName));
	}

	public static TaskAssagnmentSolution load(String fileName) {
		SolutionFileIO<TaskAssagnmentSolution> fileIO = new XStreamSolutionFileIO<TaskAssagnmentSolution>();
		return fileIO.read(new File(fileName));
	}

}
