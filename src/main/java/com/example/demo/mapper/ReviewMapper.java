package com.example.demo.mapper;

import com.example.demo.entity.Review;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ReviewMapper {

    @Select("SELECT COUNT(*) FROM review")
    int countReview();

    @Select("SELECT AVG(rating) FROM review WHERE book_id = #{bookId}")
    BigDecimal avgRatingByBookId(Integer bookId);

    @Insert("INSERT INTO review(book_id, user_id, content, rating, create_time, like_count) " +
            "VALUES(#{bookId}, #{userId}, #{content}, #{rating}, #{createTime}, #{likeCount})")
    @Options(useGeneratedKeys = true, keyProperty = "reviewId")
    int insert(Review review);

    @Select("SELECT * FROM review WHERE review_id = #{reviewId}")
    Review findById(Integer reviewId);

    @Select("SELECT * FROM review WHERE book_id = #{bookId} ORDER BY create_time DESC")
    List<Review> findByBookId(Integer bookId);

    @Select("SELECT * FROM review WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Review> findByUserId(Integer userId);

    @Update("UPDATE review SET content=#{content}, rating=#{rating} WHERE review_id=#{reviewId}")
    int update(Review review);

    @Delete("DELETE FROM review WHERE review_id = #{reviewId}")
    int deleteById(Integer reviewId);

    @Update("UPDATE review SET like_count = IFNULL(like_count, 0) + 1 WHERE review_id = #{reviewId}")
    int incrementLikeCount(Integer reviewId);
}