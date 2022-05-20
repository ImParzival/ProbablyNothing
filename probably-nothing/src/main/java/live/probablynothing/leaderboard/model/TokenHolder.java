package live.probablynothing.leaderboard.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Currently this entity is not used. In future it will be included as part of the <code>TokenInfo</code> entity
 * @author ImParzival
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenHolder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String walletAddress;
	
	/*
	 * @ManyToMany private TokenInfo tokenInfo;
	 */
	
	private String balance;

}
