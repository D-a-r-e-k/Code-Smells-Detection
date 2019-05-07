/**
	 * Sets the classloader to be used to load the dynamicaly resolved 
	 * classes
	 * @since 1.3.3
	 */
public void setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
    Thread.currentThread().setContextClassLoader(classLoader);
}
