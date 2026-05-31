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
    
    @GetMapping("/borrows")
    public Result getBorrowList(@RequestParam Integer userId){
        return Result.success(borrowService.list());
    }
    
    @GetMapping("/reviews")
    public Result getMyReviews(@RequestParam Integer userId){
        return Result.success(reviewService.listByBookId(null));
    }
    
    @PutMapping("/info")
    public Result updateInfo(@RequestBody User user){
        User updatedUser = userService.updateInfo(user);
        return Result.success(updatedUser);
    }
}
