package io.pillopl.Shop1;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*implement interface*/
public class InMemoryUserRepo implements UserRepository {

    /*store users in a hashmap UUID as the key and user as the value*/
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    @Override
    public User find(UUID uuid) {
        return users.get(uuid);
    }
}
