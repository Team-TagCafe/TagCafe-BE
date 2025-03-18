package com.Minjin.TagCafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved_cafe", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "cafe_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedCafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savedCafeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cafe cafe;

    @Column(nullable = false)
    private Boolean visited = false;

    public SavedCafe(User user, Cafe cafe) {
        this.user = user;
        this.cafe = cafe;
        this.visited = false;
    }
}
