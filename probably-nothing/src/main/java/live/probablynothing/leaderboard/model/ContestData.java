package live.probablynothing.leaderboard.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import live.probablynothing.leaderboard.util.CustomDoubleSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestData {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//foreign key from the header
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name="header_id", referencedColumnName = "id", insertable=true, updatable=false, nullable=false)
	private ContestHeader contestHeader;
	
	private String address;
	
	@Column(precision = 10, scale = 2)
	@JsonSerialize(using = CustomDoubleSerializer.class)
	private double tokenAmount;
	
	@Column(precision = 10, scale = 2)
	@JsonSerialize(using = CustomDoubleSerializer.class)
	private double purchaseValueInETH;
	
	@Column(precision = 10, scale = 2)
	@JsonSerialize(using = CustomDoubleSerializer.class)
	private double purchaseValueInUSD;
	
	@Transient
	private boolean isMedianLeader;
	
	@Transient
	private boolean isMedian;
	
	@Transient
	private boolean isWinner;
	
	@Transient
	private int rank;

}
