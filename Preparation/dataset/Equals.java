// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package fat;

import fit.*;
import java.util.Date;

public class Equals extends PrimitiveFixture {

    Parse heads;
    TypeAdapter type;
    Object x, y;

    public void doRows (Parse rows) {
        heads = rows.parts;
        super.doRows(rows.more);
    }

    public void doCell (Parse cell, int col) {
        try {
            char head = heads.at(col).text().charAt(0);
            switch (head) {
                case 't':   type = type(cell.text()); break;
                case 'x':   x = type.parse(cell.text()); break;
                case 'y':   y = type.parse(cell.text()); break;
                case '=':   check(cell, type.equals(x, y)); break;
                case '?':   cell.addToBody(info("x: " + print(x) + " y: " + print(y))); break;

                default:    throw new Exception("don't do " + head);
            }
        } catch (Exception e) {
            exception(cell, e);
        }
    }

    static private Integer [] IntegerArray = {};
    static private Boolean [] BooleanArray = {};
    static private String [] StringArray = {};

    TypeAdapter type(String name) {
        Class type =
            name.equals("boolean") ?    Boolean.class :
            name.equals("integer") ?    Integer.class :
            name.equals("real") ?      Float.class :
            name.equals("string") ?     String.class :
            name.equals("date") ?       Date.class :
            name.equals("money") ?      Money.class :
            name.equals("scientific") ? ScientificDouble.class :
            name.equals("integers") ?   IntegerArray.getClass():
            name.equals("booleans") ?   BooleanArray.getClass():
            name.equals("strings") ?    StringArray.getClass():
            null ;
        if (type == null) throw new RuntimeException("Unimplemented choice "+name);
        return TypeAdapter.on(this, type);
    }

    public Object parse (String s, Class type) throws Exception {
        if (type.equals(Money.class))   return new Money(s);
        if (type.equals(Boolean.class)) return parseCustomBoolean(s);
        return super.parse(s, type);
    }

    Boolean parseCustomBoolean(String s) {
        if (true) throw new RuntimeException("boolean");
        return
            s.startsWith("y") ?         Boolean.TRUE :
            s.startsWith("n") ?         Boolean.FALSE :
            s.startsWith("t") ?         Boolean.TRUE :
            s.startsWith("f") ?         Boolean.FALSE :
            null ;
    }

    String print (Object value) {
        return type.toString(value);
    }

}

