package fit;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import java.io.*;
import java.util.*;

public class FileRunner {

    public String input;
    public Parse tables;
    public Fixture fixture = new Fixture();
    public PrintWriter output;

    public static void main(String argv[]) {
    	try {
    		new FileRunner().run(argv);
		} 
		catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
    }

    public void run(String argv[]) throws IOException {
        args(argv);
        process();
        exit();
    }

    public void process() {
        try {
            if (input.indexOf("<wiki>") >= 0) {
                tables = new Parse(input, new String[]{"wiki", "table", "tr", "td"});
                fixture.doTables(tables.parts);
            } else {
                tables = new Parse(input, new String[]{"table", "tr", "td"});
                fixture.doTables(tables);
            }
        } catch (Exception e) {
            exception(e);
        }
        tables.print(output);
    }

    public void args(String[] argv) throws IOException {
        if (argv.length != 2) {
            System.err.println("usage: java fit.FileRunner input-file output-file");
            System.exit(-1);
        }
        File in = new File(argv[0]);
        File out = new File(argv[1]);
        fixture.summary.put("input file", in.getAbsolutePath());
        fixture.summary.put("input update", new Date(in.lastModified()));
        fixture.summary.put("output file", out.getAbsolutePath());
        input = read(in);
        output = new PrintWriter(new BufferedWriter(new FileWriter(out)));
    }

    protected String read(File input) throws IOException {
        char chars[] = new char[(int) (input.length())];
        FileReader in = new FileReader(input);
        in.read(chars);
        in.close();
        return new String(chars);
    }

    protected void exception(Exception e) {
        tables = new Parse("body", "Unable to parse input. Input ignored.", null, null);
        fixture.exception(tables, e);
    }

    protected void exit() {
        output.close();
        System.err.println(fixture.counts());
        System.exit(fixture.counts.wrong + fixture.counts.exceptions);
    }

}
