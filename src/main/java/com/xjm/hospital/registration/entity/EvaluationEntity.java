package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "evaluation", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class EvaluationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "evaluation_id")
    private long evaluationId;
    @Basic
    @Column(name = "register_id")
    private long registerId;
    @Basic
    @Column(name = "user_id")
    private long userId;
    @Basic
    @Column(name = "patient_id")
    private long patientId;
    @Basic
    @Column(name = "patient_name")
    private String patientName;
    @Basic
    @Column(name = "doctor_id")
    private long doctorId;
    @Basic
    @Column(name = "doctor_name")
    private String doctorName;
    @Basic
    @Column(name = "patient_satisfaction")
    private int patientSatisfaction;
    @Basic
    @Column(name = "patient_comment")
    private String patientComment;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

}
