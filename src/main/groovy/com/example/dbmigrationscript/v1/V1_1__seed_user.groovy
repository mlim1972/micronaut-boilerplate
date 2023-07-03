package com.example.dbmigrationscript.v1

import com.example.service.security.BCryptPasswordEncoderService
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

/**
 * This class is responsible for seeding the database with a user. This is not possible in a .sql because
 * the password needs to be encoded.
 */
class V1_1__seed_user extends BaseJavaMigration{

    /**
         #################  INSERTING USER  #############################
        INSERT INTO `user` (`version`,`first_name`,`last_name`, `username`,`password`, `enabled`,
        `password_expired`, `account_locked`, `account_expired`,`date_created`,
        `date_updated`, `notes`
        )
        VALUES (0, 'Super', 'Admin', 'super.admin@company.com', 'password', 1,
                0, 0, 0, NOW(), NOW(), 'notes here');

        #################  INSERTING USER_ROLE  #############################
        INSERT INTO `user_role` (`role_id`, `user_id`)
        VALUES (1, 1);
     */
    @Override
    void migrate(Context context) throws Exception {
        BCryptPasswordEncoderService encoder = new BCryptPasswordEncoderService()
        String pswd = encoder.encode('password')

        String newPerson = "INSERT INTO user (`version`,`first_name`,`last_name`, `username`, `password`, `enabled`, " +
                "`password_expired`,`account_locked`,`account_expired`, `date_created`, `date_updated`) " +
                "VALUES (0, 'Super', 'Admin', 'super.admin@company.com', '$pswd', 1, 0, 0, 0, NOW(), NOW()) "

        String newRole = "INSERT INTO user_role (`role_id`, `user_id`) VALUES (1, 1)"

        // Inserting the user and the new (together) in the database using prepared statements
        def ps1 = context.getConnection().prepareStatement(newPerson)
        def ps2 = context.getConnection().prepareStatement(newRole)
        ps1.execute()
        ps2.execute()
    }
}