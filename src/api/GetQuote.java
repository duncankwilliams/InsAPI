package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.String;

@Path("/getquote")
public class GetQuote extends GetRiskdata
{
	/** Fetches the data for a quote and presents it as a JSON String
	 * 
	 * @param tempQuoteID - id provided by user
	 * @return a JSON String containing the id and quote for the customer with the id given in URL
	 * @throws Exception
	 */
	@GET
	@Path("/quote/{tempQuoteID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuoteJSON(@PathParam("tempQuoteID") int tempQuoteID) throws Exception
	{
		if (firstRun)
			pullRiskDataJSON(1);
		
		String quoteJsonStr = "";
		for (int i = 0; i < rdList.size(); i++)
        {
            if (tempQuoteID == (rdList.get(i).get(0).intValue()))
            {
            	quoteJsonStr = "[\n{\n\t\"id\": \"" + rdList.get(i).get(0).intValue() + "\",\n\t\"quote\": \"$" + (rdList.get(i).get(2)) / 100 + 
            			"\", \n\t\"uwPointNumer\": \"" + (rdList.get(i).get(7).intValue() / 10000) +
                        "\", \n\t\"uwPointDenom\": \"" + ((rdList.get(i).get(8).intValue() / 10000) + 10) +
                        "\", \n\t\"ficoScore\": \"" + (rdList.get(i).get(9).intValue() / 100)  + "\"\n}\n]";
            	return quoteJsonStr;
            }
            else
            	quoteJsonStr = "No customer exists with an ID of " + tempQuoteID + ".";
        }
		return quoteJsonStr;
	}
}