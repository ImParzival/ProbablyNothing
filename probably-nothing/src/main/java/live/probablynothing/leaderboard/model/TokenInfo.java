package live.probablynothing.leaderboard.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)	
	private String tokenContract;
	
	@Column(nullable = true)
	private String tokenSymbol;
	
	@Column(nullable = true)
	private double marketCap;
	
	@Column(nullable = true)
	private long totalTokenHolders;
	
	@Column(nullable = true)
	private double tokenPrice;
}
