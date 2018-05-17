package domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

public class Interval implements Serializable{

	private static final long serialVersionUID = 3899896186288810099L;
	
	private LocalTime start;
	private LocalTime end;

	public Interval(LocalTime start, LocalTime end) {
		this.start = start;
		this.end = end;
	}
		
	public boolean overlap(LocalTime a, Duration duration) {
		LocalTime b = a.plus(duration);
		
		// start --- a ------- end --- b
		// start --- a ------- b ----- end
		if (start.isBefore(a) && end.isAfter(a)) return true;

		// start = a ------- end --- b
		// start = a ------- end = b
		// a---start ------- end = b
		if (start.equals(a) || end.equals(b)) return true;
		
		// a ------- start --- b ----- end
		// a ------- start --- end --- b
		if (a.isBefore(start) && start.isBefore(b)) return true;
		
		return false;
	}

	@Override
	public String toString() {
		return String.format("Interval [%s, %s]", start, end);
	}	

	// ************************************************************************
	// Getters / Setters
	// ************************************************************************
	
	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}

}
