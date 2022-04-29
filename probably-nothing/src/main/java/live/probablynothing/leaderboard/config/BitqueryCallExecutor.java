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
//@Service
public class BitqueryCallExecutor {

	// Initial Logic
	/*
	 * 1. Read the Active Contest 2. Get the start and End date 3. convert start and
	 * end date to ISO8601 with offset of PST time zone 4. Call the Bitquery by
	 * passing start and end 5. Calculate the leaderboard and save to the
	 * ContestData
	 */	
	

	@Autowired
	ContestHeaderRepository repo;
	
	@Autowired
	LeaderboardEngine engine;

	@Scheduled(fixedDelay = 30000)
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
