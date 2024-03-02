package ykvlv.blss.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ykvlv.blss.data.type.GenreEnum;
import ykvlv.blss.data.type.MediaTypeEnum;

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
@Table(name = "media")
public class Media {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @Column(name = "poster_uuid")
    private String posterUUID;

    @NonNull
    @Column(name = "media_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaTypeEnum mediaTypeEnum;

    @Builder.Default
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "media_genres", joinColumns = @JoinColumn(name = "media_id", nullable = false))
    @Column(name = "genre", nullable = false)
    private Set<GenreEnum> genreEnums = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media that)) {
            return false;
        }
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
