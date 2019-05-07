package fat;

import fit.*;
import java.io.*;
import java.text.ParseException;

public class AnnotationFixture extends ColumnFixture {
	public String OriginalHTML;
	public int Row;
	public int Column;
	
	public String OverwriteCellBody;
	public String AddToCellBody;
	
	public String OverwriteCellTag;
	public String OverwriteEndCellTag;
	public String AddToCellTag;
	
	public String OverwriteRowTag;
	public String OverwriteEndRowTag;
	public String AddToRowTag;

	public String OverwriteTableTag;
	public String OverwriteEndTableTag;
	public String AddToTableTag;
	
	public String AddCellFollowing;
	public String RemoveFollowingCell;
	
	public String AddRowFollowing;
	public String RemoveFollowingRow;
	
	public String AddTableFollowing;

	public String ResultingHTML() throws Exception {
		Parse table = new Parse(OriginalHTML);
		Parse row = table.at(0, Row - 1);
		Parse cell = row.at(0, Column - 1);
		
		if (OverwriteCellBody != null) cell.body = OverwriteCellBody;
		if (AddToCellBody != null) cell.addToBody(AddToCellBody);
		
        if (OverwriteCellTag != null) cell.tag = OverwriteCellTag;
        if (OverwriteEndCellTag != null) cell.end = OverwriteEndCellTag;
        if (AddToCellTag != null) cell.addToTag(stripDelimiters(AddToCellTag));
        
        if (OverwriteRowTag != null) row.tag = OverwriteRowTag;
        if (OverwriteEndRowTag != null) row.end = OverwriteEndRowTag;
        if (AddToRowTag != null) row.addToTag(stripDelimiters(AddToRowTag));

		if (OverwriteTableTag != null) table.tag = OverwriteTableTag;
		if (OverwriteEndTableTag != null) table.end = OverwriteEndTableTag;
		if (AddToTableTag != null) table.addToTag(stripDelimiters(AddToTableTag));

		if (AddCellFollowing != null) addParse(cell, AddCellFollowing, new String[] {"td"});
		if (RemoveFollowingCell != null) removeParse(cell);
				
		if (AddRowFollowing != null) addParse(row, AddRowFollowing, new String[] {"tr", "td"});
		if (RemoveFollowingRow != null) removeParse(row);
		
		if (AddTableFollowing != null) addParse(table, AddTableFollowing, new String[] {"table", "tr", "td"});

		return GenerateOutput(table);        
	}

    private void addParse(Parse parse, String newString, String[] tags) throws ParseException {
        Parse newParse = new Parse(newString, tags);
        newParse.more = parse.more;
        newParse.trailer = parse.trailer;
        parse.more = newParse;
        parse.trailer = null;
    }

	private void removeParse(Parse parse) {
		parse.trailer = parse.more.trailer;
		parse.more = parse.more.more;
	}
	
	private String stripDelimiters(String s) {
        return s.replaceAll("^\\[", "").replaceAll("]$", "");
    }
	
	// code smell note: copied from DocumentParseFixture	
	private String GenerateOutput(Parse document) throws ParseException {
		StringWriter result = new StringWriter();
		document.print(new PrintWriter(result));
		return result.toString().trim();
	}
}
