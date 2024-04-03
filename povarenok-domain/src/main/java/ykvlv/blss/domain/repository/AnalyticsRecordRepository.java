package ykvlv.blss.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ykvlv.blss.domain.entity.AnalyticsRecord;

import java.time.LocalDateTime;

@Repository
public interface AnalyticsRecordRepository extends JpaRepository<AnalyticsRecord, Long> {

	void deleteAllByPeriodStartBefore(@NonNull LocalDateTime periodStart);

}
