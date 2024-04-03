package ykvlv.blss.application.analytics;

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
	private final AtomicLong addReviewCount = new AtomicLong();

	@AfterReturning("execution(* ykvlv.blss.application.service.ClientService.create(..))")
	private void countRegistration() {
		registerCount.incrementAndGet();
	}

	@AfterReturning("execution(* ykvlv.blss.application.service.ClientService.likeRecipe(..))")
	private void countLike() {
		likeCount.incrementAndGet();
	}

	@AfterReturning("execution(* ykvlv.blss.application.service.RecipeService.create(..))")
	private void countAddRecipe() {
		addRecipeCount.incrementAndGet();
	}

	@AfterReturning("execution(* ykvlv.blss.application.service.ReviewService.create(..))")
	private void countAddReview() {
		addReviewCount.incrementAndGet();
	}

	public AnalyticsRecord getAndResetAnalyticsData() {
		var periodEnd = LocalDateTime.now();

		var analyticsRecord = AnalyticsRecord.builder()
				.registerCount(registerCount.getAndSet(0))
				.likeCount(likeCount.getAndSet(0))
				.addRecipeCount(addRecipeCount.getAndSet(0))
				.addReviewCount(addReviewCount.getAndSet(0))
				.periodStart(periodStart)
				.periodEnd(periodEnd)
				.build();

		periodStart = periodEnd;

		return analyticsRecord;
	}
}
