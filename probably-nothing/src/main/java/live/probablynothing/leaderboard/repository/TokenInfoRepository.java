package live.probablynothing.leaderboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import live.probablynothing.leaderboard.model.TokenInfo;

@Repository
public interface TokenInfoRepository extends JpaRepository<TokenInfo, Long> {

	public TokenInfo findByTokenContract(String tokenContract);
}
