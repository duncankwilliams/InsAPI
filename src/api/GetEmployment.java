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
	//public static String fileDir = System.getProperty("user.dir") + "\\";
	public static String whiteListFN = "whitelist.txt";
	public static String empDataFN = "emp_data.txt";
	
	/** Retrieves employment data from a .txt file and returns it as a JSON String
	 * 
	 * @param tempAppID - the id of the customer you want the employment data of
	 * @return - a JSON sting containing a customer's employment information
	 * @throws Exception
	 */
	@GET
	@Path("{apiKey}/{tempAppID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuoteJSON(@PathParam("apiKey") int apiKey, @PathParam("tempAppID") int tempAppID) throws Exception
	{
		if (checkKey(whiteListFN, apiKey))
		{
			pullData(empDataFN);
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
		else
			return "Invalid API Key";
	}
	
	public static void pullData(String fileName) throws IOException
    {
		ClassLoader classLoader = new GetEmployment().getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        Scanner scan = new Scanner(file);  
        
        while (scan.hasNext())
        {
            String line = scan.nextLine();
            String[] input = line.split(",");
            empList.add(input);
        }
        scan.close();
    }
	
	public File getFile(String fileName)
	{
		ClassLoader classLoader = new GetEmployment().getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        System.out.println(fileName + "File Found : " + file.exists());
		return file;
	}
	
	public boolean checkKey(String fileName, int key) throws IOException
    {
		File retrievedFile = getFile(fileName);
        Scanner input = new Scanner(retrievedFile);
        boolean valid = false;
        while (input.hasNext())
        {
            if (input.nextLine().equals(Integer.toString(key)))
            {
                valid = true;
                input.close();
                return valid;
            }
        }
        input.close();
        return valid;
    }
}