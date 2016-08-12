package standup

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(StatusReportRestController)
class StatusReportRestControllerSpec extends Specification {

    StatusReportService statusReportService = Mock(StatusReportService)

    def setup() {
        controller.statusReportService = statusReportService

    }

    def cleanup() {
    }

    void "Test save call service"() {
        when: "save is called"
        controller.save()

        then: "service is called"
        1 * statusReportService.saveFromMap(_)
    }

    void "Test update calls service"() {
        when: "update is called"
        controller.update()

        then: "service is called"
        1 * statusReportService.updateFromMap(_)
    }

    void "Test show calls service"() {
        when: "show is called"
        controller.show("2016-08-08", null)

        then: "service is called"
        1 * statusReportService.getStatusReportsForDate(*_)
    }
}
