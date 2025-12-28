package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends CommonRepository<Admin ,Integer>
{
          public Admin findByUserName(String userName);
}
