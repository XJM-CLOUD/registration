package com.xjm.hospital.registration.entity;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker", schema = "hospital", catalog = "")
@Data
public class WorkerEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "worker_id")
    private long workerId;
    @Column(name = "user_id")
    private long userId;
    @Basic
    @Column(name = "real_name")
    private String realName;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "id_card_no")
    private String idCardNo;
    @Basic
    @Column(name = "sex")
    private Integer sex;
    @Basic
    @Column(name = "age")
    private Integer age;
    @Basic
    @Column(name = "graduated_from")
    private String graduatedFrom;
    @Basic
    @Column(name = "degree_of_education")
    private String degreeOfEducation;
    @Basic
    @Column(name = "nation")
    private String nation;
    @Basic
    @Column(name = "native_place")
    private String nativePlace;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "entry_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate entryTime;
    @Basic
    @Column(name = "enable_status")
    private int enableStatus;
    @Basic
    @Column(name = "create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;


    public static void main(String[] args) {
        WorkerEntity workerEntity = new WorkerEntity();
        workerEntity.setWorkerId(1);
        workerEntity.setUserId(1);
        workerEntity.setRealName("张三");
        workerEntity.setSex(1);
        workerEntity.setAge(30);
        workerEntity.setPhone("13310266227");
        workerEntity.setIdCardNo("500235199210222892");
        workerEntity.setNation("汉族");
        workerEntity.setNativePlace("重庆");
        workerEntity.setDegreeOfEducation("本科");
        workerEntity.setGraduatedFrom("重庆大学");
        workerEntity.setAddress("重庆江北石马河街道");
        workerEntity.setEnableStatus(1);
        workerEntity.setEntryTime(LocalDate.now());
        workerEntity.setCreateTime(LocalDateTime.now());

        System.out.println(JSONUtil.toJsonStr(workerEntity));
    }
}
