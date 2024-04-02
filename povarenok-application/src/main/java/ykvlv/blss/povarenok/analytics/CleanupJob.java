package ykvlv.blss.povarenok.analytics;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ykvlv.blss.povarenok.PovarenokProperties;
import ykvlv.blss.povarenok.data.repository.AnalyticsRecordRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CleanupJob implements Job {

	private final AnalyticsRecordRepository analyticsRecordRepository;
	private final PovarenokProperties povarenokProperties;

	@Override
	public void execute(JobExecutionContext context) {
		var now = LocalDateTime.now();
		var deleteBefore = now.minusDays(povarenokProperties.getDeleteAnalyticsRecordsOlderThanDays());

		analyticsRecordRepository.deleteAllByPeriodStartBefore(deleteBefore);
	}
}
