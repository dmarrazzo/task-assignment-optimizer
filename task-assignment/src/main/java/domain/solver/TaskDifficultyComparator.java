package domain.solver;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import domain.Task;

public class TaskDifficultyComparator implements Comparator<Task>, Serializable {

	private static final long serialVersionUID = 115046742424014742L;

	@Override
	public int compare(Task a, Task b) {
        return new CompareToBuilder()
                .append(a.getPriority(), b.getPriority())
                .append(a.getTaskType().getRequiredSkillList().size(), b.getTaskType().getRequiredSkillList().size())
                .append(a.getTaskType().getDuration(), b.getTaskType().getDuration())
                .toComparison();
	}

}
