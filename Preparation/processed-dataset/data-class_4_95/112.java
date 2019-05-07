/**
	 * Utility method to load a class
	 * @since 1.3.3
	 */
public Class getClass(String name, boolean reload) throws ClassNotFoundException {
    if (name == null)
        throw new IllegalArgumentException("Class name can't be null!");
    logger.finest("Class: " + name + ", reload: " + reload);
    if (reload == true && classLoader != null) {
        return classLoader.loadClass(name);
    } else if (reload == true && classLoader == null && this.getClass().getClassLoader() != null) {
        return this.getClass().getClassLoader().loadClass(name);
    } else if (reload == false && classLoader != null) {
        return Class.forName(name, true, classLoader);
    } else /*if(reload==false && classLoader==null)*/
    {
        return Class.forName(name, true, this.getClass().getClassLoader());
    }
}
