package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
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

	@PlanningScore(bendableHardLevelsSize = 2, bendableSoftLevelsSize = 3)
	private BendableScore score;

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "taskRange")
	private List<Task> taskList;

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "employeeRange")
	private List<Employee> employeeList;

	@PlanningEntityCollectionProperty
	@ValueRangeProvider(id = "taskPartRange")
	private List<TaskPart> taskPartList;
	
	private Set<Interval> gaps;

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
		this.setTaskPartList(taskList.stream()
	               .flatMap(t -> Stream.of(t.getTaskParts()))
	               .collect(Collectors.toList()));
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

	public List<TaskPart> getTaskPartList() {
		return taskPartList;
	}

	public void setTaskPartList(List<TaskPart> taskPartList) {
		this.taskPartList = taskPartList;
	}

	public Set<Interval> getGaps() {
		return gaps;
	}

	public void setGaps(Set<Interval> gaps) {
		this.gaps = gaps;
	}

}
