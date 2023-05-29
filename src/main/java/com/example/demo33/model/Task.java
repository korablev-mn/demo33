package com.example.demo33.model;

import com.example.demo33.validator.isNoRussianPhones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "title", unique = true)
    private String title;

    @Length(min = 0, max = 100)
    @isNoRussianPhones
    private String description = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creation_id", nullable = false, updatable = false)
    @NotNull
    private Usser user;

    @CreationTimestamp
    @Column(name = "created", updatable=false)
    private Instant created;

    @UpdateTimestamp
    private Instant updated;

    public void setTask(Task task) {
        title = task.getTitle();
        description = task.getDescription();
    }

    @PrePersist
    protected void onCreate() {
        created = Instant.now();
        created = updated;
    }

    @PreUpdate
    protected void onUpdate() {
        updated = Instant.now();
    }
}