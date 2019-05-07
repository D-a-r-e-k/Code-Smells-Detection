// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package fat;

import fit.Parse;
import fit.Fixture;

public class Table extends Fixture {
    public static Parse table;

    public void doRows(Parse rows) {
        Table.table = new Parse ("table", null, copy(rows), null);
        // evaluate the rest of the table like a runner
        (new Fixture()).doTables(Table.table);
    }

    static Parse copy(Parse tree) {
        // if (2+2==4)return tree;
        return (tree == null)
            ? null
            : new Parse(tree.tag, tree.body, copy(tree.parts), copy(tree.more));
    }
}
