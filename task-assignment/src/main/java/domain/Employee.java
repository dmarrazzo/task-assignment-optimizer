package domain;

import java.io.Serializable;
import java.util.Set;

public class Employee extends TaskOrEmployee implements Serializable {

	private static final long serialVersionUID = -8397117902800537700L;

	private String fullName;
	
	private Set<Skill> skillSet;
	
	public Employee(String fullName, Set<Skill> skillSet) {
		this.fullName = fullName;
		this.skillSet = skillSet;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Set<Skill> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Set<Skill> skillSet) {
		this.skillSet = skillSet;
	}

	@Override
	public Integer getEndTime() {
		return 0;
	}

	@Override
	public Employee getEmployee() {
		return this;
	}
}
