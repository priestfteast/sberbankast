package com.balakin.sberbankast.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"operator"})
public class DailyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String date;


    private Long incoming;


    private Long lost;


    private Long outgoing;


    private Long holded;


    private Long incomingAvrg;


    private Long totalWorkTime;


    private Long totalNotReadyTime;


    private Long totalAfterCallTime;


    private Long totalTalkingTime;


    private Long totalIncomingTime;


    private Long totalOutGoingTime;


    private Long totalExternalOutGoingTime;


    private Long totalHoldTime;

    @ManyToOne
    private Operator operator;
}
