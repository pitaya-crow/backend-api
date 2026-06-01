package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowRecord;
import com.example.demo.entity.User;
import com.example.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Autowired
    private ReviewReplayMapper reviewReplayMapper;
    @Autowired
    private ReviewMapper reviewMapper;

    public Map<String,Object> getDashboard(){
        Map<String,Object> data=new HashMap<>();
        data.put("bookCount",bookMapper.countBooks());
        data.put("userCount",userMapper.countUsers());
        data.put("borrowRecordCount",borrowRecordMapper.countBorrowRecords());
        data.put("reviewCount",reviewMapper.countReview());
        data.put("overdueCount",borrowRecordMapper.countOverdue());
        return data;
    }
    public List<BorrowRecord> getRecentBorrows(){
        return borrowRecordMapper.findRecentBorrows();
    }
public List<Book> getHotBooks(){
        return bookMapper.getHotBooks();
}
public List<User> getActiveUsers(){
        return userMapper.getActiveUsers();
}
public List<User> listUsers(int page,int pageSize){
int offset=(page-1)*pageSize;
return userMapper.findUserByPage(pageSize,offset);
}
public List<User> searchUsers(String keyword){
    return userMapper.searchByKeyword(keyword);
}
public Map<String,Object> getUserStats(){
        Map<String,Object> data= new HashMap<>();
        data.put("totalUsers",userMapper.countUsers());
        data.put("activeUsers",userMapper.countActiveUsers());
    return data;
    }
    public boolean toggleUserStatus(long personId, int status) {
        return userMapper.updateUserStatus(status, personId) > 0;
    }
public Map<String,Object> getBorrowStats(){
        Map<String,Object> data=new HashMap<>();
        int total= borrowRecordMapper.countTotalBorrows();
        int returned= borrowRecordMapper.countReturnedBooks();
        int thisMonth= borrowRecordMapper.countThisMonthBorrows();
        data.put("totalBorrows",total);
        data.put("returnedBooks",returned);
        data.put("thisMonthBooks",thisMonth);
        return data;
}
public List<Map<String,Object>> getBorrowTrend(){
        return borrowRecordMapper.findBorrowTrends();
}
    public List<Map<String, Object>> getCategoryStats() {
        return bookMapper.findCategoryStats();
    }

    /** 获取逾期记录（含用户名、书名、欠费） */
    public List<Map<String, Object>> getOverdueRecords() {
        List<BorrowRecord> records = borrowRecordMapper.findOverdueRecords();
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        double overdueRate = 0.1; // 每天0.1元
        for (BorrowRecord record : records) {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", record.getId());
            map.put("bookId", record.getBookId());
            map.put("userId", record.getUserId());
            map.put("borrowedAt", record.getBorrowedAt());
            map.put("dueAt", record.getDueAt());
            // 查询用户名
            User user = userMapper.findById(record.getUserId());
            map.put("userName", user != null ? user.getUserName() : "未知");
            // 查询书名
            Book book = bookMapper.findById(record.getBookId());
            map.put("bookTitle", book != null ? book.getTitle() : "未知");
            // 计算逾期天数和欠费
            if (record.getDueAt() != null) {
                long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                        record.getDueAt(), java.time.LocalDateTime.now());
                if (overdueDays < 0) overdueDays = 0;
                double fee = overdueDays * overdueRate;
                map.put("overdueDays", overdueDays);
                map.put("fee", Math.round(fee * 10.0) / 10.0);
            } else {
                map.put("overdueDays", 0);
                map.put("fee", 0.0);
            }
            result.add(map);
        }
        return result;
    }
}
