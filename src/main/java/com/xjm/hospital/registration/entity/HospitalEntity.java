package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hospital", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class HospitalEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hospital_id")
    private long hospitalId;
    @Basic
    @Column(name = "hospital_name")
    private String hospitalName;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "introduction")
    private String introduction;
    @Basic
    @Column(name = "enable_status")
    private int enableStatus;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

}
