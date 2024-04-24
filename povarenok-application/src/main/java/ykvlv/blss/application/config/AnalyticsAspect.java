package ykvlv.blss.application.config;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ykvlv.blss.domain.entity.AnalyticsRecord;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
@Component
public class AnalyticsAspect {

	private LocalDateTime periodStart = LocalDateTime.now();

	private final AtomicLong registerCount = new AtomicLong();
	private final AtomicLong likeCount = new AtomicLong();
	private final AtomicLong addRecipeCount = new AtomicLong();

	@AfterReturning("execution(* ykvlv.blss.application.task.register.CreateUserTask.execute(..))")
	private void countRegistration() {
		registerCount.incrementAndGet();
	}

	@AfterReturning("execution(* ykvlv.blss.application.task.recipe.LikeRecipeTask.execute(..))")
	private void countLike() {
		likeCount.incrementAndGet();
	}

	@AfterReturning("execution(* ykvlv.blss.application.task.recipe.CreateRecipeTask.execute(..))")
	private void countAddRecipe() {
		addRecipeCount.incrementAndGet();
	}

	public AnalyticsRecord getAndResetAnalyticsData() {
		var periodEnd = LocalDateTime.now();

		var analyticsRecord = AnalyticsRecord.builder()
				.registerCount(registerCount.getAndSet(0))
				.likeCount(likeCount.getAndSet(0))
				.addRecipeCount(addRecipeCount.getAndSet(0))
				.periodStart(periodStart)
				.periodEnd(periodEnd)
				.build();

		periodStart = periodEnd;

		return analyticsRecord;
	}
}
