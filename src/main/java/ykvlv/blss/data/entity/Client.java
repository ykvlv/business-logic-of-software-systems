package ykvlv.blss.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client", indexes = {@Index(name = "client_login_idx", columnList = "login", unique = true)})
public class Client {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_id_seq")
	@SequenceGenerator(sequenceName = "client_id_seq", name = "client_id_seq", allocationSize = 1)
	private Long id;

	@NonNull
	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@NonNull
	@Column(name = "name", nullable = false)
	private String name;

	@ToString.Exclude
	@Builder.Default
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "clients_favorite_medias",
			joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "media_id", referencedColumnName = "id")
	)
	private Set<Media> favoriteMedias = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Client that)) {
			return false;
		}
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

}
