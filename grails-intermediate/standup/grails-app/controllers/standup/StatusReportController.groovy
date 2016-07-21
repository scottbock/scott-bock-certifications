package standup

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StatusReportController {
    def statusReportService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def date(Integer offset) {
        offset = offset ?: 0
        Date date = new Date() - offset

        respond statusReportService.getStatusReportsForDate(date), model:[date: date, offset: offset]
    }

    def index(Integer max) {
        forward action: "date"
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


}
