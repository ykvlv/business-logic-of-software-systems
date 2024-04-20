package ykvlv.blss.application.task.schedule;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.application.PovarenokProperties;
import ykvlv.blss.domain.repository.AnalyticsRecordRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CleanupAnalyticsTask implements JavaDelegate {

	private final AnalyticsRecordRepository analyticsRecordRepository;
	private final PovarenokProperties povarenokProperties;

	@Override
	public void execute(DelegateExecution execution) {
		var now = LocalDateTime.now();
		var deleteBefore = now.minusDays(povarenokProperties.getDeleteAnalyticsRecordsOlderThanDays());

		analyticsRecordRepository.deleteAllByPeriodStartBefore(deleteBefore);
	}
}
