private void println(int level, String s) {
    for (int i = 0; i < level; i++) {
        System.out.print(INDENT);
    }
    System.out.println(s);
}
