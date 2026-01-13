package com.pocketpipo.pocketpipo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocketpipo.pocketpipo.entity.IdempotencyKey;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, Long> {

    Optional<IdempotencyKey> findByUserIdAndOperationAndIdemKey(
            Long userId,
            String operation,
            String idemKey
    );
}
