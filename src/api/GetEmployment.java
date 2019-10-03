package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Scanner;

@Path("/getemployment")
public class GetEmployment
{
	public static ArrayList<String[]> empList = new ArrayList<String[]>();
	
	/** Retrieves employment data from a .txt file and returns it as a JSON String
	 * 
	 * @param tempAppID - the id of the customer you want the employment data of
	 * @return - a JSON sting containing a customer's employment information
	 * @throws Exception
	 */
	@GET
	@Path("{tempAppID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuoteJSON(@PathParam("tempAppID") int tempAppID) throws Exception
	{
		pullData(System.getProperty("user.dir") + "\\emp_data.txt");
		String jsonStr = "";
		for (int i = 0; i < empList.size(); i++)
        {
            if (tempAppID == Integer.parseInt((empList.get(i)[0])))
            {
            	jsonStr = "[\n{\n\t\"id\": \"" + empList.get(i)[0] +  
                		"\", \n\t\"employer\": \"" + empList.get(i)[1] + 
                        "\", \n\t\"role\": \"" + empList.get(i)[2] + 
                        "\", \n\t\"duration\": \"" + empList.get(i)[3] +
                        "\", \n\t\"ctc\": \"" + empList.get(i)[4] +
                        "\", \n\t\"contact\": \"" + empList.get(i)[5] +
                        "\"\n}\n]";
            	return jsonStr;
            }
            else
            	jsonStr = "No customer exists with an ID of " + tempAppID + ".";
        }
		return jsonStr;
	}
	
	public static void pullData(String fileName) throws IOException
    {
        File file = new File(fileName);
        Scanner scan = new Scanner(file);  
        
        while (scan.hasNext())
        {
            String line = scan.nextLine();
            String[] input = line.split(",");
            empList.add(input);
        }
        scan.close();
    }
}