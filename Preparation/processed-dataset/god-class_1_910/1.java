@Override
public Runner runnerForClass(Class<?> testClass) {
    if (testClass.getAnnotation(Ignore.class) != null) {
        return new IgnoredClassRunner(testClass);
    }
    return null;
}
