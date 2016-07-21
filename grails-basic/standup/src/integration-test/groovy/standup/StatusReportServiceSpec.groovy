package standup

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration(applicationClass = Application.class)
@Rollback
class StatusReportServiceIntegrationSpec extends Specification {

    @Autowired
    def StatusReportService service;

    def setup() {
    }

    def cleanup() {
    }

    def setupData(){
    }

    void "test get reports for today"() {
        given:
        setupData()

        when:
        def reports = service.getStatusReportsForDate(new Date())

        then:
        reports.size() == 2
    }

    void "test get reports for yesterday"(){
        given:
        setupData()

        when:
        def reports = service.getStatusReportsForDate(new Date() - 1)

        then:
        reports.size() == 2
    }

    void "test get reports for 2 days ago"(){
        given:
        setupData()

        when:
        def reports = service.getStatusReportsForDate(new Date() - 2)

        then:
        reports.size() == 0
    }
}
