// Associated Tests (Cookies based Authentication in Jira):
// tests.CreateJiraIssue
// resources.CommentBody
// resources.SessionBody

package resources;

public class BugBody {

	public static String getBugBody(int bugNo)
	{
		String bugBody =	"{\r\n" + 
							"    \"fields\": \r\n" + 
							"    {\r\n" + 
							"        \"project\": \r\n" + 
							"	    {\r\n" + 
							"            \"key\": \"MYP\"\r\n" + 
							"        },\r\n" + 
							"        \"summary\": \"Bug " + bugNo + " created through Jira API\",\r\n" + 
							"        \"issuetype\": \r\n" + 
							"	    {\r\n" + 
							"            \"name\": \"Bug\"\r\n" + 
							"        }\r\n" + 
							"    }\r\n" + 
							"}";
		return bugBody;
	}
}
