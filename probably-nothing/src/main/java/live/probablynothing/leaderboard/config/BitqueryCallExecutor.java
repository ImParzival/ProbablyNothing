package live.probablynothing.leaderboard.config;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import live.probablynothing.leaderboard.model.ContestHeader;
import live.probablynothing.leaderboard.repository.ContestHeaderRepository;
import live.probablynothing.leaderboard.util.DateTimeUtil;
import live.probablynothing.leaderboard.util.LeaderboardEngine;

@Configuration
@EnableScheduling
public class BitqueryCallExecutor {	

	@Autowired
	ContestHeaderRepository repo;
	
	@Autowired
	LeaderboardEngine engine;

	@Scheduled(fixedDelay = 600000)
	//@Scheduled(fixedDelay = 30000)
	public void getLatestDexTrades() throws ParseException, IOException {

		// 1. Read the active contest
		List<ContestHeader> activeContests = repo.findByIsActive(true);

		if(!activeContests.isEmpty()) {
			ContestHeader contestHeader = activeContests.get(0);
			String startDate = DateTimeUtil.getISO8601DateTimeInPST(contestHeader.getStartDate());
			String endDate = DateTimeUtil.getISO8601DateTimeInPSTEnd(contestHeader.getEndDate());
			
			engine.process(contestHeader, startDate, endDate);
		}
		else
			System.out.println("No active contest found");
		  

	}

}
