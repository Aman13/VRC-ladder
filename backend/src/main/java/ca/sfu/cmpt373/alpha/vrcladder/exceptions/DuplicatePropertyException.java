package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

/**
 * The DuplicatePropertyException is thrown when the primary key or unique constraint is violated
 */
public class DuplicatePropertyException extends RuntimeException {

    private String propertyName;

    public DuplicatePropertyException(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
