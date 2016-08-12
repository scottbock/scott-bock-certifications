package standup

import grails.transaction.Transactional

@Transactional
class UserService {

    @Transactional
    def save (User user) {
        user.save flush: true
    }

    @Transactional
    def delete(User user) {
        user.delete flush: true
    }
}
