package live.probablynothing.leaderboard.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import live.probablynothing.leaderboard.model.TokenInfo;
import live.probablynothing.leaderboard.service.TokenInfoService;

@RestController
public class TokenInfoController {

	@Autowired
	TokenInfoService tokenInfoService;
	
	@GetMapping("/tokenInfo")
	public TokenInfo getTokenInfo(@RequestParam("tokenContract") String tokenContract, @RequestParam("includeTokenHolderCount") boolean includeTokenHolderCount) throws IOException
	{
		return tokenInfoService.getTokenInfo(tokenContract, includeTokenHolderCount);
	}
}
