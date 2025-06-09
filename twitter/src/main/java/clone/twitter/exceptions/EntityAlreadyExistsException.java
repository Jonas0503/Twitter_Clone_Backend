package clone.twitter.exceptions;

import java.util.UUID;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(UUID id) {
        super("Entity with id " + id + " already exists!");
    }
}
