package standup

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Transactional(readOnly = true)
class StandupUserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond StandupUser.list(params), model:[standupUserCount: StandupUser.count()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(StandupUser standupUser) {
        respond standupUser
    }

    
    def create() {
        respond new StandupUser(params)
    }

    @Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
    def register() {
        respond new StandupUser(params)
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
    def save(StandupUser standupUser) {
        if (standupUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (standupUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond standupUser.errors, view:'create'
            return
        }

        standupUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'standupUser.label', default: 'StandupUser'), standupUser.id])
                redirect standupUser
            }
            '*' { respond standupUser, [status: CREATED] }
        }
    }

    def edit(StandupUser standupUser) {
        respond standupUser
    }

    @Transactional
    def update(StandupUser standupUser) {
        if (standupUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (standupUser.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond standupUser.errors, view:'edit'
            return
        }

        standupUser.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'standupUser.label', default: 'StandupUser'), standupUser.id])
                redirect standupUser
            }
            '*'{ respond standupUser, [status: OK] }
        }
    }

    @Transactional
    def delete(StandupUser standupUser) {

        if (standupUser == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        standupUser.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'standupUser.label', default: 'StandupUser'), standupUser.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'standupUser.label', default: 'StandupUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
