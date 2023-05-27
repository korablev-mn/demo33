package com.example.demo33.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usser { //User зарезервированное слово в Postgres!

    @Id
    @NonNull
    @Column(name = "id")
    private Long id;

    private String fio;
}