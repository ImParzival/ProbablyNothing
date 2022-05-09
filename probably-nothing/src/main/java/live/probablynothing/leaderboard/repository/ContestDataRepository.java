package live.probablynothing.leaderboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import live.probablynothing.leaderboard.model.ContestData;

/**
 * @author anand
 *
 */
@Repository
public interface ContestDataRepository extends JpaRepository<ContestData, Long> {

	public List<ContestData> findByContestHeaderId(Long id);

	public List<ContestData> findByContestHeaderIdOrderByTokenAmountDesc(Long id);

	public List<ContestData> findByContestHeaderIdOrderByPurchaseValueInETHDesc(Long id);

	public List<ContestData> findByContestHeaderIdOrderByPurchaseValueInUSDDesc(Long id);

	/**
	 * Joins the ContestData and ContestHeader and gets the contestData where
	 * ContestHeader.Id = :id and tokenAmount > parameter :tokenAmount then Orders
	 * by tokenAmount in Descending
	 */	
	public List<ContestData> findByContestHeaderIdAndTokenAmountGreaterThanOrderByTokenAmountDesc(Long id,
			double tokenAmount);
	
	

	@Query("FROM ContestData as cd JOIN cd.contestHeader ch WHERE ch.id = :id AND cd.tokenAmount > :tokenAmount ORDER BY cd.tokenAmount DESC")
	public List<ContestData> getLeaderboardWithoutSellsData(Long id, double tokenAmount);
}
