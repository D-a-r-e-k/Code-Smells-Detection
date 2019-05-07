// sort modes alphabetically 
private static void sortModes() {
    String[] modeNames = new String[modes.size()];
    for (int i = 0; i < modeNames.length; i++) modeNames[i] = ((Mode) modes.get(i)).getUserModeName();
    Arrays.sort(modeNames);
    ArrayList v = new ArrayList(modeNames.length);
    for (int i = 0; i < modeNames.length; i++) {
        int j = 0;
        String name = modeNames[i];
        while (!((Mode) modes.get(j)).getUserModeName().equals(name)) {
            if (j == modes.size() - 1)
                break;
            else
                j++;
        }
        v.add(modes.get(j));
    }
    modes = v;
    v = null;
}
