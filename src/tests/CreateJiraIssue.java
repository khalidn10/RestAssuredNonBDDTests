// Associated Tests (Cookies based Authentication in Jira):
// resources.BugBody
// resources.CommentBody
// resources.SessionBody
// data.properties
// jira_attachment.txt

package tests;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import resources.BugBody;
import resources.CommentBody;
import resources.SessionBody;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CreateJiraIssue {
	
	// Test using Cookies based Authentication, i.e. using a JSessionID
	
	private static String projectPath = System.getProperty("user.dir");
	private static Properties data = new Properties();
	private static int bugNo = 0;
	
	SessionFilter session = new SessionFilter();
	String jSessionId = "";
	String bugId = "";
	List<String> commentIds = new ArrayList<>();
	
	@BeforeSuite
	public static void getBugNo() throws IOException
	{
		FileInputStream inputFile = new FileInputStream(projectPath + "/data.properties");
		data.load(inputFile);
		bugNo = Integer.parseInt(data.getProperty("bug_no"));
		System.out.println("");
		System.out.println("BeforeSuite -\t\tbug no. = " + bugNo);
		
		RestAssured.baseURI = "http://localhost:8080";
	}
	
	@Test(priority=1)
	public void getJSessionId() throws IOException
	{
		String jSessionBody = SessionBody.getSessionBody();
		
		String jSessionResponse =
		// using relaxedHTTPSValidation() means issues with SSL certificates, etc. will be ignored
		given().relaxedHTTPSValidation().log().all().header("Content-Type", "application/json").body(jSessionBody)
		.filter(session).when().post("/rest/auth/1/session")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("");
		System.out.println("JSession Response:\t" + jSessionResponse);
		System.out.println("");
		
		JsonPath jsonJSessionResponse = new JsonPath(jSessionResponse);
		jSessionId = jsonJSessionResponse.getString("session.value");
		System.out.println("JSessionID:\t\t" + jSessionId);
		System.out.println("");
	}
	
	@Test(priority=2)
	public void createBug()
	{
		String bugBody = BugBody.getBugBody(bugNo);
		
		String bugResponse =
		// Can either use cookies in header or simply use .filter(session) (both given below)
		given().log().all().header("Content-Type", "application/json").header("Cookie", "JSESSIONID=" + jSessionId).body(bugBody)
		.filter(session).when().post("/rest/api/2/issue")
		.then().assertThat().statusCode(201).extract().response().asString();
		
		System.out.println("Bug Response:\t" + bugResponse);
		System.out.println("");
		
		JsonPath jsonBugResponse = new JsonPath(bugResponse);
		bugId = jsonBugResponse.getString("id");
		System.out.println("Bug ID:\t\t" + bugId);
		System.out.println("");
	}
	
	@Test(priority=3, dataProvider="getParams")
	public void addBugComment(int commentNo)
	{
		String commentBody = CommentBody.getCommentBody(commentNo);
		
		String commentResponse =
		// Note the use of .pathParam() so it can be used as a path parameter within the resource, e.g. {bug}
		// Path parameters are used to drill down to a more specific resource, e.g. to post a comment for a particular bug only
		// Query parameters are used to filter, sort and drill down within a particular resource
		given().pathParam("bugId", bugId).log().all().header("Content-Type", "application/json").body(commentBody)
		.filter(session).when().post("/rest/api/2/issue/{bugId}/comment")
		.then().assertThat().statusCode(201).extract().response().asString();
		
		System.out.println("Comment Response:\t" + commentResponse);
		System.out.println("");
		
		JsonPath jsonCommentResponse = new JsonPath(commentResponse);
		commentIds.add(jsonCommentResponse.getString("id"));
		System.out.println("Comment ID:\t\t" + commentIds.get(commentNo));
		System.out.println("");
	}
	
	@Test(priority=4)
	public void addBugAttachment()
	{
		String attachmentResponse =
		given().pathParam("bugId", bugId).log().all().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("jira_attachment.txt"))
		.filter(session).when().post("/rest/api/2/issue/{bugId}/attachments")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("Attachment Response:\t" + attachmentResponse);
		System.out.println("");
	}
	
	@Test(priority=5)
	public void getBugDetails()
	{
		String bugDetailsResponse =
		given().pathParam("bugId", bugId).log().all().queryParam("fields", "comment")
		.filter(session).when().get("/rest/api/2/issue/{bugId}")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		//System.out.println("Bug Details Response:\t" + bugDetailsResponse);
		//System.out.println("");
		
		JsonPath jsonDetailsResponse = new JsonPath(bugDetailsResponse);
		String detailCommentId = "";
		String detailComment = "";
		
		for (int i=0; i<commentIds.size(); i++)
		{
			// Retrieve complex Json formats within an array
			detailCommentId = jsonDetailsResponse.getString("fields.comment.comments[" + i + "].id");
			// The following retrieves the value of the 'body' node, which is within the 'comments' array,
			// which is within the 'comment' node, which is within the 'fields' node
			detailComment = jsonDetailsResponse.getString("fields.comment.comments[" + i + "].body");
			
			if (detailCommentId.equals(commentIds.get(i)))
			{
				System.out.println("Comment " + (i+1) + " was \"" + detailComment + "\"");
				System.out.println("");
			}
			else
			{
				// Rest assured asserts can only be added after the 'then()' method
				// Out with the 'then()' method, asserts can only be added via tools and frameworks like TestNG
				Assert.assertTrue(false, "Unexpected comment ID found");
			}
		}
	}
	
	@AfterSuite
	public static void incrementBugNo() throws IOException
	{
		bugNo++;
		data.setProperty("bug_no", String.valueOf(bugNo));
		FileOutputStream outputFile = new FileOutputStream(projectPath + "/data.properties");
		data.store(outputFile, "Value of bug_no property updated");
		System.out.println("AfterSuite -\tBug No. = " + bugNo);
	}
	
	@DataProvider
	public Object[][] getParams()
	{
		int noOfTests = 5;
		Object[][] params = new Object[noOfTests][1];
		
		for(int i=0; i<noOfTests; i++)
		{
			params[i][0] = i;
		}
		
		return params;
	}
}
