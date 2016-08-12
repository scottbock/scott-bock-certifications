package standup

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class StatusReportController {
    def statusReportService
    def springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def date(Integer offset) {
        offset = offset ?: 0
        Date date = new Date() - offset

        respond statusReportService.getStatusReportsForDate(date), model:[date: date, offset: offset]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max) {
        forward action: "date"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(StatusReport statusReport) {
        respond statusReport
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        def statusReport = new StatusReport(params)
        def user = getLoggedInUser()
        statusReport.setUser(user)
        respond statusReport
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(StatusReport statusReport) {
        if (statusReport == null) {
            notFound()
            return
        }

        if (statusReport.hasErrors()) {
            respond statusReport.errors, view:'create'
            return
        }

        statusReportService.save statusReport

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'statusReport.label', default: 'StatusReport'), statusReport.id])
                redirect statusReport
            }
            '*' { respond statusReport, [status: CREATED] }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(StatusReport statusReport) {
        respond statusReport
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(StatusReport statusReport) {
        if (statusReport == null) {
            notFound()
            return
        }

        if (statusReport.hasErrors()) {
            respond statusReport.errors, view:'edit'
            return
        }

        statusReportService.save statusReport

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'statusReport.label', default: 'StatusReport'), statusReport.id])
                redirect statusReport
            }
            '*'{ respond statusReport, [status: OK] }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def delete(StatusReport statusReport) {

        if (statusReport == null) {
            notFound()
            return
        }

        statusReportService.delete statusReport

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

    protected User getLoggedInUser(){
        User.get(springSecurityService.principal.id)
    }


}
