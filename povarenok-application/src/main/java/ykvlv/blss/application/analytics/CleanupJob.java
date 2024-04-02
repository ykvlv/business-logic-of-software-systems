package ykvlv.blss.application.analytics;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.domain.repository.AnalyticsRecordRepository;

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
