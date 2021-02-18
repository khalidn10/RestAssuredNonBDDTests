// Associated Tests (Static Json Files):
// add_book.json

package tests;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticLibraryAPITest {

	@Test
	public void addBook() throws IOException
	{
		String projectPath = System.getProperty("user.dir");
		
		RestAssured.baseURI = "http://216.10.245.166";
		String addBookResponse = given().log().all().header("Content-Type", "application/json")
		// Convert Json file to (bytes and then ) a string
		.body(new String(Files.readAllBytes(Paths.get(projectPath + "/add_book.json"))))
		.when().post("Library/Addbook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("");
		System.out.println("Add Book Response:\t" + addBookResponse);
		System.out.println("");
		
		JsonPath addJson = new JsonPath(addBookResponse);
		String ID = addJson.getString("ID");
		
		System.out.println("Book ID:\t\t" + ID);
		System.out.println("");
	}
}
