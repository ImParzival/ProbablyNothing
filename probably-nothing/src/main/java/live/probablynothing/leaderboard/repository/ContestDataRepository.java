package live.probablynothing.leaderboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import live.probablynothing.leaderboard.model.ContestData;

@Repository
public interface ContestDataRepository extends JpaRepository<ContestData, String>{
	

	public List<ContestData> findByContestHeaderId(String id);
	
	public List<ContestData> findByContestHeaderIdOrderByPurchaseAmountDesc(String id);
}
