// Associated Tests (Deserialisation):
// resources.GetCoursesPojo
// resources.WebAutomation
// resources.Api
// resources.Courses
// resources.Mobile

package tests;

import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//import org.testng.Assert;
import org.testng.asserts.SoftAssert;

//import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import resources.GetCoursesPojo;
import resources.WebAutomation;

//Class to test deserialisation, i.e. where an object of the corresponding POJO class is used within the response body
//The get methods of the GetCoursesPojo class will be used to retrieve the values from the response Json to an object of the Pojo class
public class GetCoursesPojoTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String accessTokenResponse =
		given().urlEncodingEnabled(false).log().all().queryParam("code", /*authCode*/"4%2F0AY0e-g4p6PNtNQH0hVkdW_aCLq_tgd9MjbaX5mla5so7V6lb47_gpc_yghucLyo0sWF3fQ")
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W").queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.queryParam("grant_type", "authorization_code")
		.when().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		System.out.println("Access Token Response:\t" + accessTokenResponse);
		System.out.println("");
		
		// Retrieve access token from response
		JsonPath jsonAccessTokenResponse = new JsonPath(accessTokenResponse);
		String accessToken = jsonAccessTokenResponse.getString("access_token");
		System.out.println("Access Token:\t\t" + accessToken);
		System.out.println("");
		
		// Use the access token to generate the end point that needs to be used to return the rahulshettyacademy course details
		// Note the use of the expect().defaultParser() method to ensure the Pojo class is parsed as Json
		// Note also the use of the as(class) method to return the response as an object of the Pojo class
		GetCoursesPojo getCoursesResponse =
		given().log().all().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php")
		.then().log().all().assertThat().statusCode(200).extract().response().as(GetCoursesPojo.class);
		
		//System.out.println("RSA Course Details Response:\t" + rSACourseDetailsResponse);
		//System.out.println("");
		
		SoftAssert sAssert = new SoftAssert();
		
		sAssert.assertTrue(getCoursesResponse.getInstructor().equals("RahulShetty"), "Unexpected instructor sent in response");
		sAssert.assertTrue(getCoursesResponse.getUrl().equals("rahulshettycademy.com"), "Unexpected URL sent in response");
		sAssert.assertTrue(getCoursesResponse.getServices().equals("projectSupport"), "Unexpected services sent in response");
		sAssert.assertTrue(getCoursesResponse.getExpertise().equals("Automation"), "Unexpected expertise sent in response");
		
		List<WebAutomation> expectedWebAutomationList = new ArrayList<>();
		WebAutomation webAutomation1 = new WebAutomation();
		WebAutomation webAutomation2 = new WebAutomation();
		WebAutomation webAutomation3 = new WebAutomation();
		//WebAutomation webAutomation4 = new WebAutomation();
		webAutomation2.setCourseTitle("Selenium Webdriver Java");
		webAutomation2.setPrice(50);
		webAutomation1.setCourseTitle("Cypress");
		webAutomation1.setPrice(40);
		webAutomation3.setCourseTitle("Protractor");
		webAutomation3.setPrice(40);
		/*webAutomation4.setCourseTitle("QTP");
		webAutomation4.setPrice(60);*/
		expectedWebAutomationList.add(webAutomation1);
		expectedWebAutomationList.add(webAutomation2);
		expectedWebAutomationList.add(webAutomation3);
		//expectedWebAutomationList.add(webAutomation4);
		//expectedWebAutomationList = expectedWebAutomationList.stream().sorted().collect(Collectors.toList());
		expectedWebAutomationList = expectedWebAutomationList.stream()
		.sorted(Comparator.comparing(WebAutomation::getCourseTitle))
        .collect(Collectors.toList());
		
		List<WebAutomation> webAutomationList = getCoursesResponse.getCourses().getWebAutomation();
		//webAutomationList = webAutomationList.stream().sorted().collect(Collectors.toList());
		webAutomationList = webAutomationList.stream()
		.sorted(Comparator.comparing(WebAutomation::getCourseTitle))
        .collect(Collectors.toList());
		
		if (webAutomationList.size() != expectedWebAutomationList.size())
		{
			sAssert.assertEquals(webAutomationList.size(), expectedWebAutomationList.size(), "Incorrect count of Web Automation courses in response");
		}
		
		if (!webAutomationList.equals(expectedWebAutomationList))
		{
			int k = 0;
			boolean matchFound = false;
			
			for (int i = 0; i < webAutomationList.size(); i++)
			{
				matchFound = false;
				
				for (int j = k; j < expectedWebAutomationList.size(); j++)
				{
					if (webAutomationList.get(i).getCourseTitle().equals(expectedWebAutomationList.get(j).getCourseTitle()))
					{
						if (webAutomationList.get(i).getPrice() != (expectedWebAutomationList.get(j).getPrice()))
						{
							sAssert.assertEquals(webAutomationList.get(i).getPrice(), expectedWebAutomationList.get(i).getPrice(), "Web automation course " + webAutomationList.get(i).getCourseTitle() + " has incorrect price");
						}
						
						k = j;
						matchFound = true;
						break;
					}
				}
				
				if (!matchFound)
				{
					sAssert.assertFalse(true, "Unexpected Web Automation Course found in response: " + webAutomationList.get(i).getCourseTitle());
				}
			}
			
			k = 0;
			matchFound = false;
			
			for (int i = 0; i < expectedWebAutomationList.size(); i++)
			{
				matchFound = false;
				
				for (int j = k; j < webAutomationList.size(); j++)
				{
					if (expectedWebAutomationList.get(i).getCourseTitle().equals(webAutomationList.get(j).getCourseTitle()))
					{
						k = j;
						matchFound = true;
						break;
					}
				}
				
				if (!matchFound)
				{
					sAssert.assertTrue(false, "Web Automation Course " + expectedWebAutomationList.get(i).getCourseTitle() + " not found in response");
				}
			}
		}
		
		sAssert.assertTrue(getCoursesResponse.getLinkedIn().equals("https://www.linkedin.com/in/rahul-shetty-trainer/"), "Unexpected LinkedIn link sent in response");
		
		sAssert.assertAll();
	}
}
