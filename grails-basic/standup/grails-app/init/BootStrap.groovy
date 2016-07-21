import standup.StatusReport

class BootStrap {

    def init = { servletContext ->
    	new StatusReport(name:'Joe', yesterdayAccomplished:'Finished Story', todayPlan:'Start new story', impediments:'Nothing', date: new Date()).save(failOnError: true)
		new StatusReport(name:'Bill', yesterdayAccomplished:'Started Story', todayPlan:'Finish story', date: new Date()).save(failOnError: true)
    	new StatusReport(name:'Bill', yesterdayAccomplished:'Finished Story', todayPlan:'Start new story', impediments:'Nothing', date: new Date() - 1).save(failOnError: true)
		new StatusReport(name:'Joe', yesterdayAccomplished:'Started Story', todayPlan:'Finish story', date: new Date() - 1).save(failOnError: true)
    }
    def destroy = {
    }
}
