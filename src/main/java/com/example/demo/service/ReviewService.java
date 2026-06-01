package com.example.demo.service;

import com.example.demo.entity.Review;
import com.example.demo.entity.ReviewLike;
import com.example.demo.entity.ReviewReplay;
import com.example.demo.entity.User;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.ReviewLikeMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.mapper.ReviewReplayMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final BookMapper bookMapper;
    private final ReviewLikeMapper reviewLikeMapper;
    private final ReviewReplayMapper reviewReplayMapper;
    private final UserMapper userMapper;

    public ReviewService(ReviewMapper reviewMapper,
                         BookMapper bookMapper,
                         ReviewLikeMapper reviewLikeMapper,
                         ReviewReplayMapper reviewReplayMapper,
                         UserMapper userMapper) {
        this.reviewMapper = reviewMapper;
        this.bookMapper = bookMapper;
        this.reviewLikeMapper = reviewLikeMapper;
        this.reviewReplayMapper = reviewReplayMapper;
        this.userMapper = userMapper;
    }

    /** 新增书评 */
    @Transactional
    public Review add(Review review) {
        if (review == null || review.getBookId() == null || review.getUserId() == null) {
            throw new IllegalArgumentException("bookId、userId 不能为空");
        }
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        if (review.getRating() == null) {
            throw new IllegalArgumentException("评分不能为空");
        }

        review.setCreateTime(LocalDateTime.now());
        review.setLikeCount(0);

        reviewMapper.insert(review);
        refreshBookRating(review.getBookId());
        return review;
    }

    /** 修改书评（只改 content、rating） */
    @Transactional
    public Review update(Review incoming) {
        if (incoming == null || incoming.getReviewId() == null) {
            throw new IllegalArgumentException("reviewId 不能为空");
        }

        Review existing = reviewMapper.findById(incoming.getReviewId());
        if (existing == null) {
            throw new IllegalArgumentException("书评不存在");
        }

        if (incoming.getContent() != null) {
            existing.setContent(incoming.getContent());
        }
        if (incoming.getRating() != null) {
            existing.setRating(incoming.getRating());
        }

        reviewMapper.update(existing);
        refreshBookRating(existing.getBookId());
        return existing;
    }

    /** 删除书评 */
    @Transactional
    public boolean delete(Integer reviewId) {
        if (reviewId == null) {
            return false;
        }
        Review existing = reviewMapper.findById(reviewId);
        if (existing == null) {
            return false;
        }

        reviewMapper.deleteById(reviewId);
        refreshBookRating(existing.getBookId());
        return true;
    }

    /** 某本书下的书评列表（填充用户名） */
    public List<Review> listByBookId(Integer bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("bookId 不能为空");
        }
        List<Review> reviews = reviewMapper.findByBookId(bookId);
        fillUsername(reviews);
        return reviews;
    }

    /** 某用户的书评列表（填充用户名） */
    public List<Review> listByUserId(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        List<Review> reviews = reviewMapper.findByUserId(userId);
        fillUsername(reviews);
        return reviews;
    }

    private void fillUsername(List<Review> reviews) {
        for (Review review : reviews) {
            if (review.getUserId() != null) {
                User user = userMapper.findById(review.getUserId());
                if (user != null) {
                    review.setUsername(user.getUserName());
                }
            }
        }
    }

    /**
     * 点赞：插入 review_like + review.like_count +1
     * 需要当前用户 id，这里先用参数传入；以后可从登录态取
     */
    @Transactional
    public boolean like(Integer reviewId, Integer userId) {
        if (reviewId == null || userId == null) {
            throw new IllegalArgumentException("reviewId、userId 不能为空");
        }
        Review review = reviewMapper.findById(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("书评不存在");
        }

        ReviewLike old = reviewLikeMapper.findByReviewAndUser(reviewId, userId);
        if (old != null) {
            throw new IllegalArgumentException("您已经点过赞了");
        }

        ReviewLike like = new ReviewLike();
        like.setReviewId(reviewId);
        like.setUserId(userId);
        like.setCreateTime(LocalDateTime.now());
        reviewLikeMapper.insert(like);

        reviewMapper.incrementLikeCount(reviewId);
        return true;
    }

    /** 回复某条书评（楼中楼） */
    @Transactional
    public ReviewReplay reply(Integer reviewId, ReviewReplay replay) {
        if (reviewId == null || replay == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        Review review = reviewMapper.findById(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("书评不存在");
        }
        if (replay.getUserId() == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        if (replay.getContent() == null || replay.getContent().isBlank()) {
            throw new IllegalArgumentException("回复内容不能为空");
        }

        replay.setReviewId(reviewId);
        replay.setCreateTime(LocalDateTime.now());
        reviewReplayMapper.insert(replay);
        return replay;
    }

    /** 重算并更新 book.rating */
    private void refreshBookRating(Integer bookId) {
        BigDecimal avg = reviewMapper.avgRatingByBookId(bookId);
        if (avg == null) {
            avg = BigDecimal.ZERO;
        } else {
            avg = avg.setScale(1, RoundingMode.HALF_UP);
        }
        bookMapper.updateRating(bookId, avg);
    }
}
