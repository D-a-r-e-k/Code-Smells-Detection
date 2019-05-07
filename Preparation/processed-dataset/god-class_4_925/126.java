/**
	 * Sets the basic confiuration of the QuickServer.
	 * @since 1.4.0
	 */
public void setBasicConfig(BasicServerConfig basicConfig) throws Exception {
    Assertion.affirm(basicConfig != null, "BasicServerConfig can't be null");
    this.basicConfig = basicConfig;
}
