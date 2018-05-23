package domain.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert.*;
import org.junit.Test;

import domain.Employee;
import domain.Skill;
import domain.Task;
import domain.TaskAssagnmentSolution;
import domain.TaskPart;
import domain.TaskPartOrEmployee;

@SuppressWarnings("unused")
public class DomainTest {

	@Test
	public void difficultyComparator() {
		TaskPartDifficultyComparator comparator = new TaskPartDifficultyComparator();
		Set<Skill> reqS = new HashSet<>(Arrays.asList(new Skill("skill",0)));
		Task task1, task2;
		TaskPart part1, part2;
		int compare;

		// priority first
		task1 = new Task("aisle 1","cat 1", new Float(30),new Integer("0"), LocalTime.parse("09:30"), 3);
		task2 = new Task("aisle 2","cat 2", new Float(90),new Integer("0"), LocalTime.parse("08:30"), 3);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);

		// the first argument is greater than the second
		assertTrue(compare > 0);

		// earlier completion time
		task1 = new Task("aisle 1","cat 1", new Float(20),new Integer("0"), LocalTime.parse("08:30"), 3);
		task2 = new Task("aisle 2","cat 2", new Float(30),new Integer("0"), LocalTime.parse("09:30"), 3);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		// the first argument is greater than the second
		assertTrue(compare > 0);

		// longer duration
		task1 = new Task("aisle 1","cat 1", new Float(30),new Integer("0"), LocalTime.parse("08:30"), 3);
		task2 = new Task("aisle 2","cat 2", new Float(20),new Integer("0"), LocalTime.parse("08:30"), 3);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		// the first argument is greater than the second
		assertTrue(compare > 0);

		// more parts
		task1 = new Task("aisle 1","cat 1", new Float(90),new Integer("0"), LocalTime.parse("08:30"), 3);
		task2 = new Task("aisle 2","cat 2", new Float(60),new Integer("0"), LocalTime.parse("08:30"), 3);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		// the first argument is greater than the second
		assertTrue(compare > 0);
		
	}

	@Test
	public void strenghtComparator() {
		PreviousTaskPartOrEmployeeStrengthComparator comparator = new PreviousTaskPartOrEmployeeStrengthComparator();

		Set<Skill> skills = new HashSet<>(Arrays.asList(new Skill("a_skill",0)));

		TaskPartOrEmployee a = new Employee("1", LocalTime.parse("06:00"),LocalTime.parse("20:00"), skills);
		TaskPartOrEmployee b = new Employee("2", LocalTime.parse("08:00"),LocalTime.parse("20:00"), skills);
		;
		int compare = comparator.compare(a, b);

		assertTrue(compare > 0);
	}

	@Test
	public void parts() {
		// check the partList creation
		Task task1 = new Task("aisle 1","cat 1", new Float(20),new Integer("0"), LocalTime.parse("08:30"), 3);
		Task task2 = new Task("aisle 2","cat 2", new Float(20),new Integer("0"), LocalTime.parse("09:30"), 3);

		TaskAssagnmentSolution solution = new TaskAssagnmentSolution();
		solution.setTaskList(Arrays.asList(task1, task2));

		List<TaskPart> partList = solution.getTaskPartList();
		assertTrue(partList.size() == 2);
	}

	@Test
	public void outOfTime() {
		// check out of time implementation
		Set<Skill> reqS = new HashSet<>(Arrays.asList(new Skill("a_skill",0)));
		Task task1 = new Task("aisle 1","cat 1", new Float(90),new Integer("0"), LocalTime.parse("08:30"), 3);
		task1.getTaskParts()[0].setStartTime(LocalTime.parse("08:30"));
		
		assertEquals( -90, task1.getTaskParts()[0].getOutOfTime());
	}

	@Test
	public void elapsed() {
		// check out of time implementation
		Set<Skill> skills = new HashSet<>(Arrays.asList(new Skill("a_skill",0)));
		Task task1 = new Task("aisle 1","cat 1", new Float(90),new Integer("0"), LocalTime.parse("08:30"), 3);
		
		TaskPart taskPart10 = task1.getTaskParts()[0];
		
		Employee employee = new Employee("emp1",LocalTime.parse("05:00"),LocalTime.parse("20:00"), skills);
		taskPart10.setEmployee(employee);
		task1.getTaskParts()[0].setStartTime(employee.getEndTime());
		
		assertEquals( 90, task1.getTaskParts()[0].getElapsed());
	}

}
