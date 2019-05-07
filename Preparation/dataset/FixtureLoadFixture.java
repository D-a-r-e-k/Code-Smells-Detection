package fat;

import fit.*;

public class FixtureLoadFixture extends ColumnFixture {
	public String FixtureName;
	
	public String LoadResult() throws Exception {
        loadFixture();
		return "loaded";    // we'll get an exception if it didn't load
	}

    private void loadFixture()
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Fixture fixture = new Fixture();
        fixture.loadFixture(FixtureName);
    }
	
	public String ErrorMessage() {
		try {
			loadFixture();
			return "(none)";
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}
}




