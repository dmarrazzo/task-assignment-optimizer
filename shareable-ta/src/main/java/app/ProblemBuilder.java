package app;

import java.io.File;
import java.util.List;

import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import domain.Employee;
import domain.Task;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class ProblemBuilder {
	public static TaskAssagnmentSolution readProblemFacts(String employeeFile, String taskFile) {
		TaskAssagnmentSolution taskAssagnmentSolution = new TaskAssagnmentSolution();
		
		
		List<Employee> employees =  CsvReaderBuilder.readEmployees(employeeFile);
		taskAssagnmentSolution.setEmployeeList(employees);
		
		
		List<Task> tasks = CsvReaderBuilder.readTasks(taskFile);
		taskAssagnmentSolution.setTaskList(tasks);

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
