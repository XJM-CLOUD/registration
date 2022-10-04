package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "patient", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class PatientEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "patient_id")
    private long patientId;
    @Basic
    @Column(name = "user_id")
    private long userId;
    @Basic
    @Column(name = "real_name")
    private String realName;
    @Basic
    @Column(name = "id_card_no")
    private String idCardNo;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "sex")
    private Integer sex;
    @Basic
    @Column(name = "age")
    private Integer age;
    @Basic
    @Column(name = "child_or_not")
    private Integer childOrNot;
    @Basic
    @Column(name = "guardian_real_name")
    private String guardianRealName;
    @Basic
    @Column(name = "guardian_id_card_no")
    private String guardianIdCardNo;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

}
