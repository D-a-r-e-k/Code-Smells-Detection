protected int checkNext(int pos, String s, int val, int type) throws ParseException {
    int end = -1;
    int i = pos;
    if (i >= s.length()) {
        addToSet(val, end, -1, type);
        return i;
    }
    char c = s.charAt(pos);
    if (c == 'L') {
        if (type == DAY_OF_WEEK) {
            if (val < 1 || val > 7)
                throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
            lastdayOfWeek = true;
        } else {
            throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
        }
        TreeSet set = getSet(type);
        set.add(new Integer(val));
        i++;
        return i;
    }
    if (c == 'W') {
        if (type == DAY_OF_MONTH) {
            nearestWeekday = true;
        } else {
            throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
        }
        TreeSet set = getSet(type);
        set.add(new Integer(val));
        i++;
        return i;
    }
    if (c == '#') {
        if (type != DAY_OF_WEEK) {
            throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i);
        }
        i++;
        try {
            nthdayOfWeek = Integer.parseInt(s.substring(i));
            if (nthdayOfWeek < 1 || nthdayOfWeek > 5) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
        }
        TreeSet set = getSet(type);
        set.add(new Integer(val));
        i++;
        return i;
    }
    if (c == '-') {
        i++;
        c = s.charAt(i);
        int v = Integer.parseInt(String.valueOf(c));
        end = v;
        i++;
        if (i >= s.length()) {
            addToSet(val, end, 1, type);
            return i;
        }
        c = s.charAt(i);
        if (c >= '0' && c <= '9') {
            ValueSet vs = getValue(v, s, i);
            int v1 = vs.value;
            end = v1;
            i = vs.pos;
        }
        if (i < s.length() && ((c = s.charAt(i)) == '/')) {
            i++;
            c = s.charAt(i);
            int v2 = Integer.parseInt(String.valueOf(c));
            i++;
            if (i >= s.length()) {
                addToSet(val, end, v2, type);
                return i;
            }
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ValueSet vs = getValue(v2, s, i);
                int v3 = vs.value;
                addToSet(val, end, v3, type);
                i = vs.pos;
                return i;
            } else {
                addToSet(val, end, v2, type);
                return i;
            }
        } else {
            addToSet(val, end, 1, type);
            return i;
        }
    }
    if (c == '/') {
        i++;
        c = s.charAt(i);
        int v2 = Integer.parseInt(String.valueOf(c));
        i++;
        if (i >= s.length()) {
            addToSet(val, end, v2, type);
            return i;
        }
        c = s.charAt(i);
        if (c >= '0' && c <= '9') {
            ValueSet vs = getValue(v2, s, i);
            int v3 = vs.value;
            addToSet(val, end, v3, type);
            i = vs.pos;
            return i;
        } else {
            throw new ParseException("Unexpected character '" + c + "' after '/'", i);
        }
    }
    addToSet(val, end, 0, type);
    i++;
    return i;
}
