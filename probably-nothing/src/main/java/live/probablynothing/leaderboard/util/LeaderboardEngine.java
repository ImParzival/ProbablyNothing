package live.probablynothing.leaderboard.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import live.probablynothing.leaderboard.client.BitqueryClient;
import live.probablynothing.leaderboard.model.ContestData;
import live.probablynothing.leaderboard.model.ContestHeader;
import live.probablynothing.leaderboard.model.bitquery.dto.DexTradeDTO;
import live.probablynothing.leaderboard.model.bitquery.dto.DexTradeDTO.DexTrade;
import live.probablynothing.leaderboard.repository.ContestDataRepository;

@Component
public class LeaderboardEngine {

	@Autowired
	BitqueryClient bitqueryClient;

	@Autowired
	ContestDataRepository contestDataRepository;

	private static final String BUY = "BUY";	
	
	int numberOfTradesFound ;

	public void process(ContestHeader contestHeader, String startDate, String endDate) throws IOException {

		// query bitquery API to get the latest trades
		DexTradeDTO dto = bitqueryClient.getDexTrades(startDate, endDate);

		Map<String, Integer> addressIndex = new HashMap<String, Integer>();
		List<ContestData> contestsData = new ArrayList<ContestData>();
		try {
			if(numberOfTradesFound == dto.getData().getEthereum().getDexTrades().size())
			{
				System.out.println("No new transactions found..");
				return;
			}
		    numberOfTradesFound = dto.getData().getEthereum().getDexTrades().size();
			for (DexTrade trade : dto.getData().getEthereum().getDexTrades()) {
				String walletAddress = trade.getTransaction().getTxFrom().getAddress();
				ContestData contestData;
				if (addressIndex.containsKey(walletAddress)) {
					int index = addressIndex.get(walletAddress);
					contestData = contestsData.get(index);
					if (trade.getSide().equals(BUY))
						contestData.setPurchaseAmount(contestData.getPurchaseAmount() + trade.getBaseAmount());
					else {
						contestData.setPurchaseAmount(contestData.getPurchaseAmount() - trade.getBaseAmount());
					}

				} else {
					contestData = new ContestData();
					contestData.setAddress(walletAddress);
					contestData.setContestHeader(contestHeader);
					if (trade.getSide().equals(BUY))
						contestData.setPurchaseAmount(trade.getBaseAmount());
					else {
						contestData.setPurchaseAmount(-trade.getBaseAmount());
					}
					contestsData.add(contestData);
					int index = contestsData.lastIndexOf(contestData);
					addressIndex.put(walletAddress, index);
				}
			}
			if(!contestsData.isEmpty())
			{
				contestDataRepository.deleteAll();
				contestDataRepository.saveAll(contestsData);
			}
			
			System.out.println("Leaderboard updated...");

		} catch (NullPointerException exc) {
			// TODO: handle exception
		}

	}
	

}
