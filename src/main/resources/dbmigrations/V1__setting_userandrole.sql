/* #################  USER TABLE  ############################# */
CREATE TABLE `user` (
    `id`                 bigint NOT NULL AUTO_INCREMENT,
    `version`            bigint NOT NULL,
    `first_name`         varchar(255) DEFAULT NULL,
    `last_name`          varchar(255) DEFAULT NULL,
    `username`           varchar(255) DEFAULT NULL,
    `password`           varchar(255) NOT NULL,
    `enabled`            bit(1) NOT NULL,
    `password_expired`   bit(1) NOT NULL,
    `account_locked`     bit(1) NOT NULL,
    `account_expired`    bit(1) NOT NULL,
    `date_created`       datetime(6) DEFAULT NULL,
    `date_updated`       datetime(6) DEFAULT NULL,
    `notes`              text,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_USER_USERNAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/* #################  ROLE TABLE  ############################# */
CREATE TABLE `role` (
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `version`      bigint DEFAULT NULL,
    `authority`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ROLE_AUTHORITY` (`authority`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/* #################  USR_ROLE TABLE  ############################# */
CREATE TABLE `user_role` (
    `role_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`role_id`,`user_id`),
    KEY `KK_USERROLE_USERID` (`user_id`),
    CONSTRAINT `FK_USERROLE_USERID` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_USERROLE_ROLEID` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

/*
#####################################
#           SEED DATA               #
#####################################
#################  INSERTING USER  #############################
 */
INSERT INTO `user` (`version`,`first_name`,`last_name`, `username`,`password`, `enabled`,
                    `password_expired`, `account_locked`, `account_expired`,`date_created`,
                    `date_updated`, `notes`
                    )
       VALUES (0, 'Super', 'Admin', 'super.admin@company.com', 'password', 1,
               0, 0, 0, NOW(), NOW(), 'notes here');

/* #################  INSERTING ROLE  ############################# */
INSERT INTO `role` (`version`, `authority`)
        VALUES (0, 'ADMIN');
INSERT INTO `role` (`version`, `authority`)
        VALUES (0, 'USER');
INSERT INTO `role` (`version`, `authority`)
        VALUES (0, 'EDITOR');

/* #################  INSERTING USER_ROLE  ############################# */
INSERT INTO `user_role` (`role_id`, `user_id`)
        VALUES (1, 1);