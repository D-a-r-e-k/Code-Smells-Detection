package fat;

import java.text.ParseException;
import fit.*;

public class FixtureNameFixture extends ColumnFixture {
	public String Table;
	
	public String FixtureName() throws Exception {
		Parse tableParse = GenerateTableParse(Table);
		
		String result = fixtureName(tableParse).text();
		if (result.equals("")) return "(missing)";
		return result;
	}
	
	private Parse GenerateTableParse(String table) throws ParseException {
		String[] rows = table.split("\n");
		return new Parse("table", null, GenerateRowParses(rows, 0), null);
	}

	private Parse GenerateRowParses(String[] rows, int rowIndex) {
		if (rowIndex >= rows.length) return null;
		
		String[] cells = rows[rowIndex].split("\\]\\s*\\[");
		if (cells.length != 0) {
			cells[0] = cells[0].substring(1); // strip beginning '['
			int lastCell = cells.length - 1;
			cells[lastCell] = cells[lastCell].replaceAll("\\]$", "");  // strip ending ']' 
		}
		
		return new Parse("tr", null, GenerateCellParses(cells, 0), GenerateRowParses(rows, rowIndex+1));
	}		

	private Parse GenerateCellParses(String[] cells, int cellIndex) {
		if (cellIndex >= cells.length) return null;
		
		return new Parse("td", cells[cellIndex], null, GenerateCellParses(cells, cellIndex + 1));
	}
}




