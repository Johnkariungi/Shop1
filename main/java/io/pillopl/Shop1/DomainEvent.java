package io.pillopl.Shop1;

import java.time.Instant;

public interface DomainEvent { /*this will only provide us a timestamp of an event*/

    Instant occuredAt();
}
