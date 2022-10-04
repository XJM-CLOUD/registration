package com.xjm.hospital.registration.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "arrange_work_time_interval", schema = "hospital", catalog = "")
@Data
@EqualsAndHashCode
public class ArrangeWorkTimeIntervalEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "time_interval_id")
    private long timeIntervalId;
    @Basic
    @Column(name = "time_interval")
    private Time timeInterval;
    @Basic
    @Column(name = "arrange_work_id")
    private long arrangeWorkId;
    @Basic
    @Column(name = "total_quantity")
    private Integer totalQuantity;
    @Basic
    @Column(name = "registered_quantity")
    private Integer registeredQuantity;
    @Basic
    @Column(name = "start_serial_number")
    private Integer startSerialNumber;
    @Basic
    @Column(name = "end_serial_number")
    private Integer endSerialNumber;
    @Basic
    @Column(name = "queue_no_json")
    private String queueNoJson;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;

}
