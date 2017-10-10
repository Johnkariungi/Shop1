package io.pillopl.Shop1

import spock.lang.Specification

class UserRepositoryTest extends Specification {

    UserRepository userRepository = new EventSourcedUserRepository();

    def 'should be able to save and load user' () {
        given:
            UUID uuid = UUID.randomUUID();
        and:
            User someUser = new User(uuid);
        and
            someUser.activate()
        and:
            someUser.changeNickNameTo(newNickName: "Barry")
        when:
            userRepository.save(someUser)
        and:
            User loaded = userRepository.find(uuid);
        then:
            loaded.isActivated()
            loaded.getNickName() == "Barry"
    }
}
