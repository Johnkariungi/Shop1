package io.pillopl.Shop1;

import java.time.Instant;

public class UserDeactivated implements DomainEvent { /*create field for parameter when*/
    private Instant when;

    public UserDeactivated(Instant when) {
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
}
