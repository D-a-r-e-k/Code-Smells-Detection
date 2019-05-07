package fat;

import fit.*;

public class HtmlToTextFixture extends ColumnFixture {
	public String HTML;
	
	public String Text() {
		HTML = HTML.replaceAll("\\\\u00a0", "\u00a0");
		return escapeAscii(Parse.htmlToText(HTML));
	}

	private String escapeAscii(String text) {
		text = text.replaceAll("\\x0a", "\\\\n");
		text = text.replaceAll("\\x0d", "\\\\r");
		text = text.replaceAll("\\xa0", "\\\\u00a0");
		return text;
	}
}
