package com.example.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Data
    public class Review {

        private Integer reviewId;
        private Integer bookId;
        private Integer userId;
        private String content;

        /** 对应 decimal(2,1)，用 BigDecimal 更贴切 */
        private BigDecimal rating;

        private LocalDateTime createTime;
        private Integer likeCount;

        /** 非数据库字段，由 Service 层填充（MyBatis 自动忽略无对应列的字段） */
        private String username;
    }

