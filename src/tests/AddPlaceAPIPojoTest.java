// Associated Tests (Serialisation):
// resources.AddPlaceAPIPojo
// resources.Location

package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import resources.AddPlaceAPIPojo;
import resources.Location;

// Class to test serialisation, i.e. where an object of the corresponding POJO class is used within the request body
// The set methods of the AddPlaceAPIPojo class will be used to set the request Json to the required values
public class AddPlaceAPIPojoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// POJO (Plain Old Java Object) classes
		
		// The Jar files for serialisation / deserialisation can be downloaded and installed as follows:
		// Google search on 'jackson databind maven'
		// Click on the corresponding link for the maven repository (will normally be the first result)
		// This should take you to the maven repository for 'Jackson Databind'
		// Select the latest stable version
		// In the Files field, select 'View All'
		// Select the jackson-databind-x.x.x.jar file to download it
		// Go back to the previous page (i.e. where the XML for the POM file is displayed)
		// Scroll down to the 'Compile Dependencies' section
		// Select the jackson-annotations link and download the same version of the jackson-annotations-x.x.x.jar file
		// Go back to the 'Compile Dependencies' section
		// Select the jackson-core link and download the same version of the jackson-core-x.x.x.jar file
		// Press the back button and search the maven respository for 'Gson'
		// Select the result for (Google) Gson
		// Download the latest stable version of the gson-x.x.x.jar file
		// Add these 4 Jar files to the Build Path of your Rest Assured (Java) test project in Eclipse
		// Note that some of these 4 Jar files may already have been added when adding the Rest Assured Jar files
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		AddPlaceAPIPojo addPlaceApi = new AddPlaceAPIPojo();
		
		Location location = new Location();
		location.setLat(-38.383494);
		location.setLng(33.427362);
		addPlaceApi.setLocation(location);
		
		addPlaceApi.setAccuracy(50);
		addPlaceApi.setName("Frontline house");
		addPlaceApi.setPhone_number("(+91) 983 893 3937");
		addPlaceApi.setAddress("29, side layout, cohen 09");
		
		List<String> typesList = new ArrayList<String>();
		typesList.add("shoe park");
		typesList.add("shop");
		addPlaceApi.setTypes(typesList);
		
		addPlaceApi.setWebsite("http://google.com");
		addPlaceApi.setLanguage("French-IN");
		
		String addPlaceResponse = given().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(addPlaceApi)
		.when().post("/maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println("");
		System.out.println("Add Place Response:\t" + addPlaceResponse);
		
		// Use JsonPath to parse Json message, i.e. to retrieve certain pieces of data values from the Json request body or the Json response
		JsonPath addJson = new JsonPath(addPlaceResponse);
		String placeId = addJson.getString("place_id");
		System.out.println("");
		System.out.println("Place ID:\t" + placeId);
	}

}
