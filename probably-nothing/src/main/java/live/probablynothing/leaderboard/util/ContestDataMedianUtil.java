package live.probablynothing.leaderboard.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import live.probablynothing.leaderboard.model.ContestData;

/**
 * @author ImParzival
 *
 */
public class ContestDataMedianUtil {

	/**
	 * Returns the Median tokenAmount of the ContestData
	 * 
	 * @param contestsData
	 * @return median tokenAmount
	 */

	public static double SMALL_DIFF = 0.00001;

	public static double getMedianTokenAmount(List<ContestData> contestsData) {

		int size = contestsData.size();

		if (size > 1) {
			if (size % 2 != 0) {
				ContestData contestData = contestsData.get(size / 2);
				contestData.setMedian(true);
				return contestData.getTokenAmount();

			} else {
				ContestData median1 = contestsData.get((size - 1) / 2);
				ContestData median2 = contestsData.get(size / 2);
				median1.setMedian(true);
				median2.setMedian(true);
				return (median1.getTokenAmount() + median2.getTokenAmount()) / 2;
			}

		} else if (size == 1) {
			return contestsData.get(size).getTokenAmount();
		} else
			return 0.0;
	}

	public static List<ContestData> medianContestWinners(List<ContestData> contestsData, boolean onlyMedianLeaders) {

		double medianTokenAmount = getMedianTokenAmount(contestsData);

		SortedMap<Double, ContestData> medianMap = new TreeMap<Double, ContestData>();

		int rank = 1;
		for (ContestData contestData : contestsData) {
			
			//set rank
			contestData.setRank(rank);
			rank++;
			double currentTokenAmount = contestData.getTokenAmount();
			if (currentTokenAmount > medianTokenAmount) {

				double medianDiff = currentTokenAmount - medianTokenAmount;
				if (medianMap.containsKey(medianDiff)) {
					double newKey = medianDiff + SMALL_DIFF;
					medianMap.put(newKey, contestData);
				} else
					medianMap.put(medianDiff, contestData);
			} else {
				double medianDiff = medianTokenAmount - currentTokenAmount;
				if (medianMap.containsKey(medianDiff)) {
					double newKey = medianDiff + SMALL_DIFF;
					medianMap.put(newKey, contestData);
				}
				else
					medianMap.put(medianDiff, contestData);
			}

		}
		
		List<ContestData> medianLeaders = new ArrayList<ContestData>();
		
		int count = 1;
		for (Map.Entry<Double, ContestData> entry : medianMap.entrySet()) {
			if (count <= 6) {
				ContestData contestData = entry.getValue();
				contestData.setMedianLeader(true);
				
				contestData.setWinner(true);
				count++;
				if(onlyMedianLeaders) {
					medianLeaders.add(contestData);					
				}			
				
			} else
				break;
		}
		
		if(onlyMedianLeaders)
			return medianLeaders;
		return contestsData;

	}	
	
}
