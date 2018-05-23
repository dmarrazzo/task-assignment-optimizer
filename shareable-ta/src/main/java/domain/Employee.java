package domain;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TaEmployee")
public class Employee extends TaskPartOrEmployee implements Serializable {

	private static final long serialVersionUID = -8397117902800537700L;

	private String fullName;
	/** The start time */
	private LocalTime startTime;
	
	/** The end time. */
    private LocalTime endTime;

	
	private Set<Skill> skillSet;
	
	public Employee() {
	}
	
	public Employee(final String fullName, final LocalTime startTime,final LocalTime endTime, final Set<Skill> skillSet) {
		this.fullName = fullName;
		this.setStartTime(startTime);
		this.setEndTime(endTime);
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

	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public LocalTime getEndTime() {
		return this.endTime;
	}

	@Override
	public Employee getEmployee() {
		return this;
	}

	@Override
	public String getId() {
		return getFullName();
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

}
