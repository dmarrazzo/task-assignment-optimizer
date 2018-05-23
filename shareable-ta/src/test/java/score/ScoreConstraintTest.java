package score;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.test.impl.score.buildin.bendable.BendableScoreVerifier;

import domain.Employee;
import domain.Skill;
import domain.Task;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;

public class ScoreConstraintTest {
	private BendableScoreVerifier<TaskAssagnmentSolution> scoreVerifier = new BendableScoreVerifier<>(
	        SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml"));

	@Test
	public void keepPartsTogether() {
		TaskAssagnmentSolution solution = new TaskAssagnmentSolution();

		List<Employee> employeeList = new ArrayList<>();
		Set<Skill> skills = new HashSet<>();
		skills.add(new Skill("reading",0));
		Employee employee = new Employee("emp1",LocalTime.parse("05:00"),LocalTime.parse("20:00"), skills);
		employeeList.add(employee);
		solution.setEmployeeList(employeeList);

		List<Task> taskList = new ArrayList<>();
		Task task = new Task("aisle 1","cat 1", new Float(90),new Integer("0"), LocalTime.parse("10:00"), 1);
		
		TaskPart[] taskParts = task.getTaskParts();
		taskParts[0].setPreviousTaskPartOrEmployee(employee);
		taskParts[0].setEmployee(employee);
		taskParts[0].setNextTaskPart(taskParts[1]);
		taskParts[1].setPreviousTaskPartOrEmployee(taskParts[0]);
		taskParts[1].setEmployee(employee);
		taskParts[1].setNextTaskPart(null);
		
		taskList.add(task);
		solution.setTaskList(taskList);

		scoreVerifier.assertHardWeight("Same employee - Keep parts together", 0, 0, solution);
	}

	@Test
	public void tasks() {
		TaskAssagnmentSolution solution = new TaskAssagnmentSolution();

		List<Employee> employeeList = new ArrayList<>();
		Set<Skill> skills = new HashSet<>();
		skills.add(new Skill("reading",0));
		Employee employee = new Employee("emp1",LocalTime.parse("05:00"),LocalTime.parse("20:00"), skills);
		employeeList.add(employee);
		solution.setEmployeeList(employeeList);

		List<Task> taskList = new ArrayList<>();
		Task task = new Task("aisle 1","cat 1", new Float(90),new Integer("0"), LocalTime.parse("10:00"), 3);
		task.getTaskParts()[0].setPreviousTaskPartOrEmployee(employee);
		task.getTaskParts()[0].setEmployee(employee);
		taskList.add(task);
		solution.setTaskList(taskList);
		// Uninitialized
		scoreVerifier.assertHardWeight("Skill requirements", 0, 0, solution);

		// No skill
		employee.setSkillSet(new HashSet<>());
		scoreVerifier.assertHardWeight("Skill requirements", 0, -1, solution);
		employee.setSkillSet(skills);

		// High priority task must be accomplished on time
		scoreVerifier.assertHardWeight("High priority task must be accomplished on time", 1, 0, solution);

		// Break: High priority task must be accomplished on time
		employee.setStartTime(LocalTime.parse("09:30"));
		task.getTaskParts()[0].setPreviousTaskPartOrEmployee(employee);

		// shadow start time
		task.getTaskParts()[0].setStartTime(employee.getEndTime());

		scoreVerifier.assertHardWeight("High priority task must be accomplished on time", 1, -60, solution);
	}

	public void checkOtherPriorities() {
		// Minimze makespan (starting with the latest ending employee first)/[TaskPart
		// [id=8-0, employee=Employee [fullName=emp3, startTime=05:00,
		// skillSet=[reading]], task=domain.Task@538cd0f2,
		// duration=PT30M]=[0/0]hard/[-44100/0]soft
		// solver/Other priorities/[TaskPart [id=8-0, employee=Employee [fullName=emp3,
		// startTime=05:00, skillSet=[reading]], task=domain.Task@538cd0f2,
		// duration=PT30M]]=[0/0]hard/[0/-360]soft
		TaskAssagnmentSolution solution = new TaskAssagnmentSolution();

		List<Employee> employeeList = new ArrayList<>();
		Set<Skill> skills = new HashSet<>();
		skills.add(new Skill("reading",0));
		Employee emp1 = new Employee("emp1",LocalTime.parse("05:00"),LocalTime.parse("20:00"), skills);
		Employee emp2 = new Employee("emp2",LocalTime.parse("05:00"),LocalTime.parse("20:00"), skills);
		employeeList.add(emp1);
		employeeList.add(emp2);
		solution.setEmployeeList(employeeList);

		List<Task> taskList = new ArrayList<>();
		Task task1 = new Task("aisle 1","cat 1", new Float(90),new Integer("0"), LocalTime.parse("10:00"), 1);

		task1.getTaskParts()[0].setPreviousTaskPartOrEmployee(emp1);
		task1.getTaskParts()[0].setEmployee(emp1);
		task1.getTaskParts()[0].setStartTime(emp1.getEndTime());

		taskList.add(task1);

		Task task2 = new Task("aisle 2","cat 2", new Float(30),new Integer("0"), LocalTime.parse("05:30"), 3);
		task2.getTaskParts()[0].setPreviousTaskPartOrEmployee(task1.getTaskParts()[0]);
		// shadow anchor
		task2.getTaskParts()[0].setEmployee(emp1);
		// shadow start time
		task2.getTaskParts()[0].setStartTime(task1.getTaskParts()[0].getEndTime());
		// shadow nextTaskPart
		task1.getTaskParts()[0].setNextTaskPart(task2.getTaskParts()[0]);

		taskList.add(task2);

		solution.setTaskList(taskList);

		scoreVerifier.assertSoftWeight("Other priorities", 1, -4 * 90, solution);

	}
}
