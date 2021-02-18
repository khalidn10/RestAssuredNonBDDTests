// Associated Tests (Cookies based Authentication in Jira):
// tests.CreateJiraIssue
// resources.BugBody
// resources.SessionBody

package resources;

public class CommentBody {

	public static String getCommentBody(int commentNo)
	{
		String commentBody = 	"{\r\n" + 
								"    \"body\": \"This is comment " + (commentNo+1) + "\",\r\n" + 
								"    \"visibility\": {\r\n" + 
								"        \"type\": \"role\",\r\n" + 
								"        \"value\": \"Administrators\"\r\n" + 
								"    }\r\n" + 
								"}";
		return commentBody;
	}
}
