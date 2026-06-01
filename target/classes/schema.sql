CREATE TABLE IF NOT EXISTS `borrow_record` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `book_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `borrowed_at` DATETIME DEFAULT NULL,
    `due_at` DATETIME DEFAULT NULL,
    `returned_at` DATETIME DEFAULT NULL,
    `status` INT DEFAULT 1,
    `renew_count` INT DEFAULT 0,
    `last_renewed_at` DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
