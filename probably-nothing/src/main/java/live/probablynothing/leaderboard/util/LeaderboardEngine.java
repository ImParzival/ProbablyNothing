package live.probablynothing.leaderboard.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	@Autowired
	LeaderboardEngineOld oldEngine;
	

	private static final String SELL = "SELL";

	int numberOfTradesFound;

	/**
	 * Finds Dex trades between start and end date by querying Bitquery API. If new
	 * trades are found, updates the ContestData.
	 * 
	 * @param contestHeader
	 * @param startDate
	 * @param endDate
	 * @throws IOException
	 */
	public void process(ContestHeader contestHeader, String startDate, String endDate) throws IOException {

		// Call bitquery API to get the latest trades
		DexTradeDTO dto = bitqueryClient.getDexTrades(contestHeader, startDate, endDate);
		
		processCumulativeContestData(dto,contestHeader, startDate, endDate);

		if (numberOfTradesFound == dto.getData().getEthereum().getDexTrades().size()) {
			System.out.println("No new transactions found..");
			return;
		}
		numberOfTradesFound = dto.getData().getEthereum().getDexTrades().size();

		List<ContestData> contestsData = new ArrayList<ContestData>();
		for (DexTrade trade : dto.getData().getEthereum().getDexTrades()) {

			// the bitquery considers it from the DEX perspective. So if person A buys a
			// token, the dex treats it as sale of the token. "SELL" side means token buy.
			if (trade.getSide().equals(SELL)) {
				ContestData contestData = new ContestData();
				contestData.setAddress(trade.getTransaction().getTxFrom().getAddress());
				contestData.setTxHash(trade.getTransaction().getHash());
				contestData.setContestHeader(contestHeader);
				contestData.setPurchaseValueInETH(trade.getQuoteAmount());
				contestData.setPurchaseValueInUSD(trade.getTradeAmount());
				contestData.setTokenAmount(trade.getBaseAmount());
				contestsData.add(contestData);
			}

		}
		
		if(!contestsData.isEmpty())
		{
			contestDataRepository.deleteAll();
			contestDataRepository.saveAll(contestsData);
		}
	}
	
	private void processCumulativeContestData(DexTradeDTO dto, ContestHeader contestHeader, String startDate, String endDate) throws IOException {
		
		oldEngine.process(dto, contestHeader, startDate, endDate);
	}

}
