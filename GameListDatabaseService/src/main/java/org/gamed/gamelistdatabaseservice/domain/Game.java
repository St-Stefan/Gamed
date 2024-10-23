package org.gamed.gamelistdatabaseservice.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Game")
@EntityListeners(AuditingEntityListener.class)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String developer;

    @Column(nullable = false)
    private LocalDateTime release_date;

    @Column(nullable = false)
    private String platforms;

    public Game() {}

    public Game(String name, String developer, LocalDateTime release_date, String platforms) {
        this.name = name;
        this.developer = developer;
        this.release_date = release_date;
        this.platforms = platforms;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }


    public LocalDateTime getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(LocalDateTime release_date) {
        this.release_date = release_date;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", developer='" + developer + '\'' +
                ", release_date='" + release_date + '\'' +
                ", platforms='" + platforms + '\'' +
                '}';
    }
}