package fat;

import fit.*;
import java.io.*;
import java.text.ParseException;

public class StandardAnnotationFixture extends ColumnFixture {
	public String OriginalHTML = "Text";
	public String Annotation;
	public String Text;
	
	public String Output() throws ParseException {
		Parse parse = new Parse(OriginalHTML, new String[] {"td"});
		Fixture testbed = new Fixture();
		
		if (Annotation.equals("right")) testbed.right(parse);
		if (Annotation.equals("wrong")) testbed.wrong(parse, Text);
		if (Annotation.equals("error")) testbed.error(parse, Text);
		if (Annotation.equals("info")) testbed.info(parse, Text); 
		if (Annotation.equals("ignore")) testbed.ignore(parse);
				
		return GenerateOutput(parse); 
	}
	
	public void doCell(Parse cell, int column) {
		try {
			if (column == 4) {
				cell.body = RenderedOutput();
			}
			else {
				super.doCell(cell, column);
			}
		}
		catch (Exception e) {
			exception(cell, e);
		}	
	}
	
	public String RenderedOutput() throws ParseException {
		return "<table border='1'><tr>" + Output() + "</tr></table>";		
	}
	
	// code smell note: copied from ParseFixture	
	private String GenerateOutput(Parse parse) {
		StringWriter result = new StringWriter();
		parse.print(new PrintWriter(result));
		return result.toString().trim();
	}
}
