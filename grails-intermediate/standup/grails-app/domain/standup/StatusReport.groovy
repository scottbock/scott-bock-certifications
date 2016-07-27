package standup

class StatusReport {

	User user
	String yesterdayAccomplished
	String todayPlan
	String impediments
	Date date

	static constraints = {
	  date()
	  user()
	  yesterdayAccomplished()
	  todayPlan()
	  impediments(nullable: true)
	}

}
