package fat;

import fit.*;

public class TableParseFixture extends ColumnFixture {
	
	public String HTML;
	public int Row;
	public int Column;
	
	public String CellBody() throws Exception {
		return cell().body;
	}
	
	public String CellTag() throws Exception {
		return cell().tag;
	}
	
	public String RowTag() throws Exception {
		return row().tag;
	}
	
	public String TableTag() throws Exception {
		return table().tag;
	}
		
	private Parse table() throws Exception {
		return new Parse(HTML);
	}
	
	private Parse row() throws Exception {
		return table().at(0, Row - 1);
	}
	
	private Parse cell() throws Exception {
		return row().at(0, Column - 1);
	}
}
