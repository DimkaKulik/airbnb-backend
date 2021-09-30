CREATE TABLE `booking`
(
    `id`          int       NOT NULL AUTO_INCREMENT,
    `products_id` int       NOT NULL,
    `users_id`    int       NOT NULL,
    `start`       timestamp NOT NULL,
    `finish`      timestamp NOT NULL,
    `description` varchar(255)       DEFAULT NULL,
    `record_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `one_product_has_one_client_UNIQUE` (`products_id`,`start`),
    KEY           `fk_booking` (`products_id`,`users_id`),
    CONSTRAINT `fk_booking_products` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`),
    CONSTRAINT `fk_booking_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
);

