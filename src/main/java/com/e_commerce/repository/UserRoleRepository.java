package com.e_commerce.repository;

import com.e_commerce.entity.UserRole;
import com.e_commerce.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {

}
