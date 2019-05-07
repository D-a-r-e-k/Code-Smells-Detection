/**
   * {@inheritDoc}
   */
public void format(final LoggingEvent event, final StringBuffer toAppendTo) {
    toAppendTo.append(event.getThreadName());
}
