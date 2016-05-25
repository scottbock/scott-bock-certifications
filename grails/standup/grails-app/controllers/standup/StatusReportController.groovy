package standup

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StatusReportController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond StatusReport.list(params), model:[statusReportCount: StatusReport.count()]
    }

    def show(StatusReport statusReport) {
        respond statusReport
    }

    def create() {
        respond new StatusReport(params)
    }

    @Transactional
    def save(StatusReport statusReport) {
        if (statusReport == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (statusReport.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond statusReport.errors, view:'create'
            return
        }

        statusReport.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'statusReport.label', default: 'StatusReport'), statusReport.id])
                redirect statusReport
            }
            '*' { respond statusReport, [status: CREATED] }
        }
    }

    def edit(StatusReport statusReport) {
        respond statusReport
    }

    @Transactional
    def update(StatusReport statusReport) {
        if (statusReport == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (statusReport.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond statusReport.errors, view:'edit'
            return
        }

        statusReport.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'statusReport.label', default: 'StatusReport'), statusReport.id])
                redirect statusReport
            }
            '*'{ respond statusReport, [status: OK] }
        }
    }

    @Transactional
    def delete(StatusReport statusReport) {

        if (statusReport == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        statusReport.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'statusReport.label', default: 'StatusReport'), statusReport.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'statusReport.label', default: 'StatusReport'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    def date(Integer offset) {
        offset = offset ?: 0
        Date start = new Date() - offset
        start.set(hourOfDay: 0, minute: 0, second: 0)
        Date end = new Date() - offset
        end.set(hourOfDay: 23, minute: 59, second: 59)
        respond StatusReport.findAllByDateLessThanAndDateGreaterThan(end, start), model:[date: start, offset: offset]
    }
}
