// OAuth 2.0 - Authorization Code

package tests;

import static io.restassured.RestAssured.*;

/*import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;*/

import io.restassured.path.json.JsonPath;

public class OAuthTest {

	// Test using OAuth 2.0 with the grant type as authorization code
	
	// This has 3 main steps:
	// 1. Opening the 'preAuthCodeURL' below using Selenium and logging into a user's Google account to retrieve the authorization code
	// 2. Using the authorization code (and other details such as client ID and secret) to post a request and retrieve an access token
	// 3. Using the access token (this would be used in a cookie) to submit requests
	
	// Using the grant type as client credentials is the same except step 1 isn't required (the scope is required in step 2 instead
	// and the redirect URL should be omitted)
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		// Create URL from contract
								// URL for authorization server (this is the base URL)
		/*String preAuthCodeURL = "https://accounts.google.com/o/oauth2/v2/auth" + "?" +  
								// scope, i.e. the details to be returned by Google to calling application. Note the '&' signs after initial '?' sign.
								"scope=https://www.googleapis.com/auth/userinfo.email" + "&" +
								// URL for authorization server (to generate authorization code)
								"auth_url=https://accounts.google.com/o/oauth2/v2/auth" + "&" +
								// The Client ID given by Google for the application
								"client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com" + "&" +
								// Set response code to code (meaning the authorization code is to be returned)
								"response_type=code" + "&" +
								// The URL to be opened after authenticating via OAuth 2.0
								"redirect_uri=https://rahulshettyacademy.com/getCourse.php";
		
		System.out.println("");
		System.out.println("Pre Authorization Code URL:\t" + preAuthCodeURL);
		System.out.println("");
		
		// Use Selenium to open browser and go to the above URL
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Khalid\\Documents\\Documents\\Courses\\Rest API\\Apps\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(preAuthCodeURL);
		
		// Enter email and press Enter ***NO LONGER WORKS DUE TO GOOGLE BLOCKING THIS FOR AUTOMATION*** (i.e. must be done manually)
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("john.test10.smith@gmail.com");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		
		// Enter password and press Enter
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("restapi10");
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		
		// Retrieve the URL from the browser after logging in
		String authCodeURL = driver.getCurrentUrl();
		
		// Remove the text before the the string "code=" (this removes "code=" as well)
		// (split() method returns a String array containing text before "code=" (in [0]) and text after "code=" (in [1])
		String partialCode = authCodeURL.split("code=")[1];
		
		// Remove the text after the the string "&scope" (this removes "&scope" as well leaving only the authorization code)
		String authCode = partialCode.split("&scope")[0];
		
		System.out.println("Authorization Code:\t" + authCode);
		System.out.println("");*/
		
		// Use the authorization code to generate the end point that needs to be posted to return the access token
		String accessTokenResponse =
		// A valid authorization code must be provided (use urlEncodingEnabled(false) as otherwise special characters may get changed)
		given().urlEncodingEnabled(false).log().all().queryParam("code", /*authCode*/"4%2F0AY0e-g43mvoWXLIDNIc2EQgJgggwS96Gr8tceItUX30rTGcRhXju6-PrOrjtUMGexlgWuQ")
		// The Client ID given by Google for the application
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		// The Client Secret given by Google for the application
		.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W").queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		// The Grant Type being used by the request. The authorization code is being used here but could be other grant types such as
		// Client Credentials and Password Credentials 
		.queryParam("grant_type", "authorization_code")
		// URL for authorization server (to generate access token)
		.when().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		System.out.println("Access Token Response:\t" + accessTokenResponse);
		System.out.println("");
		
		// Retrieve access token from response
		JsonPath jsonAccessTokenResponse = new JsonPath(accessTokenResponse);
		String accessToken = jsonAccessTokenResponse.getString("access_token");
		System.out.println("Access Token:\t\t" + accessToken);
		System.out.println("");
		
		// Use the access token to generate the end point that needs to be used to return the rahulshettyacademy course details
		String rSACourseDetailsResponse =
		given().log().all().queryParam("access_token", accessToken)
		.when().get("https://rahulshettyacademy.com/getCourse.php")
		// No need to use then() method as no assertions are being added
		/*.then().assertThat().statusCode(200).extract().response()*/.asString();
		
		System.out.println("RSA Course Details Response:\t" + rSACourseDetailsResponse);
		System.out.println("");
	}

}
