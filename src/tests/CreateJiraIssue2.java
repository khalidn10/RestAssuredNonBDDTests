package tests;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.IOException;

public class CreateJiraIssue2 {
	
	public static void main(String[] args) throws IOException
	{	
		RestAssured.baseURI = "http://localhost:8080";
		SessionFilter session = new SessionFilter();
		
		String jSessionBody =
		"{\r\n" + 
		"\"username\": \"*********\",\r\n" + 
		"\"password\": \"*********\"\r\n" + 
		"}";
		
		String jSessionResponse =
		given().log().all().header("Content-Type", "application/json").body(jSessionBody)
		.filter(session).when().post("/rest/auth/1/session")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("");
		System.out.println("JSession Response:\t" + jSessionResponse);
		System.out.println("");
		
		JsonPath jsonJSessionResponse = new JsonPath(jSessionResponse);
		String jSessionId = jsonJSessionResponse.getString("session.value");
		System.out.println("JSessionID:\t" + jSessionId);
		System.out.println("");
		
		String bugBody =
		"{\r\n" + 
		"    \"fields\": \r\n" + 
		"    {\r\n" + 
		"        \"project\": \r\n" + 
		"	    {\r\n" + 
		"            \"key\": \"MYP\"\r\n" + 
		"        },\r\n" + 
		"        \"summary\": \"My 29th Bug through Jira API\",\r\n" + 
		"        \"issuetype\": \r\n" + 
		"	    {\r\n" + 
		"            \"name\": \"Bug\"\r\n" + 
		"        }\r\n" + 
		"    }\r\n" + 
		"}";
		
		String bugResponse =
		given().log().all().header("Content-Type", "application/json").header("Cookie", "JSESSIONID=" + jSessionId).body(bugBody)
		.filter(session).when().post("/rest/api/2/issue")
		.then().assertThat().statusCode(201).extract().response().asString();
		
		System.out.println("");
		System.out.println("Bug Response:\t" + bugResponse);
		System.out.println("");
		
		JsonPath jsonBugResponse = new JsonPath(bugResponse);
		String bugId = jsonBugResponse.getString("id");
		System.out.println("Bug ID:\t" + bugId);
		System.out.println("");
		
		
	}
}
