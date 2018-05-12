package domain.solver;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert.*;

import domain.Employee;
import domain.Skill;
import domain.Task;
import domain.TaskPart;
import domain.TaskPartOrEmployee;

@SuppressWarnings("unused")
public class Test {

	@org.junit.Test
	public void difficultyComparator() {
		TaskPartDifficultyComparator comparator = new TaskPartDifficultyComparator();

		Set<Skill> reqS = new HashSet<>(Arrays.asList(new Skill("aaa")));
		Task task1 = new Task("1", Duration.ofMinutes(20), LocalTime.parse("08:30"), 3, 2, reqS );
		Task task2 = new Task("2", Duration.ofMinutes(20), LocalTime.parse("09:30"), 3, 2, reqS );
		
		
		TaskPart part1 = task1.getTaskParts()[0];
		TaskPart part2 = task2.getTaskParts()[0];
		int compare = comparator.compare(part1, part2);
		assertTrue(compare>0);

		task1 = new Task("1", Duration.ofMinutes(30), LocalTime.parse("08:30"), 3, 2, reqS );
		task2 = new Task("2", Duration.ofMinutes(20), LocalTime.parse("08:30"), 3, 2, reqS );		
		
		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		assertTrue(compare>0);

		task1 = new Task("1", Duration.ofMinutes(20), LocalTime.parse("08:30"), 2, 2, reqS );
		task2 = new Task("2", Duration.ofMinutes(20), LocalTime.parse("08:30"), 3, 2, reqS );		
		
		part1 = task1.getTaskParts()[0];
		part2 = task2.getTaskParts()[0];
		compare = comparator.compare(part1, part2);
		assertTrue(compare>0);

	}
	
	@org.junit.Test
	public void strenghtComparator() {
		PreviousTaskPartOrEmployeeStrengthComparator comparator = new PreviousTaskPartOrEmployeeStrengthComparator();

		Set<Skill> skills = new HashSet<>(Arrays.asList(new Skill("aaa")));

		TaskPartOrEmployee a = new Employee("1", LocalTime.parse("06:00"), skills);
		TaskPartOrEmployee b = new Employee("2", LocalTime.parse("08:00"), skills);;
		int compare = comparator.compare(a, b);
		
		assertTrue(compare>0);
	}
}
