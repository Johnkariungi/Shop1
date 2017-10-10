package io.pillopl.Shop1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*implement interface*/
public class EventSourcedUserRepository implements UserRepository {

    /*don't store users in state but only domain events when it's created and mutating state with new changes
    * Example of Functional programming and producing the results - production log like git */
    private final Map<UUID, List<DomainEvent>> users = new ConcurrentHashMap<>();


    @Override
    public void save(User user) {
        /*append new event from our user if it's empty it will not have anything else it will have new changes*/
        List<DomainEvent> newChanges = user.getChanges();
        /*because we have them in our map coz it's empty wan the list of the current changes get the user or empty arrayList*/
        List<DomainEvent> currentChanges  = users.getOrDefault(user.getUuid(), new ArrayList<>());
        currentChanges.addAll(newChanges); /*append new changes to current changes*/
        users.put(user.getUuid(), currentChanges); /*put the current changes in out map*/
        user.flushChanges(); /*update the object, the state will not be dirty any more only save internal collection of changes*/
    }

    @Override
    public User find(UUID uuid) {
        if (!users.containsKey(uuid)) { /*if our map doesn't contain uuid, don't know the object*/
            return null;
        }
        /*user that is been recreated for all the domain events from the begining pass and initialize user, and list of events*/
        return User.recreateFrom(uuid, users.get(uuid));
    }
}
