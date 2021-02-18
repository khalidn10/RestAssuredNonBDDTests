// Associated Tests (Dynamic Data):
// tests.DynamicLibraryAPITest

package resources;

public class LibraryBody {
	
	public static String getLibraryBody(int aisle)
	{
		String libraryBody =
		"{\r\n" + 
		"\r\n" + 
		"\"name\":\"Learn Appium Automation with Java\",\r\n" + 
		"\"isbn\":\"abc\",\r\n" + 
		"\"aisle\":\"" + aisle + "\",\r\n" + 
		"\"author\":\"John foe\"\r\n" + 
		"}";
		
		return libraryBody;
	}
}
