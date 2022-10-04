package com.xjm.hospital.registration.repository;

import com.xjm.hospital.registration.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "workers", collectionResourceRel = "workers")
public interface WorkerEntityRepository extends JpaRepository<WorkerEntity, Long>, JpaSpecificationExecutor<WorkerEntity> {

}