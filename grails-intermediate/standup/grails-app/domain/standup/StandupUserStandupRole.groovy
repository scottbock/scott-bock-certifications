package standup

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class StandupUserStandupRole implements Serializable {

	private static final long serialVersionUID = 1

	StandupUser standupUser
	StandupRole standupRole

	@Override
	boolean equals(other) {
		if (other instanceof StandupUserStandupRole) {
			other.standupUserId == standupUser?.id && other.standupRoleId == standupRole?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (standupUser) builder.append(standupUser.id)
		if (standupRole) builder.append(standupRole.id)
		builder.toHashCode()
	}

	static StandupUserStandupRole get(long standupUserId, long standupRoleId) {
		criteriaFor(standupUserId, standupRoleId).get()
	}

	static boolean exists(long standupUserId, long standupRoleId) {
		criteriaFor(standupUserId, standupRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long standupUserId, long standupRoleId) {
		StandupUserStandupRole.where {
			standupUser == StandupUser.load(standupUserId) &&
			standupRole == StandupRole.load(standupRoleId)
		}
	}

	static StandupUserStandupRole create(StandupUser standupUser, StandupRole standupRole) {
		def instance = new StandupUserStandupRole(standupUser: standupUser, standupRole: standupRole)
		instance.save()
		instance
	}

	static boolean remove(StandupUser u, StandupRole r) {
		if (u != null && r != null) {
			StandupUserStandupRole.where { standupUser == u && standupRole == r }.deleteAll()
		}
	}

	static int removeAll(StandupUser u) {
		u == null ? 0 : StandupUserStandupRole.where { standupUser == u }.deleteAll()
	}

	static int removeAll(StandupRole r) {
		r == null ? 0 : StandupUserStandupRole.where { standupRole == r }.deleteAll()
	}

	static constraints = {
		standupRole validator: { StandupRole r, StandupUserStandupRole ur ->
			if (ur.standupUser?.id) {
				StandupUserStandupRole.withNewSession {
					if (StandupUserStandupRole.exists(ur.standupUser.id, r.id)) {
						return ['userRole.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['standupUser', 'standupRole']
		version false
	}
}
