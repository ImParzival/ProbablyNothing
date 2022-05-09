package live.probablynothing.leaderboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import live.probablynothing.leaderboard.model.ContestHeader;

@Repository
public interface ContestHeaderRepository extends JpaRepository<ContestHeader, Long> {


	
	public List<ContestHeader> findByIsActive(boolean isActive);

}
