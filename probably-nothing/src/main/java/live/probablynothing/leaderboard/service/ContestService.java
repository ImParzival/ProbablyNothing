package live.probablynothing.leaderboard.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import live.probablynothing.leaderboard.model.ContestData;
import live.probablynothing.leaderboard.model.ContestHeader;
import live.probablynothing.leaderboard.model.ContestStatus;
import live.probablynothing.leaderboard.model.ContestType;
import live.probablynothing.leaderboard.model.CumulativeContestData;
import live.probablynothing.leaderboard.model.LeaderboardType;
import live.probablynothing.leaderboard.model.OrderBy;
import live.probablynothing.leaderboard.repository.ContestDataRepository;
import live.probablynothing.leaderboard.repository.ContestHeaderRepository;
import live.probablynothing.leaderboard.repository.CumulativeContestDataRepository;
import live.probablynothing.leaderboard.util.ContestDataMedianUtil;

@Service
public class ContestService {

	@Autowired
	ContestHeaderRepository contestHeaderRepository;

	@Autowired
	ContestDataRepository contestDataRepository;

	@Autowired
	CumulativeContestDataRepository cumulativeContestDataRepository;

	public ContestHeader createContestHeader(String name, String tokenContract, String startDate, String endDate,
			boolean isActive) {

		ContestHeader header = new ContestHeader();
		header.setName(name);
		header.setTokenContract(tokenContract);
		header.setType(ContestType.STANDARD);
		header.setStartDate(startDate);
		header.setEndDate(endDate);
		header.setActive(isActive);
		if (isActive)
			header.setStatus(ContestStatus.IN_PROGRESS);
		else
			header.setStatus(ContestStatus.NEW);

		return contestHeaderRepository.save(header);
	}

	public List<ContestHeader> getAllContestHeaders() {
		return contestHeaderRepository.findAll();
	}

	public List<ContestHeader> getActiveContests() {
		return contestHeaderRepository.findByIsActive(true);
	}

	public Optional<ContestHeader> getContest(Long id) {

		return contestHeaderRepository.findById(id);
	}

	public void deleteContest(Long id) {
		contestHeaderRepository.deleteById(id);
	}

	public String activateContest(Long id) {
		List<ContestHeader> contests = contestHeaderRepository.findByIsActive(true);
		if (!contests.isEmpty()) {
			return "Already an active contest exists";
		} else {

			try {
				Optional<ContestHeader> contestHeader = contestHeaderRepository.findById(id);
				ContestHeader contest = contestHeader.get();

				if (contest.isActive())
					return "contest is already active";

				contest.setActive(true);
				contest.setStatus(ContestStatus.IN_PROGRESS);
				contestHeaderRepository.save(contest);
				return "contest is activated";

			} catch (NoSuchElementException exc) {
				return "input correct contest Id";
			} catch (IllegalArgumentException exc) {
				return "input correct contest Id";
			}

		}

	}

	public String deActivateContest(Long id) {

		try {
			Optional<ContestHeader> contestHeader = contestHeaderRepository.findById(id);
			ContestHeader contest = contestHeader.get();
			if (!contest.isActive())
				return "contest is already inactive";

			contest.setActive(false);
			contest.setStatus(ContestStatus.HALTED);
			contestHeaderRepository.save(contest);
			return "contest is deactivated";

		} catch (NoSuchElementException exc) {
			return "input correct contest Id";
		} catch (IllegalArgumentException exc) {
			return "input correct contest Id";
		}
	}

	public List<ContestData> getContestDataDesc(Long contestId, String orderBy) {

		switch (orderBy) {
		case OrderBy.TOKEN_AMOUNT:

			return contestDataRepository.findByContestHeaderIdOrderByTokenAmountDesc(contestId);

		case OrderBy.PURCHASE_VALUE_IN_ETH:
			return contestDataRepository.findByContestHeaderIdOrderByPurchaseValueInETHDesc(contestId);

		case OrderBy.PURCHASE_VALUE_IN_USD:
			return contestDataRepository.findByContestHeaderIdOrderByPurchaseValueInUSDDesc(contestId);

		default:
			return contestDataRepository.findByContestHeaderIdOrderByTokenAmountDesc(contestId);

		}

	}

	public List<ContestData> getContestDataExcludingSellsDesc(Long contestId, String orderBy) {

		List<ContestData> contestsData;

		switch (orderBy) {
		case OrderBy.TOKEN_AMOUNT:

			contestsData = contestDataRepository
					.findByContestHeaderIdAndTokenAmountGreaterThanOrderByTokenAmountDesc(contestId, 1.0);

			return ContestDataMedianUtil.medianContestWinners(contestsData, false);

		case OrderBy.PURCHASE_VALUE_IN_USD:
			contestsData = contestDataRepository.findByContestHeaderIdOrderByPurchaseValueInUSDDesc(contestId);
			return ContestDataMedianUtil.medianContestWinnersByPurchaseValueInUSD(contestsData, false);

		/*
		 * case OrderBy.PURCHASE_VALUE_IN_ETH: break; case
		 * OrderBy.PURCHASE_VALUE_IN_USD:
		 */

		default:
			return contestDataRepository.findByContestHeaderIdAndTokenAmountGreaterThanOrderByTokenAmountDesc(contestId,
					1.0);

		}
	}

	/**
	 * Gets the current leaders and median leaders
	 * 
	 * @param contestId
	 * @return
	 */
	public List<ContestData> getCurrentLeadersContestData(Long contestId, String orderBy, boolean onlyLeaders) {
		if (onlyLeaders) {
			/*
			 * List<ContestData> contestsData = contestDataRepository
			 * .findByContestHeaderIdAndTokenAmountGreaterThanOrderByTokenAmountDesc(
			 * contestId, 1.0);
			 */

			List<ContestData> contestsData = contestDataRepository
					.findByContestHeaderIdOrderByPurchaseValueInUSDDesc(contestId);

			int index = 0;
			List<ContestData> result = new ArrayList<ContestData>();
			for (ContestData data : contestsData) {
				if (index < 4) {
					data.setWinner(true);
					result.add(data);
					index++;
				} else
					break;

			}

			result.addAll(ContestDataMedianUtil.medianContestWinnersByPurchaseValueInUSD(contestsData, true));

			return result;
		} else {
			return getContestDataExcludingSellsDesc(contestId, orderBy);
		}

	}

	public List<ContestData> getAllTypesLeaders(Long contestId, String orderBy, String type) {
		switch (type) {
		case LeaderboardType.SHOW_ALL:
			
			return getCurrentLeadersContestData(contestId, orderBy, false);

		case LeaderboardType.MEDIAN_LEADERS:
			return getCurrentLeadersContestData(contestId, orderBy, true);

		case LeaderboardType.WHALES:
			return getCumulativeLeaders(contestId, orderBy,10);

		default:
			return getCurrentLeadersContestData(contestId, orderBy, false);
			
		}
	}

	public List<ContestData> getCumulativeLeaders(Long contestId, String orderBy, int top) {
		List<CumulativeContestData> cumulativeContestData = cumulativeContestDataRepository
				.findByContestHeaderIdOrderByPurchaseValueInUSDDesc(contestId);

		List<ContestData> result = new ArrayList<ContestData>();

		

		int index = 1;
		for (CumulativeContestData data : cumulativeContestData) {

			if (index <= top) {

				ContestData contestData = ContestData.builder().address(data.getAddress())
						.contestHeader(data.getContestHeader()).id(data.getId()).isMedian(data.isMedian())
						.isMedianLeader(data.isMedianLeader()).isWinner(data.isWinner())
						.purchaseValueInETH(data.getPurchaseValueInETH())
						.purchaseValueInUSD(data.getPurchaseValueInUSD()).rank(index)
						.tokenAmount(data.getTokenAmount()).build();

				result.add(contestData);
				index++;
			} else
				break;
		}

		return result;

	}

}
