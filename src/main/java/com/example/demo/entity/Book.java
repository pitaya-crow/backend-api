package com.example.demo.entity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.ToString;
@Data
public class Book {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer total;
        private Integer available;
        private LocalDateTime createTime;
        private Integer borrowCount;
        private Double rating;
        private String category;
        private String publisher;
        private String publishDate;
        private String description;
        private String coverUrl;
}
      


      
