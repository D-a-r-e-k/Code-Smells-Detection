// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package fat;

public class Money {
    long cents;

    public Money (String s) {
        String stripped = "";
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                stripped += c;
            }
        }
        cents = (long) (100 * Double.parseDouble(stripped));
    }

    public boolean equals(Object value) {
        return (value instanceof Money) && cents == ((Money)value).cents;
    }

    public int hashCode() {
        return (int)cents;
    }

    public String toString () {
        return cents + " cents";
    }
}

