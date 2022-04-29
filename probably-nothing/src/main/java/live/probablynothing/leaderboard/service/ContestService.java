package live.probablynothing.leaderboard.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import live.probablynothing.leaderboard.model.ContestData;
import live.probablynothing.leaderboard.model.ContestHeader;
import live.probablynothing.leaderboard.model.ContestType;
import live.probablynothing.leaderboard.repository.ContestDataRepository;
import live.probablynothing.leaderboard.repository.ContestHeaderRepository;

@Service
public class ContestService {

	@Autowired
	ContestHeaderRepository contestHeaderRepository;

	@Autowired
	ContestDataRepository contestDataRepository;

	public ContestHeader createContestHeader(String name, String startDate, String endDate, boolean isActive) {

		ContestHeader contestHeader = ContestHeader.builder().name(name).type(ContestType.STANDARD).startDate(startDate)
				.endDate(endDate).isActive(isActive).build();
		return contestHeaderRepository.save(contestHeader);
	}

	public List<ContestHeader> getAllContestHeaders() {
		return contestHeaderRepository.findAll();
	}

	public List<ContestHeader> getActiveContests() {
		return contestHeaderRepository.findByIsActive(true);
	}

	public Optional<ContestHeader> getContest(String id) {

		return contestHeaderRepository.findById(id);
	}

	public void deleteContest(String id) {
		contestHeaderRepository.deleteById(id);
	}

	public String activateContest(String id) {
		List<ContestHeader> contests = contestHeaderRepository.findByIsActive(true);
		if (!contests.isEmpty()) {
			return "Already an active contest exists";
		} else {

			try {
				Optional<ContestHeader> contestHeader = contestHeaderRepository.findById(id);
				ContestHeader contest = contestHeader.get();
				
				if(contest.isActive())
					return "contest is already active";
				contest.setActive(true);
				contestHeaderRepository.save(contest);
				return "contest is activated";

			} catch (NoSuchElementException exc) {
				return "input correct contest Id";
			} catch (IllegalArgumentException exc) {
				return "input correct contest Id";
			}

		}

	}

	public String deActivateContest(String id) {

		try {
			Optional<ContestHeader> contestHeader = contestHeaderRepository.findById(id);
			ContestHeader contest = contestHeader.get();
			if(!contest.isActive())
				return "contest is already inactive";
			
			
			contest.setActive(false);
			contestHeaderRepository.save(contest);
			return "contest is deactivated";

		} catch (NoSuchElementException exc) {
			return "input correct contest Id";
		} catch (IllegalArgumentException exc) {
			return "input correct contest Id";
		}
	}
	
	public List<ContestData> getContestDataDesc(String contestId){
		return contestDataRepository.findByContestHeaderIdOrderByPurchaseAmountDesc(contestId);
	}

}
