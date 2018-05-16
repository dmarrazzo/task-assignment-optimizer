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
		Set<Skill> reqS = new HashSet<>(Arrays.asList(new Skill("skill")));
		Task task1, task2;
		TaskPart part1, part2;
		int compare;

		// priority first
		task1 = new Task("1", Duration.ofMinutes(30), LocalTime.parse("09:30"), 1, 2, reqS);
		task2 = new Task("2", Duration.ofMinutes(90), LocalTime.parse("08:30"), 3, 2, reqS);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);

		// the first argument is greater than the second
		assertTrue(compare > 0);

		// earlier completion time
		task1 = new Task("1", Duration.ofMinutes(20), LocalTime.parse("08:30"), 3, 2, reqS);
		task2 = new Task("2", Duration.ofMinutes(30), LocalTime.parse("09:30"), 3, 2, reqS);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		// the first argument is greater than the second
		assertTrue(compare > 0);

		// longer duration
		task1 = new Task("1", Duration.ofMinutes(30), LocalTime.parse("08:30"), 3, 2, reqS);
		task2 = new Task("2", Duration.ofMinutes(20), LocalTime.parse("08:30"), 3, 2, reqS);

		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		// the first argument is greater than the second
		assertTrue(compare > 0);

	}

	@Test
	public void strenghtComparator() {
		PreviousTaskPartOrEmployeeStrengthComparator comparator = new PreviousTaskPartOrEmployeeStrengthComparator();

		Set<Skill> skills = new HashSet<>(Arrays.asList(new Skill("a_skill")));

		TaskPartOrEmployee a = new Employee("1", LocalTime.parse("06:00"), skills);
		TaskPartOrEmployee b = new Employee("2", LocalTime.parse("08:00"), skills);
		;
		int compare = comparator.compare(a, b);

		assertTrue(compare > 0);
	}

	@Test
	public void parts() {
		// check the partList creation
		Set<Skill> reqS = new HashSet<>(Arrays.asList(new Skill("a_skill")));
		Task task1 = new Task("1", Duration.ofMinutes(20), LocalTime.parse("08:30"), 3, 1, reqS);
		Task task2 = new Task("2", Duration.ofMinutes(20), LocalTime.parse("09:30"), 3, 1, reqS);

		TaskAssagnmentSolution solution = new TaskAssagnmentSolution();
		solution.setTaskList(Arrays.asList(task1, task2));

		List<TaskPart> partList = solution.getTaskPartList();
		assertTrue(partList.size() == 2);
	}

	@Test
	public void outOfTime() {
		// check out of time implementation
		Set<Skill> reqS = new HashSet<>(Arrays.asList(new Skill("a_skill")));
		Task task1 = new Task("1", Duration.ofMinutes(90), LocalTime.parse("08:30"), 3, 1, reqS);
		task1.getTaskParts()[0].setStartTime(LocalTime.parse("08:30"));
		
		assertEquals( -90, task1.getTaskParts()[0].getOutOfTime());
	}

	@Test
	public void elapsed() {
		// check out of time implementation
		Set<Skill> skills = new HashSet<>(Arrays.asList(new Skill("a_skill")));
		Task task1 = new Task("1", Duration.ofMinutes(90), LocalTime.parse("08:30"), 3, 1, skills);
		TaskPart taskPart10 = task1.getTaskParts()[0];
		
		Employee employee = new Employee("emp1",LocalTime.parse("05:00"), skills);
		taskPart10.setEmployee(employee);
		task1.getTaskParts()[0].setStartTime(employee.getEndTime());
		
		assertEquals( 90, task1.getTaskParts()[0].getElapsed());
	}

}
