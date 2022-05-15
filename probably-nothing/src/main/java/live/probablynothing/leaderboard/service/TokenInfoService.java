package live.probablynothing.leaderboard.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import live.probablynothing.leaderboard.client.EtherscanClient;
import live.probablynothing.leaderboard.model.TokenInfo;
import live.probablynothing.leaderboard.repository.TokenInfoRepository;

@Service
public class TokenInfoService {

	@Autowired
	EtherscanClient etherscanClient;

	@Autowired
	TokenInfoRepository tokenInfoRepo;

	public TokenInfo getTokenInfo(String tokenContract, boolean includeTokenHolderCount) throws IOException {
		if (includeTokenHolderCount) {
			return tokenInfoRepo.findByTokenContract(tokenContract);
		}
		return null;
	}

	public TokenInfo saveTokenInfo(String tokenContract) throws IOException {
		int numberOfTokenHolders = etherscanClient.getNumberOfTokenHolders(tokenContract);

		TokenInfo tokenInfo = TokenInfo.builder().tokenContract(tokenContract).totalTokenHolders(numberOfTokenHolders)
				.build();

		return tokenInfoRepo.save(tokenInfo);
	}

}
