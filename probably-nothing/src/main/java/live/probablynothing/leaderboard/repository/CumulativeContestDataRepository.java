package live.probablynothing.leaderboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import live.probablynothing.leaderboard.model.CumulativeContestData;

@Repository
public interface CumulativeContestDataRepository extends JpaRepository<CumulativeContestData, Long>{

	public List<CumulativeContestData> findByContestHeaderIdOrderByPurchaseValueInUSDDesc(Long id);
}
