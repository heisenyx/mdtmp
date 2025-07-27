package dev.heisen.metadata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "publications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publication {

    @Id
    @Column(length = 8, nullable = false)
    private String hash;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "ttl_minutes", nullable = false)
    private int ttlMinutes;

    @Column(name = "is_expired", nullable = false)
    private boolean expired;
}