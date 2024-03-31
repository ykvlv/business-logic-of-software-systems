package ykvlv.blss.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import ykvlv.blss.data.type.TagEnum;
import ykvlv.blss.data.type.RecipeTypeEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipe")
public class Recipe {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_id_seq")
    @SequenceGenerator(sequenceName = "recipe_id_seq", name = "recipe_id_seq", allocationSize = 1)
    private Long id;

    @NonNull
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

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
    @Builder.Default
    @Column(name = "likes_count", nullable = false)
    private Long likesCount = 0L;

    @NonNull
    @Column(name = "recipe_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecipeTypeEnum recipeTypeEnum;

    @NonNull
    @ToString.Exclude
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id", nullable = false))
    @Column(name = "tag", nullable = false)
    private Set<TagEnum> tagEnums = new HashSet<>();

    @NonNull
    @Builder.Default
    @ToString.Exclude
    @OrderBy("orderNumber")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CookingStep> cookingSteps = new ArrayList<>();

    @NonNull
    @Builder.Default
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @NonNull
    @ToString.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id")
    )
    private Set<Client> likes = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe that)) {
            return false;
        }
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
