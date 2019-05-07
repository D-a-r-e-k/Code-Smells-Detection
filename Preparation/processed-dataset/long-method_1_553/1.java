/**
   * Create new pattern converter.
   * @return pattern converter.
   */
public org.apache.log4j.helpers.PatternConverter parse() {
    return new BridgePatternConverter(pattern);
}
