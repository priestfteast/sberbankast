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

    public static String getTime(Long time) {

        int seconds   = Math.toIntExact(time);
        int finalHours =  (seconds/3600);
        int secondsLeft = seconds%3600;
        int minutesFinal = secondsLeft/60;
        int secondsFinal = secondsLeft%60;
        String result = String.valueOf(finalHours)+":"+String.valueOf(minutesFinal)+":"+secondsFinal;

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
