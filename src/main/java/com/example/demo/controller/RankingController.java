package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    @Autowired
    private RankingService rankingService;
    
    @GetMapping("/borrow")
    public Result borrowRanking(){
        return Result.success(rankingService.borrowTop10());
    }
    
    @GetMapping("/score")
    public Result scoreRanking(){
        return Result.success(rankingService.ratingTop10());
    }
}

