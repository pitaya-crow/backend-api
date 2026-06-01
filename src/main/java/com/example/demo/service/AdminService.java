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

}
