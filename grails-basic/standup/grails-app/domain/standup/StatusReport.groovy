package standup

class StatusReport {

	String name
	String yesterdayAccomplished
	String todayPlan
	String impediments
	Date date

	static constraints = {
	  date()
	  name()
	  yesterdayAccomplished()
	  todayPlan()
	  impediments(nullable: true)
	}

}
