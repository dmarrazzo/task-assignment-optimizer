package domain;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TaTaskAssigningSolution")
@PlanningSolution
public class TaskAssagnmentSolution implements Serializable {
	private static final long serialVersionUID = 7896839531154664433L;

	@PlanningScore(bendableHardLevelsSize = 1, bendableSoftLevelsSize = 4)
	private BendableScore score;

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "taskRange")
	private List<Task> taskList;

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "employeeRange")
	private List<Employee> employeeList;

	@PlanningEntityCollectionProperty
	@ValueRangeProvider(id = "taskPartRange")
	public List<TaskPart> getTaskPartList() {
		return getTaskList().stream()
		               .flatMap(t -> Stream.of(t.getTaskParts()))
		               .collect(Collectors.toList());
	}
	
	// ************************************************************************
	// Getters / Setters
	// ************************************************************************
	
	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

}
