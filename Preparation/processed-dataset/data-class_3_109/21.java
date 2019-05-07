private Config getConfig() {
    List services = JagGenerator.getObjectsFromTree(Config.class);
    for (int i = 0; i < services.size(); i++) {
        Config c = (Config) services.get(i);
        return c;
    }
    return null;
}
