// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.


package fat;

import fit.*;
import java.util.*;

public class Frameworks extends ColumnFixture {

    public static Map runscripts = new HashMap();

    public String language;
    public String page;
    public String runscript;

    public void reset() {
        language = page = runscript = null;
    }

    public void execute() {
        if (language != null && runscript != null) {
            runscripts.put(language, runscript);
        }
    }
}
