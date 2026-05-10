package com.uni.impact.category;

import com.uni.impact.campaign.Campaign;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@EnableJpaAuditing
@Getter
@Setter
public class Category {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category")
    private List<Campaign> categoryCampaigns = new ArrayList<>();

}
