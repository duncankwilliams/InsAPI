package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.ApplicationPath;
//import javax.ws.rs.core.Application;
import java.lang.String;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Path("/getcustomer")
public class GetCustomer 
{
	public static ArrayList<String[]> custList = new ArrayList<String[]>();
	
	/** Calls a method to pull data from a text file and return it as a JSON String
	 * 
	 * @param tempID - id given in URL
	 * @return String in JSON format of customer information for customer with specified id
	 * @throws IOException
	 */
	@GET
	@Path("{tempCustID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCustomerJSON(@PathParam("tempCustID") int tempCustID) throws IOException
	{
		pullData(System.getProperty("user.dir") + "\\user_data.txt");
		String strCustomJSON = (getCustomer(tempCustID));
		return strCustomJSON;
	}
	
	/** Converts a txt file into an arraylist of arrays, on array for each customer
	 * 
	 * @param fileName - file to be read and converted into an array
	 * @throws IOException
	 */
	public static void pullData(String fileName) throws IOException
    {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);  
        
        while (scan.hasNext())
        {
            String line = scan.nextLine();
            String[] input = line.split(",");
            custList.add(input);
        }
        scan.close();
    }
	
	/** Searches the ArrayList for the customer with the given ID, then returns their info in a JSON String
	 * 
	 * @param id - id of the customer to look for in the array
	 * @return a JSON String with the id, name, and address of the customer
	 */
	public static String getCustomer(int id)
    {
		String jsonStr = "";
        
        for (int i = 0; i < custList.size(); i++)
        {
            if (Integer.toString(id).equals(custList.get(i)[0]))
            {
                jsonStr = "[\n{\n\t\"id\": \"" + custList.get(i)[0] +  
                		"\", \n\t\"firstName\": \"" + custList.get(i)[1] + 
                        "\", \n\t\"lastName\": \"" + custList.get(i)[2] + 
                        "\", \n\t\"guardianName\": \"" + custList.get(i)[3] +
                        "\", \n\t\"dob\": \"" + custList.get(i)[4] +
                        "\", \n\t\"address\": \"" + custList.get(i)[5] + " " + custList.get(i)[6] + " " + custList.get(i)[7] + " " + custList.get(i)[8] + 
                        "\", \n\t\"contact\": \"" + custList.get(i)[9] +
                        "\"\n}\n]";
                return jsonStr;
            }
            else
            	jsonStr =  "No customer exists with an ID of " + id + ".";
        }
        return jsonStr;
    }
}