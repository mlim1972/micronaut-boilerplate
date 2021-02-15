package com.myexample.service.gorm

import com.myexample.domain.Role
import grails.gorm.services.Service

@Service(Role)
interface RoleGormService {
    Role save(String authority)
    Role find(String authority)
    void delete(Serializable id)
}