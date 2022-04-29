package live.probablynothing.leaderboard.model.bitquery.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class DexTradeDTO {
	
	private Data data;
	
	@Getter
	public static class Data {
		
		private Ethereum ethereum;
		
	}
	
	
	@Getter
	public static class Ethereum {

		private List<DexTrade> dexTrades;
		
	}
	
	@Getter
	public static class DexTrade {

		private BaseCurrency baseCurrency;
		private double baseAmount;
		private QuoteCurrency quoteCurrency;
		private double quoteAmount;
		public int trades;
		private double quotePrice;
		private Transaction transaction;				
		private String side;
		
	}
	
	
	
	@Getter
	public static class Transaction {
		private String hash;		
		private TxFrom txFrom;
	}
	
	@Getter
	public static class TxFrom{
		private String address;
	}
	
	@Getter
	public static class BaseCurrency {
		private String symbol;
		private String address;
	}

	@Getter
	public static class QuoteCurrency {
		private String symbol;
		private String address;
	}

}
