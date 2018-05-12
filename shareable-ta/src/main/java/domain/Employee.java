package domain;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TaEmployee")
public class Employee extends TaskPartOrEmployee implements Serializable {

	private static final long serialVersionUID = -8397117902800537700L;

	private String fullName;
	private LocalTime startTime;
	
	private Set<Skill> skillSet;
	
	public Employee() {
	}
	
	public Employee(String fullName, Set<Skill> skillSet) {
		this.fullName = fullName;
		setStartTime(LocalTime.parse("05:00"));
		this.skillSet = skillSet;
	}

	public Employee(String fullName, LocalTime startTime, Set<Skill> skillSet) {
		this.fullName = fullName;
		this.setStartTime(startTime);
		this.skillSet = skillSet;
	}

	@Override
	public String toString() {
		return "Employee [fullName=" + fullName + ", startTime=" + getStartTime() + ", skillSet=" + skillSet + "]";
	}
	
	// ************************************************************************
	// Getters / Setters
	// ************************************************************************

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
	public LocalTime getEndTime() {
		return getStartTime();
	}

	@Override
	public Employee getEmployee() {
		return this;
	}

	@Override
	public String getId() {
		return "emp "+getFullName();
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

}
