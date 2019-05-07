package fat;

import fit.*;
import java.text.*;
import java.io.*;

public class DocumentParseFixture extends ColumnFixture {
	public String HTML;
	public String Note;  // non-functional
	
	public String Output() throws ParseException {
		return GenerateOutput(new Parse(HTML));
	}

	public String Structure() throws ParseException {
		return dumpTables(new Parse(HTML));		
	}
	
	private String GenerateOutput(Parse parse) {
		StringWriter result = new StringWriter();
		parse.print(new PrintWriter(result));
		return result.toString();
	}
		
	private String dumpTables(Parse table) {
		String result = "";
		String separator = "";
		while (table != null) {
			result += separator;
			result += dumpRows(table.parts);
			separator = "\n----\n";
			table = table.more;
		}
		return result;
	}
	
	private String dumpRows(Parse row) {
		String result = "";
		String separator = "";
		while (row != null) {
			result += separator;
			result += dumpCells(row.parts);
			separator = "\n";
			row = row.more;
		}
		return result;
	}
	
	private String dumpCells(Parse cell) {
		String result = "";
		String separator = "";
		while (cell != null) {
			result += separator;
			result += "[" + cell.body + "]";
			separator = " ";
			cell = cell.more;
		}
		return result;
	}
}
