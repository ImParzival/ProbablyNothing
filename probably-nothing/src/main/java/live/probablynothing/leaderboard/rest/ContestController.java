package live.probablynothing.leaderboard.rest;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import live.probablynothing.leaderboard.model.ContestData;
import live.probablynothing.leaderboard.model.ContestHeader;
import live.probablynothing.leaderboard.repository.ContestHeaderRepository;
import live.probablynothing.leaderboard.service.ContestService;
import live.probablynothing.leaderboard.util.DateTimeUtil;

@RestController
public class ContestController {
	
	@Autowired
	ContestService contestService;
	
	@Autowired
	ContestHeaderRepository contestHeaderRepository;

	@PostMapping(value = "/contest")
	public @ResponseBody ResponseEntity createContest(@PathParam("name") String name,
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @PathParam("isActive") boolean isActive)
	
	{
		
		//validate the data
		if(name == null || name.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input valid contest name");
		}
		
		if(!DateTimeUtil.isDateFormatValid(startDate))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input startDate in the format (yyyy-M-dd)");
		}
		
		if(!DateTimeUtil.isDateFormatValid(endDate))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("input endDate in the format (yyyy-M-dd)");
		}
		if(isActive)
		{
			List<ContestHeader> contests = contestHeaderRepository.findByIsActive(isActive);
			if(!contests.isEmpty())
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already an active contest exists...");
			}
		}		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(contestService.createContestHeader(name,startDate,endDate,isActive));
		
				 		
	}
	
	
	@GetMapping("/contests")
	public @ResponseBody ResponseEntity getAllContests()
	{
		return ResponseEntity.status(HttpStatus.OK).body(contestService.getAllContestHeaders());
	}
	
	@GetMapping("/activeContests")
	public @ResponseBody ResponseEntity getActiveContests()
	{
		return ResponseEntity.status(HttpStatus.OK).body(contestService.getActiveContests());
	}
	
	@GetMapping("/contest/{contestId}")
	public @ResponseBody ResponseEntity getContest(@PathVariable("contestId") String contestId) {
		
		return ResponseEntity.status(HttpStatus.OK).body(contestService.getContest(contestId));
	}
	
	@PatchMapping("/contest/{contestId}/activate")
	public @ResponseBody ResponseEntity activateContest(@PathVariable("contestId") String contestId) 
	{
		
		return ResponseEntity.status(HttpStatus.OK).body(contestService.activateContest(contestId));
	}
	
	@PatchMapping("/contest/{contestId}/deActivate")
	public @ResponseBody ResponseEntity deActivateContest(@PathVariable("contestId") String contestId) 
	{
		return ResponseEntity.status(HttpStatus.OK).body(contestService.deActivateContest(contestId));
	}
	
	@DeleteMapping("contest/{contestId}")
	public @ResponseBody ResponseEntity deleteContest(@PathVariable("contestId") String contestId) {
		contestService.deleteContest(contestId);
		return ResponseEntity.status(HttpStatus.OK).body("Contest Deleted");
	}
	
	@GetMapping("contest/{contestId}/leaderboard")
	public @ResponseBody ResponseEntity<List<ContestData>> getLeadeboard(@PathVariable("contestId") String contestId)
	{
		return ResponseEntity.status(HttpStatus.OK).body(contestService.getContestDataDesc(contestId));
	}
	
	
}
