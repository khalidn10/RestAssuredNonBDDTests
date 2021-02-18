// Associated Tests (Spec Builder):
// resources.AddPlaceAPIPojo
// resources.Location

package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

//import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.AddPlaceAPIPojo;
import resources.Location;

// Class to test serialisation, i.e. where an object of the corresponding POJO class is used within the request body
// The set methods of the AddPlaceAPIPojo class will be used to set the request Json to the required values
public class AddPlaceAPIPojoSpecBuilderTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Use this in RequestSpecification below
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		
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
		
		/*String addPlaceResponse = given().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(addPlaceApi)
		.when().post("/maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();*/
		
		// Request and Response Spec Builders allow generic parts of the request / response to be re-used in multiple requests. This also
		// simplifies the code and makes it more readable. They can also be put into separate files when you create your test framework.  
		
		// Create a generic RequestSpecification that can be used for all Place API requests (and not just for AddPlaceAPI)
		// Note you must use the build() method at the end
		RequestSpecification placeReqSpec =	new RequestSpecBuilder()
												.setBaseUri("https://rahulshettyacademy.com")
												.addQueryParam("key", "qaclick123")
												.setContentType(ContentType.JSON)//.addHeader("Content-Type", "application/json")
												.build();
		
		// Create a generic ResponseSpecification that can be used for all Place API responses (and not just for AddPlaceAPI)
		// Note you must use the build() method at the end
		ResponseSpecification placeRespSpec =	new ResponseSpecBuilder()
													.expectStatusCode(200)
													//.expectBody("scope", equalTo("APP"))
													// It's best not to include this here as it will make the response non-generic
													// e.g. DeletePlaceAPI does not return "scope" in the response
													.expectHeader("server", "Apache/2.4.18 (Ubuntu)")
													.build();
		
		/* It's best to split the response and request and not use one big code statement as we've been doing until now
		String addPlaceResponse =	given().log().all().spec(placeReqSpec).body(addPlaceApi)
									.when().post("/maps/api/place/add/json")
									.then().log().all().spec(placeRespSpec)
									.extract().response().asString();*/
		
		placeReqSpec = given().log().all().spec(placeReqSpec).body(addPlaceApi);	// given() = request details to be sent
		
		Response placeResponse = placeReqSpec.when().post("/maps/api/place/add/json")					// when() = send request details
								.then().log().all().spec(placeRespSpec).body("scope", equalTo("APP"))	// then() = response for request
								.extract().response();
		
		String addPlaceResponse = placeResponse.asString();
		
		System.out.println("");
		System.out.println("Add Place Response:\t" + addPlaceResponse);
		
		JsonPath addJson = new JsonPath(addPlaceResponse);
		String placeId = addJson.getString("place_id");
		System.out.println("");
		System.out.println("Place ID:\t" + placeId);
	}

}
