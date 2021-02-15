#################  ROLE TABLE  #############################
CREATE TABLE `role`
(
    `id`        bigint(20)   NOT NULL AUTO_INCREMENT,
    `version`   bigint(20)   NOT NULL,
    `authority` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ROLE_AUTHORITY` (`authority`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

#################  USER_ROLE TABLE  #############################
CREATE TABLE `user_role`
(
    `id`        bigint(20)  NOT NULL AUTO_INCREMENT,
    `version`   bigint(20)  NOT NULL,
    `role_id`   bigint(20)  NOT NULL,
    `user_id`   bigint(20)  NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_USERROLE_USER_ID`
        FOREIGN KEY (`user_id`) REFERENCES user (`id`),
    CONSTRAINT `FK_USERROLE_ROLE_ID`
        FOREIGN KEY (`role_id`) REFERENCES role (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

#####################################
#           SEED DATA               #
#####################################
#################  INSERTING ROLES  #############################
INSERT INTO `role` (`version`,`authority`) VALUES (0,'USER');
INSERT INTO `role` (`version`,`authority`) VALUES (0,'ADMIN');