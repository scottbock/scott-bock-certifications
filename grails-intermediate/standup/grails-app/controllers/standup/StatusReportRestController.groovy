package standup

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class StatusReportRestController {

    def statusReportService

    def show(String date, Long id){
        if(id)
            render text: StatusReport.get(id) as JSON
        else
            render text: statusReportService.getStatusReportsForDate(Date.parse("yyyy-MM-dd", date)) as JSON
    }

    def update(){
        statusReportService.updateFromMap request.JSON

        render status: 200
    }

    def save(){
        statusReportService.saveFromMap request.JSON

        render status: 200
    }

}
