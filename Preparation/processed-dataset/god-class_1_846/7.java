protected Method method(String test, int args) throws NoSuchMethodException {
    Method methods[] = actor.getClass().getMethods();
    Method result = null;
    for (int i = 0; i < methods.length; i++) {
        Method m = methods[i];
        if (m.getName().equals(test) && m.getParameterTypes().length == args) {
            if (result == null) {
                result = m;
            } else {
                throw new NoSuchMethodException("too many implementations");
            }
        }
    }
    if (result == null) {
        throw new NoSuchMethodException();
    }
    return result;
}
