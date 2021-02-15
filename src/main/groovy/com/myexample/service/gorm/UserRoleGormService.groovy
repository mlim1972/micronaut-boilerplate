package com.myexample.service.gorm

import com.myexample.domain.Role
import com.myexample.domain.User
import com.myexample.domain.UserRole
import grails.gorm.services.Query
import grails.gorm.services.Service

@Service(UserRole)
interface UserRoleGormService {

    UserRole save(User user, Role role)

    UserRole find(User user, Role role)

    void delete(Serializable id)

    @Query("""select $r.authority
    from ${UserRole ur}
    inner join ${User u = ur.user}
    inner join ${Role r = ur.role}
    where $u.email = $email""")
    List<String> findAllAuthoritiesByEmail(String email)

}