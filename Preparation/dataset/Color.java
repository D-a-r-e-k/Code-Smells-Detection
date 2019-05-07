// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package fat;

import fit.*;

public class Color extends PrimitiveFixture {

    Parse actualRow;

    public void doRows(Parse rows) {
        actualRow = Table.table.parts;
        if (rows.size() != actualRow.size())
            throw new RuntimeException("wrong size table");
        super.doRows(rows);
    }

    public void doRow(Parse row) {
        super.doRow(row);
        actualRow = actualRow.more;
    }

    public void doCell(Parse cell, int columnNumber) {
        check(cell, color(actualRow.parts.at(columnNumber)));
    }

    String color (Parse cell) {
        String b = extract(cell.tag, "bgcolor=\"", "white");
        String f = extract(cell.body, "<font color=", "black");
        return f.equals("black") ? b : f+"/"+b;
    }

    String extract (String text, String pattern, String defaultColor) {
        int index = text.indexOf(pattern);
        if (index < 0) return defaultColor;
        index += pattern.length();
        return decode(text.substring(index, index+7));
    }

    String decode(String code) {
        return
            code.equals(Fixture.red) ?      "red" :
            code.equals(Fixture.green) ?    "green" :
            code.equals(Fixture.yellow) ?   "yellow" :
            code.equals(Fixture.gray) ?     "gray" :
            code.equals("#808080") ?        "gray" :
            code;
    }

}