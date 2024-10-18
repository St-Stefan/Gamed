package org.gamed.reviewdatabaseservice.domain;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Rating")
@EntityListeners(AuditingEntityListener.class)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String graph;

    @ManyToOne
    @JoinColumn(name = "review_id", insertable = false, updatable = false)
    private Review review;

    public Rating() {}

    public Rating(Review review, String category, int score, String graph) {
        this.review = review;
        this.category = category;
        this.score = score;
        this.graph = graph;
    }

    // Getters and setters

    public String getId() {
        return id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public Review getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id='" + id + '\'' +
                ", review_id='" + review + '\'' +
                ", category='" + category + '\'' +
                ", score=" + score +
                ", graph='" + graph + '\'' +
                '}';
    }
}