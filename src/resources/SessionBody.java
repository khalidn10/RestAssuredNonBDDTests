// Associated Tests (Cookies based Authentication in Jira):
// tests.CreateJiraIssue
// resources.BugBody
// resources.CommentBody

package resources;

public class SessionBody {

	public static String getSessionBody()
	{
		String sessionBody = 	"{\r\n" + 
								"\"username\": \"*********\",\r\n" + 
								"\"password\": \"*********\"\r\n" + 
								"}";
		return sessionBody;
	}
}
