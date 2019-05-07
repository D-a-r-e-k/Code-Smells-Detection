protected ValueSet getValue(int v, String s, int i) {
    char c = s.charAt(i);
    String s1 = String.valueOf(v);
    while (c >= '0' && c <= '9') {
        s1 += c;
        i++;
        if (i >= s.length()) {
            break;
        }
        c = s.charAt(i);
    }
    ValueSet val = new ValueSet();
    val.pos = (i < s.length()) ? i : i + 1;
    val.value = Integer.parseInt(s1);
    return val;
}
