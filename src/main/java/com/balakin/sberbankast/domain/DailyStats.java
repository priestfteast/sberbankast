package com.balakin.sberbankast.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"operator"})
public class DailyStats {
    public DailyStats(Date date, String number, Long incoming, Long lost, Long lost406, Long outgoingTotal, Long outgoingExternal,
                      Long outgoingInternal, Long holded, Long incomingAvrg, Long totalWorkTime, Long totalNotReadyTime,
                      Long totalAfterCallTime, Long afterCallTimeAvrg, Long totalTalkingTime, Long totalIncomingTime,
                      Long totalOutGoingTime, Long totalExternalOutGoingTime, Long totalHoldTime, Long holdTimeAvrg, Operator operator) {
        this.date = date;
        this.number = number;
        this.incoming = incoming;
        this.lost = lost;
        this.lost406 = lost406;
        this.outgoingTotal = outgoingTotal;
        this.outgoingExternal = outgoingExternal;
        this.outgoingInternal = outgoingInternal;
        this.holded = holded;
        this.incomingAvrg = incomingAvrg;
        this.totalWorkTime = totalWorkTime;
        this.totalNotReadyTime = totalNotReadyTime;
        this.totalAfterCallTime = totalAfterCallTime;
        this.afterCallTimeAvrg = afterCallTimeAvrg;
        this.totalTalkingTime = totalTalkingTime;
        this.totalIncomingTime = totalIncomingTime;
        this.totalOutGoingTime = totalOutGoingTime;
        this.totalExternalOutGoingTime = totalExternalOutGoingTime;
        this.totalHoldTime = totalHoldTime;
        this.holdTimeAvrg = holdTimeAvrg;
        this.operator = operator;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Date date;

    private  String number;

    private Long incoming;

    private Long lost;

    private Long lost406;

    private Long outgoingTotal;

    private Long outgoingExternal;

    private Long outgoingInternal;

    private Long holded;

    private Long incomingAvrg;

    private Long totalWorkTime;

    private Long totalNotReadyTime;

    private Long totalAfterCallTime;

    private Long afterCallTimeAvrg;

    private Long totalTalkingTime;

    private Long totalIncomingTime;

    private Long totalOutGoingTime;

    private Long totalExternalOutGoingTime;

    private Long totalHoldTime;

    private Long holdTimeAvrg;

    @ManyToOne
    private Operator operator;

    public String getTime(Long time) {

        int seconds   = Math.toIntExact(time);
//        int finalHours =  (seconds/3600);
        String finalHours = String.valueOf(seconds/3600);
        int secondsLeft = seconds%3600;
        String finalMinutes = String.valueOf(secondsLeft/60);
        if(finalMinutes.length()==1)
            finalMinutes="0"+finalMinutes;
//        int minutesFinal = secondsLeft/60;
//        int secondsFinal = secondsLeft%60;
        String finalSeconds = String.valueOf(secondsLeft%60);
        if(finalSeconds.length()==1)
            finalSeconds="0"+finalSeconds;
        String result = finalHours+":"+finalMinutes+":"+finalSeconds;


        return result;
    }

    @Override
    public String toString() {
        return "DailyStats{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", number=" + number +
                ", incoming=" + incoming +
                ", lost=" + lost +
                ", outgoingTotal=" + outgoingTotal +
                ", outgoingExternal=" + outgoingExternal +
                ", holded=" + holded +
                ", incomingAvrg=" + getTime( incomingAvrg) +
                ", totalWorkTime=" + getTime( totalWorkTime)+
                ", totalNotReadyTime=" + getTime( totalNotReadyTime )+
                ", totalAfterCallTime=" + getTime( totalAfterCallTime )+
                ", totalTalkingTime=" + getTime( totalTalkingTime) +
                ", totalIncomingTime=" +getTime(  totalIncomingTime) +
                ", totalOutGoingTime=" + getTime( totalOutGoingTime) +
                ", totalExternalOutGoingTime=" + getTime( totalExternalOutGoingTime) +
                ", totalHoldTime=" + getTime( totalHoldTime) +
                ", operator=" + operator +
                '}';
    }
}
