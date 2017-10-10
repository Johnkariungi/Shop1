package io.pillopl.Shop1;

import java.time.Instant;

public class UserActivated implements DomainEvent {
    private Instant when;

    public UserActivated(Instant when) {
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
