import standup.StatusReport
import standup.User

class BootStrap {

    def init = { servletContext ->
        def admin = new User(username: 'admin', password: '1234')
        admin.save(flush: true)

        def joe = new User(username: 'joe', password: 'joe')
        joe.save(flush: true)
        def bill = new User(username: 'bill', password: 'bill')
        bill.save(flush: true)

    	new StatusReport(user: joe, yesterdayAccomplished:'Finished Story', todayPlan:'Start new story', impediments:'Nothing', date: new Date()).save(failOnError: true)
		new StatusReport(user: bill, yesterdayAccomplished:'Started Story', todayPlan:'Finish story', date: new Date()).save(failOnError: true)
    	new StatusReport(user: bill, yesterdayAccomplished:'Finished Story', todayPlan:'Start new story', impediments:'Nothing', date: new Date() - 1).save(failOnError: true)
		new StatusReport(user: joe, yesterdayAccomplished:'Started Story', todayPlan:'Finish story', date: new Date() - 1).save(failOnError: true)
    }
    def destroy = {
    }
}
