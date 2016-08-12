package standup

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock(User)
class UserServiceSpec extends Specification {

    void "test save"() {
        when: "save a user"
        service.save(new User(username: 'user', password: 'password'))

        then:
        User.count == 1
    }
}
