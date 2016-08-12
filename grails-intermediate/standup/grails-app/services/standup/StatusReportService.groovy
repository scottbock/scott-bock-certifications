package standup

import grails.transaction.Transactional

@Transactional
class StatusReportService {

    def getStatusReportsForDate(Date date) {
        Date start = date.clone() - 1
        start.set(hourOfDay: 23, minute: 59, second: 59)
        Date end = date.clone()
        end.set(hourOfDay: 23, minute: 59, second: 59)

        StatusReport.findAllByDateBetween(start, end)
    }

    @Transactional
    def updateFromMap (Map jsonParams) {
        StatusReport statusReport = StatusReport.get jsonParams.id
        statusReport.setProperties jsonParams
        statusReport.save()
    }

    @Transactional
    def saveFromMap (Map jsonParams) {
        new StatusReport(jsonParams).save()
    }

    @Transactional
    def save (StatusReport statusReport) {
        statusReport.save flush: true
    }

    @Transactional
    def delete(StatusReport statusReport) {
        statusReport.delete flush: true
    }

}
