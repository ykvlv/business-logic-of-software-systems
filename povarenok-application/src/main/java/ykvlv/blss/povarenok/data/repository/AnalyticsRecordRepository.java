package ykvlv.blss.povarenok.data.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ykvlv.blss.povarenok.data.entity.AnalyticsRecord;

import java.time.LocalDateTime;

@Repository
public interface AnalyticsRecordRepository extends JpaRepository<AnalyticsRecord, Long> {

	void deleteAllByPeriodStartBefore(@NonNull LocalDateTime periodStart);

}
