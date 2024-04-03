package ykvlv.blss.application.analytics;

import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import ykvlv.blss.domain.repository.AnalyticsRecordRepository;

@Component
@RequiredArgsConstructor
public class AnalyticsJob implements Job {

	private final AnalyticsAspect analyticsAspect;
	private final AnalyticsRecordRepository analyticsRecordRepository;

	@Override
	public void execute(JobExecutionContext context) {
		var analyticsRecord = analyticsAspect.getAndResetAnalyticsData();

		analyticsRecordRepository.save(analyticsRecord);
	}
}
