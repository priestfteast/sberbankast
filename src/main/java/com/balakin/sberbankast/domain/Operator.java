package com.balakin.sberbankast.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private String additionalNumber;

    private LocalDate employementDate;

    private boolean incoming;

    private boolean outgoing;

    private boolean stake;

    private boolean card;

    private boolean fired;

    private LocalDate retirementDate;


    @Lob
    private Byte[] image;

    @Override
    public String toString() {
        return "Operator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @ManyToMany
    @JoinTable(name = "operator_specialty",
            joinColumns = @JoinColumn(name = "operator_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private List<Specialty> specialties = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private Shift shift;

    @Enumerated(value = EnumType.STRING)
    private Company company;

    @Enumerated(value = EnumType.STRING)
    private ContractType contractType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operator")
    private Set<Fine> fines = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operator")
    private Set<Bonus> bonuses = new HashSet<>();

    @OneToMany
    private List<DailyStats> dailyStats = new ArrayList<>();

    public Operator addBonus(Bonus bonus){
        bonus.setOperator(this);
        this.getBonuses().add(bonus);
        return this;

    }

    public Operator addFine(Fine fine){
        fine.setOperator(this);
        this.getFines().add(fine);
        return this;

    }

    public  int getYears() {
        Period period = Period.between(this.getEmployementDate(),LocalDate.now());
        return period.getYears();

    }
}
