package io.pillopl.Shop1

import spock.lang.Specification

class UserTest extends Specification {
    /*initialize new user*/
    User user = new User(UUID.randomUUID())

    /*invariant case transitions that our user can do*/
    def 'deactivated user cannot change nickname' () {
        given:
            user.deactivate()
        when:
            user.changeNickNameTo(newNickName: "Barry")
                .changeNickNameTo(newNickName: "Barry")
        then:
            thro
            thrown(IllegalStateException)
    }

    def 'activated user can change nickname' () {
        given:
            user.activate()
        when:
            user.changeNickNameTo(newNickName: "Barry")
        then:
        user.getNickName() == "Barry"
    }

    def 'new user can be activated' () {
        when:
            user.activate()
        then:
            user.isActivated()
    }

    def 'activated  can be deactivated' () {
        given:
            user.activate()
        when:
            user.activate()
        then:
            user.isDeactivated()
    }

    def 'activated user cannot be activated' () {
        given:
            user.activate()
        when:
            user.activate()
        then:
            thrown(IllegalStateException)
    }

    def 'deactivated user cannot be deactivated' () {
        given:
            user.deactivate()
        when:
            user.deactivate()
        then:
            thrown(IllegalStateException)
    }
}
