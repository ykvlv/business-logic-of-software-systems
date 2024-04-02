package ykvlv.blss.povarenok.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "analytics_record")
public class AnalyticsRecord {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analytics_record_id_seq")
	@SequenceGenerator(sequenceName = "analytics_record_id_seq", name = "analytics_record_id_seq", allocationSize = 1)
	private Long id;

	@NonNull
	@Column(name = "register_сount", nullable = false)
	private Long registerCount;

	@NonNull
	@Column(name = "like_count", nullable = false)
	private Long likeCount;

	@NonNull
	@Column(name = "add_recipe_count", nullable = false)
	private Long addRecipeCount;

	@NonNull
	@Column(name = "add_review_count", nullable = false)
	private Long addReviewCount;

	@NonNull
	@Column(name = "period_start", nullable = false)
	private LocalDateTime periodStart;

	@NonNull
	@Column(name = "preiod_end", nullable = false)
	private LocalDateTime periodEnd;
}
