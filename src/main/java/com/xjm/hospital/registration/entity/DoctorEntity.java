package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "doctor", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class DoctorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "doctor_id")
    private long doctorId;
    @Basic
    @Column(name = "worker_id")
    private long workerId;
    @Basic
    @Column(name = "real_name")
    private String realName;
    @Basic
    @Column(name = "hospital_id")
    private long hospitalId;
    @Basic
    @Column(name = "department_id")
    private long departmentId;
    @Basic
    @Column(name = "professional_title_grade")
    private String professionalTitleGrade;
    @Basic
    @Column(name = "qualification_image_src")
    private String qualificationImageSrc;
    @Basic
    @Column(name = "head_portrait_image_src")
    private String headPortraitImageSrc;
    @Basic
    @Column(name = "introduction")
    private String introduction;
    @Basic
    @Column(name = "enable_status")
    private int enableStatus;

}
