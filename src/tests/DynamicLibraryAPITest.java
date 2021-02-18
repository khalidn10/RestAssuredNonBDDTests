// Associated Tests (Dynamic Data):
// resources.LibraryBody
// data.properties

package tests;

import org.testng.annotations.AfterSuite;
//import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
//import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import resources.LibraryBody;

import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DynamicLibraryAPITest {

	private static int aisle = 0;
	private static String projectPath = System.getProperty("user.dir");
	private static Properties data = new Properties();
	
	@BeforeSuite
	public static void getAisleProperty() throws IOException
	{
		FileInputStream inputFile = new FileInputStream(projectPath + "/data.properties");
		data.load(inputFile);
		aisle = Integer.parseInt(data.getProperty("aisle"));
		System.out.println("");
		System.out.println("BeforeTest -\t\taisle = " + aisle);
	}
	
	@Test(dataProvider="getParams")
	public void addBook(int aisle)
	{
		System.out.println("Test -\t\t\taisle = " + aisle);
		System.out.println("");
		
		RestAssured.baseURI = "http://216.10.245.166";
		String addBookResponse = given().log().all().header("Content-Type", "application/json").body(LibraryBody.getLibraryBody(aisle))
		.when().post("Library/Addbook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("");
		System.out.println("Add Book Response:\t" + addBookResponse);
		
		JsonPath addJson = new JsonPath(addBookResponse);
		String ID = addJson.getString("ID");
		
		System.out.println("Book ID:\t\t" + ID);
		System.out.println("");
	}
	
	@AfterSuite
	public static void setAisleProperty() throws IOException
	{
		//aisle++;
		data.setProperty("aisle", String.valueOf(aisle));
		FileOutputStream outputFile = new FileOutputStream(projectPath + "/data.properties");
		data.store(outputFile, "Value of aisle property updated");
		System.out.println("AfterTest -\taisle = " + aisle);
	}
	
	@DataProvider
	public Object[][] getParams()
	{
		int noOfTests = 5;
		Object[][] params = new Object[noOfTests][1];
		
		for(int i=0; i<noOfTests; i++)
		{
			System.out.println("DataProvider[" + i + "] -\taisle = " + aisle);
			params[i][0] = aisle;
			aisle++;
		}
		
		return params;
	}
}
