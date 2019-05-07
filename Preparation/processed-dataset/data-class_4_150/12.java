private ClassLoader findClassloader() {
    // work-around set context loader for windows-service started jvms (QUARTZ-748) 
    if (Thread.currentThread().getContextClassLoader() == null && getClass().getClassLoader() != null) {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }
    return Thread.currentThread().getContextClassLoader();
}
