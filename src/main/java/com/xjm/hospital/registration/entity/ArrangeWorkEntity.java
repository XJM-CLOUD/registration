package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "arrange_work", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class ArrangeWorkEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "arrange_work_id")
    private long arrangeWorkId;
    @Basic
    @Column(name = "arrange_work_date")
    private Date arrangeWorkDate;
    @Basic
    @Column(name = "hospital_id")
    private long hospitalId;
    @Basic
    @Column(name = "department_id")
    private long departmentId;
    @Basic
    @Column(name = "doctor_id")
    private long doctorId;
    @Basic
    @Column(name = "total_quantity")
    private Integer totalQuantity;
    @Basic
    @Column(name = "registered_quantity")
    private Integer registeredQuantity;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

}
