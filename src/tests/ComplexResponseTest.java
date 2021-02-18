// Associated Tests (Complex Json in Responses):
// resources.MockComplexResponse

package tests;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import resources.MockComplexResponse;

public class ComplexResponseTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// If the developer is still working on the code you need to test
		// then it's often useful to create a 'mock' response Json (within a separate class)
		// that matches the expected Json format of the response message
		// so that you can use that Json as a stub for your tests
		JsonPath jsonComplex = new JsonPath(MockComplexResponse.getCourseDetails());
		
		// 1. Print No of courses returned by API
		// Note that values from Json files can be retrieved as different datatypes such as String, int, float and even object types
		int noOfCourses = jsonComplex.getInt("courses.size()");
		System.out.println("No. of Courses:\t\t" + noOfCourses);
		
		// 2. Print Purchase Amount
		int purchaseAmount = jsonComplex.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount:\t" + purchaseAmount);
		
		// 3. Print Title of the first course
		String firstCourseTitle = jsonComplex.getString("courses[0].title");
		System.out.println("First Course Title:\t" + firstCourseTitle);
		
		int totalPrice = 0;
		
		// 4. Print All course titles and their respective Prices
		// Arrays within Json files can be used in the same way as Java
		for(int i=0; i<noOfCourses; i++)
		{
			int price = jsonComplex.getInt("courses[" + i + "].price");
			int copies = jsonComplex.getInt("courses[" + i + "].copies");
			totalPrice = totalPrice + price*copies;
			
			System.out.println("Course " + (i+1) + " Title:\t\t" + jsonComplex.getString("courses[" + i + "].title"));
			System.out.println("Course " + (i+1) + " Price:\t\t" + price);
			
			// 5. Print no of copies sold by RPA Course
			if (jsonComplex.getString("courses[" + i + "].title").equalsIgnoreCase("RPA"))
			{
				System.out.println("No. of Course " + (i+1) + " Copies:\t" + copies);
			}
		}
		
		// 6. Verify if Sum of all Course prices matches with Purchase Amount
		if (purchaseAmount == totalPrice)
		{
			System.out.println("Calculated Total Purchase Amount is " + totalPrice + " which matches Purchase Amount");
		}
		else
		{
			System.out.println("Calculated Total Purchase Amount is " + totalPrice + " which doesn't match Purchase Amount");
		}
		Assert.assertEquals(purchaseAmount, totalPrice);
	}

}
