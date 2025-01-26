package com.e_commerce.repository;

import com.e_commerce.entity.SizeVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeVariantRepository extends JpaRepository<SizeVariant, Long> {
}
