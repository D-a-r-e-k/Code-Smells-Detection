/**
     * Throws a ValidationException if the number of validationExceptions
     * detected is greater than zero.
     * 
     * @exception ValidationException
     *              DTD validation exception.
     */
protected void maybeThrowValidationException() throws ValidationException {
    if (validationExceptions.size() > 0) {
        throw new ValidationException("Encountered " + validationExceptions.size() + " validation exceptions.", validationExceptions);
    }
}
