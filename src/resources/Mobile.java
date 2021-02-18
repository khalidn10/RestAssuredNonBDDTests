// Associated Tests (Deserialisation):
// tests.GetCoursesPojoTest
// resources.GetCoursesPojo
// resources.WebAutomation
// resources.Api
// resources.Courses

package resources;

public class Mobile 
{
	private String courseTitle;
	private int price;
	
	public String getCourseTitle() 
	{
		return courseTitle;
	}
	
	public void setCourseTitle(String courseTitle) 
	{
		this.courseTitle = courseTitle;
	}
	
	public int getPrice() 
	{
		return price;
	}
	
	public void setPrice(int price) 
	{
		this.price = price;
	}
}