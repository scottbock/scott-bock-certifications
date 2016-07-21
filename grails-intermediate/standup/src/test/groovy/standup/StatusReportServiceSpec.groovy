package standup

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(StatusReportService)
@Mock(StatusReport)
class StatusReportServiceSpec extends Specification {

    void "test get reports for date"() {
        given:
        def date = new Date()

        when:
        def reports = service.getStatusReportsForDate(date)

        then:
        reports != null;
    }
}
