package domain.solver;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import domain.TaskPartOrEmployee;

/**
 * Some optimization algorithms work a bit more efficiently if they have an
 * estimation of which planning values are stronger, which means they are more
 * likely to satisfy a planning entity. For example: in bin packing bigger
 * containers are more likely to fit an item and in course scheduling bigger
 * rooms are less likely to break the student capacity constraint.
 * 
 * @author Donato Marrazzo
 *
 */
public class PreviousTaskPartOrEmployeeStrengthComparator implements Comparator<TaskPartOrEmployee> {

	@Override
	public int compare(TaskPartOrEmployee a, TaskPartOrEmployee b) {
		// a task is stroger if has a lower end time

		return new CompareToBuilder().append(b.getEndTime(), a.getEndTime())
		                             .append(a.getId(), b.getId())
		                             .toComparison();
	}

}
