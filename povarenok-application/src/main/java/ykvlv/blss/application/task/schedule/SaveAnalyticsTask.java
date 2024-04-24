package ykvlv.blss.application.task.schedule;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ykvlv.blss.application.config.AnalyticsAspect;
import ykvlv.blss.domain.repository.AnalyticsRecordRepository;

@Component
@RequiredArgsConstructor
public class SaveAnalyticsTask implements JavaDelegate {

	private final AnalyticsAspect analyticsAspect;
	private final AnalyticsRecordRepository analyticsRecordRepository;

	@Override
	public void execute(DelegateExecution execution) {
		var analyticsRecord = analyticsAspect.getAndResetAnalyticsData();

		analyticsRecordRepository.save(analyticsRecord);
	}
}
