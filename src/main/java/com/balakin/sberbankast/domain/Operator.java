package com.balakin.sberbankast.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"notes","specialties"})
@NoArgsConstructor
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String number;

    private LocalDate employementDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @ManyToMany
    @JoinTable(name = "operator_specialty",
            joinColumns = @JoinColumn(name = "operator_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    private Shift shift;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operator")
    private Set<Fine> fines = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "operator")
    private Set<Bonus> bonuses = new HashSet<>();




}
