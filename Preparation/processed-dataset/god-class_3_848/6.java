public Fixture loadFixture(String fixtureName) throws InstantiationException, IllegalAccessException {
    String notFound = "The fixture \"" + fixtureName + "\" was not found.";
    try {
        return (Fixture) (Class.forName(fixtureName).newInstance());
    } catch (ClassCastException e) {
        throw new RuntimeException("\"" + fixtureName + "\" was found, but it's not a fixture.", e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(notFound, e);
    } catch (NoClassDefFoundError e) {
        throw new RuntimeException(notFound, e);
    }
}
