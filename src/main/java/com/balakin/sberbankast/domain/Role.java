package com.balakin.sberbankast.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(exclude = {"users"})
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    @ManyToMany(mappedBy = "roles")
    private List< User > users;
}