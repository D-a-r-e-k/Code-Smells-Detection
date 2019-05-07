package fit;

// Copyright (c) 2002-2005 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.text.DateFormat;

public class Fixture {

    public Map summary = new HashMap();
    public Counts counts = new Counts();
    protected String[] args;

    public class RunTime {
        long start = System.currentTimeMillis();
        long elapsed = 0;

        public String toString() {
            elapsed = (System.currentTimeMillis()-start);
            if (elapsed > 600000) {
                return d(3600000)+":"+d(600000)+d(60000)+":"+d(10000)+d(1000);
            } else {
                return d(60000)+":"+d(10000)+d(1000)+"."+d(100)+d(10);
            }
        }

        String d(long scale) {
            long report = elapsed / scale;
            elapsed -= report * scale;
            return Long.toString(report);
        }
    }



    // Traversal //////////////////////////

	/* Altered by Rick Mugridge to dispatch on the first Fixture */
    public void doTables(Parse tables) {
        summary.put("run date", new Date());
        summary.put("run elapsed time", new RunTime());
        if (tables != null) {
        	Parse fixtureName = fixtureName(tables);
            if (fixtureName != null) {
                try {
                    Fixture fixture = getLinkedFixtureWithArgs(tables);
                    fixture.interpretTables(tables);
                } catch (Exception e) {
                    exception (fixtureName, e);
                    interpretFollowingTables(tables);
                }
            }
        }
    }

    /* Added by Rick Mugridge to allow a dispatch into DoFixture */
    protected void interpretTables(Parse tables) {
  		try { // Don't create the first fixture again, because creation may do something important.
  			getArgsForTable(tables); // get them again for the new fixture object
  			doTable(tables);
  		} catch (Exception ex) {
  			exception(fixtureName(tables), ex);
  			return;
  		}
  		interpretFollowingTables(tables);
  	}

    /* Added by Rick Mugridge */
    private void interpretFollowingTables(Parse tables) {
        //listener.tableFinished(tables);
            tables = tables.more;
        while (tables != null) {
            Parse fixtureName = fixtureName(tables);
            if (fixtureName != null) {
                try {
                    Fixture fixture = getLinkedFixtureWithArgs(tables);
                    fixture.doTable(tables);
                } catch (Throwable e) {
                    exception(fixtureName, e);
		        }
		    }
            //listener.tableFinished(tables);
            tables = tables.more;
        }
    }

    /* Added from FitNesse*/
	protected Fixture getLinkedFixtureWithArgs(Parse tables) throws Exception {
		Parse header = tables.at(0, 0, 0);
        Fixture fixture = loadFixture(header.text());
		fixture.counts = counts;
		fixture.summary = summary;
		fixture.getArgsForTable(tables);
		return fixture;
	}
	
	public Parse fixtureName(Parse tables) {
		return tables.at(0, 0, 0);
	}

	public Fixture loadFixture(String fixtureName)
	throws InstantiationException, IllegalAccessException {
		String notFound = "The fixture \"" + fixtureName + "\" was not found.";
		try {
			return (Fixture)(Class.forName(fixtureName).newInstance());
		}
		catch (ClassCastException e) {
			throw new RuntimeException("\"" + fixtureName + "\" was found, but it's not a fixture.", e);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(notFound, e);
		}
		catch (NoClassDefFoundError e) {
			throw new RuntimeException(notFound, e);
		}
	}

	/* Added by Rick Mugridge, from FitNesse */
	protected void getArgsForTable(Parse table) {
	    ArrayList argumentList = new ArrayList();
	    Parse parameters = table.parts.parts.more;
	    for (; parameters != null; parameters = parameters.more)
	        argumentList.add(parameters.text());
	    args = (String[]) argumentList.toArray(new String[0]);
	}

    public void doTable(Parse table) {
        doRows(table.parts.more);
    }

    public void doRows(Parse rows) {
        while (rows != null) {
            Parse more = rows.more;
            doRow(rows);
            rows = more;
        }
    }

    public void doRow(Parse row) {
        doCells(row.parts);
    }

    public void doCells(Parse cells) {
        for (int i=0; cells != null; i++) {
            try {
                doCell(cells, i);
            } catch (Exception e) {
                exception(cells, e);
            }
            cells=cells.more;
        }
    }

    public void doCell(Parse cell, int columnNumber) {
        ignore(cell);
    }


    // Annotation ///////////////////////////////

    public static String green = "#cfffcf";
    public static String red = "#ffcfcf";
    public static String gray = "#efefef";
    public static String yellow = "#ffffcf";

    public  void right (Parse cell) {
        cell.addToTag(" bgcolor=\"" + green + "\"");
        counts.right++;
    }

    public void wrong (Parse cell) {
        cell.addToTag(" bgcolor=\"" + red + "\"");
		cell.body = escape(cell.text());
        counts.wrong++;
    }

    public void wrong (Parse cell, String actual) {
        wrong(cell);
        cell.addToBody(label("expected") + "<hr>" + escape(actual) + label("actual"));
    }

	public void info (Parse cell, String message) {
		cell.addToBody(info(message));
	}

	public String info (String message) {
		return " <font color=\"#808080\">" + escape(message) + "</font>";
	}

    public void ignore (Parse cell) {
        cell.addToTag(" bgcolor=\"" + gray + "\"");
        counts.ignores++;
    }

	public void error (Parse cell, String message) {
		cell.body = escape(cell.text());
		cell.addToBody("<hr><pre>" + escape(message) + "</pre>");
		cell.addToTag(" bgcolor=\"" + yellow + "\"");
		counts.exceptions++;
	}

    public void exception (Parse cell, Throwable exception) {
        while(exception.getClass().equals(InvocationTargetException.class)) {
            exception = ((InvocationTargetException)exception).getTargetException();
        }
        final StringWriter buf = new StringWriter();
        exception.printStackTrace(new PrintWriter(buf));
        error(cell, buf.toString());
    }

    // Utility //////////////////////////////////

    public String counts() {
        return counts.toString();
    }

    public static String label (String string) {
        return " <font size=-1 color=\"#c08080\"><i>" + string + "</i></font>";
    }

    public static String escape (String string) {
    	string = string.replaceAll("&", "&amp;");
    	string = string.replaceAll("<", "&lt;");
    	string = string.replaceAll("  ", " &nbsp;");
		string = string.replaceAll("\r\n", "<br />");
		string = string.replaceAll("\r", "<br />");
		string = string.replaceAll("\n", "<br />");
    	return string;
    }

    public static String camel (String name) {
        StringBuffer b = new StringBuffer(name.length());
        StringTokenizer t = new StringTokenizer(name);
        if (!t.hasMoreTokens())
            return name;
        b.append(t.nextToken());
        while (t.hasMoreTokens()) {
            String token = t.nextToken();
            b.append(token.substring(0, 1).toUpperCase());      // replace spaces with camelCase
            b.append(token.substring(1));
        }
        return b.toString();
    }

    public Object parse (String s, Class type) throws Exception {
        if (type.equals(String.class))              {return s;}
        if (type.equals(Date.class))                {return DateFormat.getDateInstance().parse(s);}
        if (type.equals(ScientificDouble.class))    {return ScientificDouble.valueOf(s);}
        throw new Exception("can't yet parse "+type);
    }

    public void check(Parse cell, TypeAdapter a) {
        String text = cell.text();
        if (text.equals("")) {
            try {
                info(cell, a.toString(a.get()));
            } catch (Exception e) {
                info(cell, "error");
            }
        } else if (a == null) {
            ignore(cell);
        } else  if (text.equals("error")) {
            try {
                Object result = a.invoke();
                wrong(cell, a.toString(result));
            } catch (IllegalAccessException e) {
                exception (cell, e);
            } catch (Exception e) {
                right(cell);
            }
        } else {
            try {
                Object result = a.get();
                if (a.equals(a.parse(text), result)) {
                    right(cell);
                } else {
                    wrong(cell, a.toString(result));
                }
            } catch (Exception e) {
                exception(cell, e);
            }
        }
    }

	/* Added by Rick, from FitNesse */
    public String[] getArgs() {
        return args;
    }

}
