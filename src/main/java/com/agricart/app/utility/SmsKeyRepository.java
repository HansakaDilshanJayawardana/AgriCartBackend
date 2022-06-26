package com.agricart.app.utility;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsKeyRepository extends JpaRepository<SmsKeyEntity , Long> {
    Optional<SmsKeyEntity>findByCartId(long cartId);

    Optional<Object> findByCartIdAndKey(long cartId, int key);

}
