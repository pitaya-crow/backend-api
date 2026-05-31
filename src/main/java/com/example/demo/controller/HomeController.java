package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private HomeService homeService;
    @GetMapping("/stats")
    public Result getStats(){
        return Result.success(homeService.getStats());
    }

    @GetMapping("/hot-books")
    public Result getHotBooks(){
        return Result.success(homeService.getHotBooks());
    }
}
