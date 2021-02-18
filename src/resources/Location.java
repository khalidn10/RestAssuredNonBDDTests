// Associated Tests (Spec Builder):
// tests.AddPlaceAPIPojoSpecBuilderTest
// resources.AddPlaceAPIPojo

// Associated Tests (Serialisation):
// tests.AddPlaceAPIPojoTest
// resources.AddPlaceAPIPojo

package resources;

public class Location 
{
	private double lat; 
	private double lng;
	
	public double getLat() 
	{
		return lat;
	}
	
	public void setLat(double lat) 
	{
		this.lat = lat;
	}
	
	public double getLng() 
	{
		return lng;
	}
	
	public void setLng(double lng) 
	{
		this.lng = lng;
	}
}
