package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.BorrowService;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/info")
    public Result getUserInfo(@RequestParam Integer userId){
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 返回时清除密码
        user.setPassword(null);
        return Result.success(user);
    }

    @GetMapping("/borrows")
    public Result getBorrowList(@RequestParam Integer userId){
        return Result.success(borrowService.listByUserId(userId));
    }

    @GetMapping("/reviews")
    public Result getMyReviews(@RequestParam Integer userId){
        return Result.success(reviewService.listByUserId(userId));
    }

    @PutMapping("/info")
    public Result updateInfo(@RequestBody User user){
        User updatedUser = userService.updateInfo(user);
        return Result.success(updatedUser);
    }
}
