package live.probablynothing.leaderboard.client;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class EtherscanClient {

	private static final String ETHERSCAN_URL = "https://etherscan.io/token/";
	
	
	public int getNumberOfTokenHolders(String tokenContract) throws IOException
	{
		String url = prepareUrl(tokenContract);
		
		Document doc = Jsoup.connect(url).get();
		
		String[] str = doc.select("div[class=mr-3]").text().split("\\s");
		
		return Integer.parseInt(str[0]);
		
	}
	
	private String prepareUrl(String tokenContract)
	{
		//TODO: validate token contract
		
		return ETHERSCAN_URL+"/".concat(tokenContract);	
		
	}		
}
