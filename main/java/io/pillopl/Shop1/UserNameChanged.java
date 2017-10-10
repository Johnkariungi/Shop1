package io.pillopl.Shop1;

import java.time.Instant;

public class UserNameChanged implements DomainEvent{
    public String getNewNickName() { /*auto generated getter*/
        return newNickName;
    }

    private final String newNickName; /*create a getter*/
    private final Instant when;

    public UserNameChanged(String newNickName, Instant when) {/*bind parameters to fields*/
        this.newNickName = newNickName;
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
