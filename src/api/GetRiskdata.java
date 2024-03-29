package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.String;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

@Path("/getriskdata")
public class GetRiskdata 
{
	public static ArrayList<ArrayList<Double>> rdList = new ArrayList<ArrayList<Double>>();
	//public static String fileDir = System.getProperty("user.dir") + "\\";
	//public static String fileDir = "/data/";
	public static String whiteListFN = "whitelist.txt";
    public static boolean firstRun = true;
	
	/** Runs the pullRiskDataJSON() method for the ID given in the URL
	 * 
	 * @param tempID - id given in URL
	 * @return String from pullRiskDataJSON() - a JSON format string containing the risk data of a customer
	 * @throws Exception
	 */
    @GET
	@Path("{apiKey}/{tempRdID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRiskDataJSON(@PathParam("apiKey") int apiKey, @PathParam("tempRdID") int tempRdID) throws Exception
	{
        if (checkKey(whiteListFN, apiKey))
        {
        	String riskData = pullRiskDataJSON(tempRdID);
        	return riskData;
        }
        else
        	return "Invalid API Key";
	}
	
	/** Makes a call to an API and converts the response line by line as an array of doubles
	 * 
	 * @param urlToRead - the URL that the method will attempt to connect to
	 * @throws Exception
	 */
	public static void getHTML(String urlToRead, double iterID) throws Exception 
    {
        ArrayList<Double> dubList = new ArrayList<Double>();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null)
        {
            dubList.add(Double.parseDouble(line));
        }
        dubList.add(0, iterID);
        rdList.add(dubList);
        //System.out.println("Ran getHTML with ID: " + iterID + "!");
        iterID++;
        rd.close();
     }

    /** Calls the random.org API and creates an array of doubles and gives adds an id for each customer
     *  
     * @param id - the id of the customer whose risk data is wanted
     * @return jsonStr - a JSON format string containing the risk data of a customer
     * @throws Exception
     */
	public String pullRiskDataJSON(int id) throws Exception
     {
        if (firstRun)
        {
        	for (int i = 1; i <= 5; i++)
        	{
        		getHTML("https://www.random.org/integers/?num=9&min=10000&max=99999&col=1&base=10&format=plain&rnd=new", i);
        		TimeUnit.SECONDS.sleep(1);
        	}
        	firstRun = false;
        }
        
        String jsonStr = "";
        String appStatus = "";
        
        for (int i = 0; i < rdList.size(); i++)
        {
            if (id == (rdList.get(i).get(0).intValue()))
            {
            	double debtOverIncome = rdList.get(i).get(3);
            	if (debtOverIncome < 40000)
            		appStatus = "poor";
            	else if (debtOverIncome > 60000)
            		appStatus = "great";
            	else
            		appStatus = "fair";
            	
                jsonStr = "[\n{\n\t\"id\": \"" + rdList.get(i).get(0).intValue() + 
                        "\", \n\t\"caseNum\": \"" + rdList.get(i).get(1).intValue() +
                        "\", \n\t\"existingCcRecord\": \"" + appStatus +
                        "\", \n\t\"debtOverIncome\": \"" + debtOverIncome + 
                        "\", \n\t\"paymentOverIncome\": \"" + rdList.get(i).get(4) + 
                        "\", \n\t\"probabilityToDefault\": \"" + rdList.get(i).get(5) +
                        "\", \n\t\"riskFactor\": \"" + rdList.get(i).get(6) + 
                        "\"\n}\n]";
                
                return jsonStr;
            }
            else
            {
            	jsonStr =  "No customer exists with an ID of " + id + ".";
            }
        }

        return jsonStr;
    }
	
	public File getFile(String fileName)
	{
		ClassLoader classLoader = new GetRiskdata().getClass().getClassLoader();
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