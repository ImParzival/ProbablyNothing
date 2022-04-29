package live.probablynothing.leaderboard.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import live.probablynothing.leaderboard.model.bitquery.dto.DexTradeDTO;
import live.probablynothing.leaderboard.model.bitquery.dto.GraphqlRequestBody;
import live.probablynothing.leaderboard.util.GraphqlSchemaReaderUtil;

@Component
@PropertySource("classpath:application-dev.properties")
public class BitqueryClient {

	private final String url;
	private final String apiKey;

	private static final String EXCHANGE_NAME = "Uniswap";
	private static final String PN_TOKEN_CONTRACT = "0x61b5c3aeE3a25f6f83531D548A4d2EE58450f5D9";
	private static final String WETH_TOKEN = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";

	public BitqueryClient(@Value("${bitqueryEndpoint}") String url, @Value("${apiKey}") String apiKey) {
		this.url = url;
		this.apiKey = apiKey;
	}

	public DexTradeDTO getDexTrades(String startDate, String endDate) throws IOException {
		WebClient webClient = WebClient.builder().build();

		GraphqlRequestBody graphqlRequestBody = new GraphqlRequestBody();
		final String query = GraphqlSchemaReaderUtil.getSchemaFromFileName("DexTrades");
		final String variables = GraphqlSchemaReaderUtil.getSchemaFromFileName("variables");

		graphqlRequestBody.setQuery(query);
		graphqlRequestBody.setVariables(variables.replace("exchangeName", EXCHANGE_NAME)
				.replace("baseCurrency", PN_TOKEN_CONTRACT).replace("quoteCurrency", WETH_TOKEN)
				.replace("startDateTime", startDate).replace("endDateTime", endDate));

		return webClient.post().uri(url).header("X-API-KEY", apiKey).bodyValue(graphqlRequestBody).retrieve()
				.bodyToMono(DexTradeDTO.class).block();

	}

}
