
#################  USER TABLE  #############################
CREATE TABLE `user`
(
    `id`  bigint(20) NOT NULL AUTO_INCREMENT,
    `version`          bigint(20)   NOT NULL,
    `email`            varchar(255) NOT NULL,
    `password`         varchar(255) NOT NULL,
    `password_expired` bit          NOT NULL,
    `account_locked`   bit          NOT NULL,
    `account_expired`  bit          NOT NULL,
    `enabled`          bit          NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_USER_EMAIL` (`email`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

#####################################
#           SEED DATA               #
#####################################
#################  INSERTING USER  #############################
INSERT INTO `user` (`version`,`email`,`password`, `password_expired`,
                    `account_locked`, `account_expired`,`enabled`)
   VALUES (0, 'fname.lname@company.com', 'password', 0, 0, 0, 1);
