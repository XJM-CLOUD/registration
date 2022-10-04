package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "register", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class RegisterEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "register_id")
    private long registerId;
    @Basic
    @Column(name = "date_of_visit")
    private Date dateOfVisit;
    @Basic
    @Column(name = "time_of_visit")
    private Time timeOfVisit;
    @Basic
    @Column(name = "hospital_id")
    private int hospitalId;
    @Basic
    @Column(name = "department_id")
    private int departmentId;
    @Basic
    @Column(name = "doctor_id")
    private long doctorId;
    @Basic
    @Column(name = "doctor_name")
    private String doctorName;
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
    @Column(name = "patient_phone")
    private String patientPhone;
    @Basic
    @Column(name = "patient_sex")
    private Integer patientSex;
    @Basic
    @Column(name = "patient_age")
    private Integer patientAge;
    @Basic
    @Column(name = "patient_address")
    private String patientAddress;
    @Basic
    @Column(name = "registration_fee")
    private BigDecimal registrationFee;
    @Basic
    @Column(name = "queue_number")
    private String queueNumber;
    @Basic
    @Column(name = "visit_status")
    private Integer visitStatus;
    @Basic
    @Column(name = "status")
    private Integer status;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

}
