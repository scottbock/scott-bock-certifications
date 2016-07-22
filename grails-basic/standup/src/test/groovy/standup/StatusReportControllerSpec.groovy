package standup

import grails.test.mixin.*
import spock.lang.*

@TestFor(StatusReportController)
@Mock(StatusReport)
class StatusReportControllerSpec extends Specification {

   StatusReportService statusReportServiceMock = Mock(StatusReportService)

    def setup() {
        controller.statusReportService = statusReportServiceMock
        statusReportServiceMock.save(_ as StatusReport) >> {args -> args[0].setId(1)}
    }

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        params["name"] = 'Tom'
        params["yesterdayAccomplished"] = 'Not Much'
        params["todayPlan"] = 'A little more'
        params["impediments"] = 'Email Server down'
        params["date"] = new Date() 
    }

    void "Test the date action returns the correct model"() {

        when:"The index action is executed"
            controller.date()

        then:"The model is correct"
            !model.statusReportList
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.statusReport!= null
    }

    void "Test the save action correctly persists an instance"() {
        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def statusReport = new StatusReport()
            statusReport.validate()
            controller.save(statusReport)

        then:"The create view is rendered again with the correct model"
            model.statusReport!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            statusReport = new StatusReport(params)

            controller.save(statusReport)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/statusReport/show/1'
            controller.flash.message != null
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def statusReport = new StatusReport(params)
            controller.show(statusReport)

        then:"A model is populated containing the domain instance"
            model.statusReport == statusReport
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def statusReport = new StatusReport(params)
            controller.edit(statusReport)

        then:"A model is populated containing the domain instance"
            model.statusReport == statusReport
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/statusReport/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def statusReport = new StatusReport()
            statusReport.validate()
            controller.update(statusReport)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.statusReport == statusReport

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            statusReport = new StatusReport(params).save(flush: true)
            controller.update(statusReport)

        then:"A redirect is issued to the show action"
            statusReport != null
            response.redirectedUrl == "/statusReport/show/$statusReport.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/statusReport/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def statusReport = new StatusReport(params).save(flush: true)

        then:"It exists"
            StatusReport.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(statusReport)

        then:"The instance is deleted"
            1 * statusReportServiceMock.delete(_)
            response.redirectedUrl == '/statusReport/index'
            flash.message != null
    }

    private getForwardedParams() {
        parseResponseForwardedUrl()[1]
    }

    private String getForwardedUrl() {
        parseResponseForwardedUrl()[0]
    }

    private parseResponseForwardedUrl() {
        // Pattern for forwardedUrl stored in response.
        def forwardedUrlPattern = ~/\/grails\/(.*)?\.dispatch\?*(.*)/

        // Match forwardedUrl in response with pattern.
        def matcher = response.forwardedUrl =~ forwardedUrlPattern

        def forwardUrl = null
        def forwardParameters = [:]

        if (matcher) {
            // Url is first group in pattern. We add '/' so it has the same format as redirectedUrl from response.
            forwardUrl = "/${matcher[0][1]}"

            // Parse parameters that are available in the forwardedUrl of the response.
            forwardParameters = parseResponseForwardedParams(matcher[0][2])
        }

        [forwardUrl, forwardParameters]
    }

    private parseResponseForwardedParams(final String queryString) {
        // Query string has format paramName=paramValue¶m2Name=param2Value. & is optional.
        def parameters = queryString.split('&')

        // Turn the paramName=paramValue parts into a Map.
        def forwardParameters = parameters.inject([:]) { result, parameter ->
            def (parameterName, parameterValue) = parameter.split('=')
            result[parameterName] = parameterValue
            result
        }
        forwardParameters
    }
}
