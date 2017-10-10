package io.pillopl.Shop1;

import java.util.UUID;

public interface UserRepository {

    void save(User user); /*we can save user*/

    User find(UUID uuid); /*we can find user*/
}
