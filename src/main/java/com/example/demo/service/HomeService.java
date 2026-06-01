package com.example.demo.service;


import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BorrowRecordMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Autowired
    private UserMapper userMapper;
    public Map<String,Object> getStats(){
        Map<String,Object> data=new HashMap<>();
        data.put("bookCount",bookMapper.countBooks());
        data.put("borrowCount",borrowRecordMapper.countTotalBorrows());
        int totalUsers = userMapper.countUsers();
        data.put("userCount", Math.max(totalUsers - 1, 0)); // 排除管理员
        data.put("countUsers", totalUsers);
        return data;
    }

    public List<Map<String, Object>> getHotBooks() {
        return bookMapper.findHotBooksTop8().stream().map(book -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", book.getId());
            map.put("title", book.getTitle());
            map.put("author", book.getAuthor());
            map.put("borrowCount", book.getBorrowCount());
            map.put("rating", book.getRating());
            return map;
        }).collect(Collectors.toList());
    }
}
