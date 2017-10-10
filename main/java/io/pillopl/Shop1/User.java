package io.pillopl.Shop1;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
/*Domain model*/
public class User {
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

    private void userDeactivated(UserDeactivated userDeactivated) {
        userActivated(new UserActivated(Instant.now()));
    }

    private void userActivated(UserActivated userActivated) {
        state = UserState.DEACTIVATED;
    }

    void changeNickNameTo(String newNickName) throws IllegalAccessException {
        if(isDeactivated()) {
            throw new IllegalAccessException();
        }
        userNameChanged(new UserNameChanged(newNickName, Instant.now())); /*plus timestamp*/
        /* refactor -> extract -> make extract method name of the event captured in the code*/
    }

    private void userNameChanged(UserNameChanged event) { /*content of our event*/
        nickname = event.getNewNickName(); /*extract the method from the variant*/
    }

    boolean isActivated() { return state.equals(UserState.ACTIVATED); }

    boolean isDeactivated() {
        return state.equals(UserState.DEACTIVATED);
    }

    String getNickName() {
        return nickname;
    }
}
