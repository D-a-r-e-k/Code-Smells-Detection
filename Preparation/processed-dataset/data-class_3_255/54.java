/**
     * Checks if the element has a height of 0.
     *
     * @return true or false
     * @since 2.1.2
     */
public boolean zeroHeightElement() {
    return composite && !compositeElements.isEmpty() && compositeElements.getFirst().type() == Element.YMARK;
}
