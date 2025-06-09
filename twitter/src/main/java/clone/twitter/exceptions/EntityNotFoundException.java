package clone.twitter.exceptions;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(UUID id) {
        super("Entity with id " + id + " was not found!");
    }
}
