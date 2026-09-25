package com.tijaro.marketplace.modules.file.infrastructure.persistence.repositories;

import com.tijaro.marketplace.modules.file.domain.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaFileRepository extends JpaRepository<File, UUID> {
     Optional<File> findByUid(UUID uid);
     List<File> findAllByUidIn(List<UUID> uids);
}
