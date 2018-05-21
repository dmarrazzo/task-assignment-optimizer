package domain.solver;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import domain.TaskPart;

public class TaskPartDifficultyComparator implements Comparator<TaskPart>, Serializable {

	private static final long serialVersionUID = 115046742424014742L;

	@Override
	public int compare(TaskPart a, TaskPart b) {

		return new CompareToBuilder().append(b.getTask()
		                                      .getPriority(), a.getTask()
		                                                       .getPriority())
		                             .append(b.getTask()
		                                      .getCompletionTime(), a.getTask()
		                                                             .getCompletionTime())
		                             .append(a.getDuration(), b.getDuration())
		                             .append(a.getTask().getMaxParts(), b.getTask().getMaxParts())
		                             .append(a.getId(), b.getId())
		                             .toComparison();
	}

}
