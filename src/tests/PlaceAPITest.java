// Associated Tests (Basic Authentication & CRUD Operations):
// resources.PlaceBody

package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import resources.PlaceBody;

// Note static packages must be imported manually in Eclipse
// as hovering over the relevant class/method (within the package) in the test code
// doesn't give you an option to import the static package
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PlaceAPITest {

	// The Rest Assured jar files can be downloaded and installed as follows:
	// Google search on ' rest assured jar download' and select the option for rest-assured.io
	// Scroll down to Documentation section and select Downloads
	// Select the zip files for:
	// 1. rest-assured-x.x.x-dist
	// 2. json-path-x.x.x-dist
	// 3. json-schema-validator-x.x.x-dist
	// 4. xml-path-x.x.x-dist
	// Note that these zip files have jar files within folders and other zip files. All of these jar files must be added to the
	// BuildPath (Classpath) in Eclipse. 
	// Note that you'll also need to download the jcommander-x.x.jar file (you can do this by searching and downloading it via maven)
	
	// Note that the contract document should detail everything that's required for the request and response including the
	// Base URL, Resource, Path and Query Parameters, Authentication Type, Request Type, Header and Json Body (example),
	// Response Header and Json Body (example), Success and Fail Status Codes, etc.
	
	// Test using Basic Authentication, i.e. using a key as a query parameter
	
	// CRUD stands for create, read, update and delete
	// Post requests are used when requesting to create (write) something in the back-end server
	// Get requests are used when requesting to retrieve (read) something from the back-end server (this normally doesn't require a request body)
	// Put requests are used when requesting to update something from the back-end server
	// Delete requests are used when requesting to delete something from the back-end server
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// The base URL
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		// Validate if Add Place API is working as expected
		// Given - all input details (apart from http method and resource)
		// It's good practice to put the Json body (to be included in the request) in a separate class
		// using a static get method with input parameters (if required)
		String addPlaceResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(PlaceBody.getPlaceBody("Place1", ""))
		// When - submit the API (give http method and resource here)
		.when().post("/maps/api/place/add/json")
		// Then - validate the response
		.then()/*.log().all()*/.assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println("");
		System.out.println("Add Place Response:\t" + addPlaceResponse);
		
		// Use JsonPath to parse Json message, i.e. to retrieve certain pieces of data values from the Json request body or the Json response
		JsonPath addJson = new JsonPath(addPlaceResponse);
		String placeId = addJson.getString("place_id");
		System.out.println("");
		System.out.println("Place ID:\t" + placeId);
		System.out.println("");
		System.out.println("**********");
		System.out.println("");
		
		// Validate if Update Place API is working as expected
		String updatePlaceResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(PlaceBody.getPlaceBody("Place2", placeId))
		.when().put("/maps/api/place/update/json")
		.then()/*.log().all()*/.assertThat().statusCode(200).body("msg", containsStringIgnoringCase("address success")).header("server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println("");
		System.out.println("Update Place Response:\t" + updatePlaceResponse);
		
		JsonPath updateJson = new JsonPath(updatePlaceResponse);
		String msg = updateJson.getString("msg");
		System.out.println("");
		System.out.println("msg:\t" + msg);
		System.out.println("");
		System.out.println("**********");
		System.out.println("");
		
		// Validate if Get Place API is working as expected
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().put("/maps/api/place/get/json")
		.then()/*.log().all()*/.assertThat().statusCode(200).body("address", equalTo("70 Summer walk, USA")).header("server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
		
		System.out.println("");
		System.out.println("Get Place Response:\t" + getPlaceResponse);
		
		JsonPath getJson = new JsonPath(getPlaceResponse);
		String address = getJson.getString("address");
		System.out.println("");
		System.out.println("Address:\t" + address);
	}

}
