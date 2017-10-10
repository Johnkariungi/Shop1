package io.pillopl.Shop1;

import com.google.common.collect.ImmutableList;
import io.vavr.API;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.Iterator.ofAll;

/*import static javaslang.collection.List.ofAll;*/

/*Domain model*/
public class User {
    public UUID getUuid() {
        return uuid;
    }

    public List<DomainEvent> getChanges() {
        return ImmutableList.copyOf(changes); /*no one can change it*/
    }

    public void flushChanges() {
        changes.clear();
    }

    public static User recreateFrom(UUID uuid, List<DomainEvent> domainEvents) {
        /*handleEvent method handles the mutative state of nickName*/
       return ofAll(domainEvents).foldLeft(new User(uuid), User::handleEvent); /*tricky for java 8 but using one liner form java client - initialState new user from UUID*/
    }

    User handleEvent(DomainEvent event) { /*patten match our event*/
        return API.Match(event).of(
                Case(instanceOf(UserActivated.class), this::userActivated),
                Case(instanceOf(UserDeactivated.class), this::userDeactivated),
                Case(instanceOf(UserNameChanged.class), this::userNameChanged)
        );
    }

    /* Behaviour - Events Storming or Domain constructs and methods comes before data structures or properties
    Think about invariants - state transitions that our objects allow us to do - if statements in our model */

    enum UserState {
        INITIALIZED, ACTIVATED, DEACTIVATED
    }
    private String nickname = "";

    private final UUID uuid;

    private UserState state = UserState.INITIALIZED;

    /*save the changes by ourselves by storing them in an internal collection creating a mapping interface DomainEvent*/
    private List<DomainEvent> changes = new ArrayList<>();

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    void activate() throws IllegalAccessException { /*activate our user.*/
        if(isActivated()) {
            throw new IllegalAccessException();
        }
        userActivated(new UserActivated(Instant.now())); /*hibernate change to our database with a event state transition*/
    }

    void deactivate() throws IllegalAccessException {
        if(isDeactivated()) {
            throw new IllegalAccessException();
        }
        userDeactivated(new UserDeactivated(Instant.now()));
    }

    private User userDeactivated(UserDeactivated userDeactivated) {
        userActivated(new UserActivated(Instant.now()));
        changes.add(userDeactivated); /*add changes to internal collection of changes when a change is made to the state*/
        return this;
    }

    private User userActivated(UserActivated userActivated) {
        state = UserState.DEACTIVATED;
        changes.add(userActivated); /*when the state is been changed*/
        return this; /*return mutated state*/
    }

    void changeNickNameTo(String newNickName) throws IllegalAccessException {
        if(isDeactivated()) {
            throw new IllegalAccessException();
        }
        userNameChanged(new UserNameChanged(newNickName, Instant.now())); /*plus timestamp*/
        /* refactor -> extract -> make extract method name of the event captured in the code*/
    }

    private User userNameChanged(UserNameChanged userNameChanged) { /*content of our event*/
        nickname = userNameChanged.getNewNickName(); /*extract the method from the variant*/
        changes.add(userNameChanged);
        return this;
    }

    boolean isActivated() { return state.equals(UserState.ACTIVATED); }

    boolean isDeactivated() {
        return state.equals(UserState.DEACTIVATED);
    }

    String getNickName() {
        return nickname;
    }
}
