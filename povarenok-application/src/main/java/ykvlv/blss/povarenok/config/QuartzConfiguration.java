package ykvlv.blss.povarenok.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ykvlv.blss.povarenok.analytics.AnalyticsJob;
import ykvlv.blss.povarenok.analytics.CleanupJob;

@Configuration
public class QuartzConfiguration {

	@Bean
	public JobDetail analyticsJobDetail() {
		return JobBuilder.newJob(AnalyticsJob.class).withIdentity("AnalyticsJob").storeDurably().build();
	}

	@Bean
	public Trigger analyticsJobTrigger() {
		return TriggerBuilder.newTrigger().forJob(analyticsJobDetail())
				.withIdentity("AnalyticsTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // Каждую минуту
				.build();
	}

	@Bean
	public JobDetail cleanupJobDetail() {
		return JobBuilder.newJob(CleanupJob.class).withIdentity("CleanupJob").storeDurably().build();
	}

	@Bean
	public Trigger cleanupJobTrigger() {
		return TriggerBuilder.newTrigger().forJob(cleanupJobDetail())
				.withIdentity("CleanupTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) // Ежедневно в полночь
				.build();
	}
}