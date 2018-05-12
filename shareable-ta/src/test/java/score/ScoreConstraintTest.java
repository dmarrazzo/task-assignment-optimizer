package score;

import java.time.Duration;
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

public class ScoreConstraintTest {
	private BendableScoreVerifier<TaskAssagnmentSolution> scoreVerifier = new BendableScoreVerifier<>(
	        SolverFactory.createFromXmlResource("solver/taskAssignmentSolverConfig.xml"));
	@Test
    public void tasks() {
		TaskAssagnmentSolution solution = new TaskAssagnmentSolution();

		List<Employee> employeeList = new ArrayList<>();
		Set<Skill> skills = new HashSet<>();
		skills.add(new Skill("reading"));
		Employee employee = new Employee("emp1", skills);
		employeeList.add(employee);
		solution.setEmployeeList(employeeList);
		
		List<Task> taskList = new ArrayList<>();
		Task task = new Task("t01", Duration.ofMinutes(90L), LocalTime.parse("10:00"), 1, 1, skills);
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
        
        //High priority task must be accomplished on time
        scoreVerifier.assertHardWeight("High priority task must be accomplished on time", 1, 0, solution);
        
        // Break: High priority task must be accomplished on time
        employee.setStartTime(LocalTime.parse("09:30"));
        task.getTaskParts()[0].setPreviousTaskPartOrEmployee(employee);
        scoreVerifier.assertHardWeight("High priority task must be accomplished on time", 1, -60, solution);
        
        
    }

}
