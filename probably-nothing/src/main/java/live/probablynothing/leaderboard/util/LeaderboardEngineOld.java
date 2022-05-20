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
import live.probablynothing.leaderboard.model.CumulativeContestData;
import live.probablynothing.leaderboard.model.bitquery.dto.DexTradeDTO;
import live.probablynothing.leaderboard.model.bitquery.dto.DexTradeDTO.DexTrade;
import live.probablynothing.leaderboard.repository.ContestDataRepository;
import live.probablynothing.leaderboard.repository.CumulativeContestDataRepository;

@Component
public class LeaderboardEngineOld {

	@Autowired
	BitqueryClient bitqueryClient;

	@Autowired
	CumulativeContestDataRepository CumulativeContestDataRepository;

	private static final String SELL = "SELL";	
	
	int numberOfTradesFound ;

	public void process(DexTradeDTO dto, ContestHeader contestHeader, String startDate, String endDate) throws IOException {

		// query bitquery API to get the latest trades
		//DexTradeDTO dto = bitqueryClient.getDexTrades(contestHeader, startDate, endDate);

		Map<String, Integer> addressIndex = new HashMap<String, Integer>();
		List<CumulativeContestData> contestsData = new ArrayList<CumulativeContestData>();
		try {
			if(numberOfTradesFound == dto.getData().getEthereum().getDexTrades().size())
			{
				System.out.println("No new transactions found..");
				return;
			}
		    numberOfTradesFound = dto.getData().getEthereum().getDexTrades().size();
			for (DexTrade trade : dto.getData().getEthereum().getDexTrades()) {
				String walletAddress = trade.getTransaction().getTxFrom().getAddress();
				CumulativeContestData contestData;
				if (addressIndex.containsKey(walletAddress)) {
					int index = addressIndex.get(walletAddress);
					contestData = contestsData.get(index);
					if (trade.getSide().equals(SELL))
					{
						contestData.setPurchaseValueInETH(contestData.getPurchaseValueInETH() + trade.getQuoteAmount());
						contestData.setPurchaseValueInUSD(contestData.getPurchaseValueInUSD() + trade.getTradeAmount());
						contestData.setTokenAmount(contestData.getTokenAmount() + trade.getBaseAmount());
					}
					else {
						contestData.setPurchaseValueInETH(contestData.getPurchaseValueInETH() - trade.getQuoteAmount());
						contestData.setPurchaseValueInUSD(contestData.getPurchaseValueInUSD() - trade.getTradeAmount());
						contestData.setTokenAmount(contestData.getTokenAmount() - trade.getBaseAmount());
						
					}

				} else {
					contestData = new CumulativeContestData();
					contestData.setAddress(walletAddress);
					contestData.setContestHeader(contestHeader);
					if (trade.getSide().equals(SELL))
					{
						contestData.setPurchaseValueInETH(trade.getQuoteAmount());
					    contestData.setPurchaseValueInUSD(trade.getTradeAmount());
					    contestData.setTokenAmount(trade.getBaseAmount());
					}
					else {
						contestData.setPurchaseValueInETH(-trade.getQuoteAmount());
					    contestData.setPurchaseValueInUSD(-trade.getTradeAmount());
					    contestData.setTokenAmount(-trade.getBaseAmount());						
					}
					contestsData.add(contestData);
					int index = contestsData.lastIndexOf(contestData);
					addressIndex.put(walletAddress, index);
				}
			}
			if(!contestsData.isEmpty())
			{
				CumulativeContestDataRepository.deleteAll();
				CumulativeContestDataRepository.saveAll(contestsData);
			}
			
			System.out.println("Leaderboard updated...");

		} catch (NullPointerException exc) {
			// TODO: handle exception
		}

	}
	

}
