package com.balakin.sberbankast.domain;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"operators"})
@ToString(exclude = {"operators"})
@NoArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String description;

    @ManyToMany(mappedBy = "specialties")
    private Set<Operator> operators;

}
