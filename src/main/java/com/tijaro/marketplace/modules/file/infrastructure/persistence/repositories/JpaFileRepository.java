package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaFileRepository extends JpaRepository<File, UUID> {

}
