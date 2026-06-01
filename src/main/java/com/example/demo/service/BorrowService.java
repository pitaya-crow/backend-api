package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BorrowRecord;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BorrowRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowService {

    private final BorrowRecordMapper borrowRecordMapper;
    private final BookMapper bookMapper;

    /** 借阅天数，可按你们规则改 */
    private static final int BORROW_DAYS = 30;

    public BorrowService(BorrowRecordMapper borrowRecordMapper, BookMapper bookMapper) {
        this.borrowRecordMapper = borrowRecordMapper;
        this.bookMapper = bookMapper;
    }

    /**
     * 借书：body 里传 bookId、userId
     */
    @Transactional
    public BorrowRecord borrow(BorrowRecord borrow) {
        if (borrow == null || borrow.getBookId() == null || borrow.getUserId() == null) {
            throw new IllegalArgumentException("bookId 和 userId 不能为空");
        }

        Book book = bookMapper.findById(borrow.getBookId());
        if (book == null) {
            throw new IllegalArgumentException("图书不存在");
        }
        if (book.getAvailable() == null || book.getAvailable() <= 0) {
            throw new IllegalArgumentException("该书暂无可借册数");
        }

        BorrowRecord active = borrowRecordMapper.findActiveBorrow(borrow.getBookId(), borrow.getUserId());
        if (active != null) {
            throw new IllegalArgumentException("您已有该书的未还记录");
        }

        LocalDateTime now = LocalDateTime.now();
        borrow.setBorrowedAt(now);
        borrow.setDueAt(now.plusDays(BORROW_DAYS));
        borrow.setReturnedAt(null);
        borrow.setStatus(1);          // 1=借出中，按你们约定可改
        borrow.setRenewCount(0);

        borrowRecordMapper.insert(borrow);

        book.setAvailable(book.getAvailable() - 1);
        int count = book.getBorrowCount() == null ? 0 : book.getBorrowCount();
        book.setBorrowCount(count + 1);
        bookMapper.update(book);

        return borrow;
    }

    /**
     * 还书：body 里传借阅记录 id
     */
    @Transactional
    public BorrowRecord returnBook(BorrowRecord borrow) {
        if (borrow == null || borrow.getId() == null) {
            throw new IllegalArgumentException("借阅记录 id 不能为空");
        }

        BorrowRecord existing = borrowRecordMapper.findById(borrow.getId());
        if (existing == null) {
            throw new IllegalArgumentException("借阅记录不存在");
        }
        if (existing.getReturnedAt() != null) {
            throw new IllegalArgumentException("该记录已归还");
        }

        existing.setReturnedAt(LocalDateTime.now());
        existing.setStatus(0);        // 0=已还
        borrowRecordMapper.updateReturn(existing);

        Book book = bookMapper.findById(existing.getBookId());
        if (book != null && book.getAvailable() != null) {
            book.setAvailable(book.getAvailable() + 1);
            bookMapper.update(book);
        }

        return existing;
    }

    /** 借阅记录列表（管理端等） */
    public List<BorrowRecord> list() {
        return borrowRecordMapper.findAll();
    }

    /** 按用户ID查借阅记录（含书名） */
    public List<Map<String, Object>> listByUserId(Integer userId) {
        List<BorrowRecord> records = borrowRecordMapper.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (BorrowRecord record : records) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", record.getId());
            map.put("bookId", record.getBookId());
            map.put("userId", record.getUserId());
            map.put("borrowedAt", record.getBorrowedAt());
            map.put("dueAt", record.getDueAt());
            map.put("returnedAt", record.getReturnedAt());
            map.put("status", record.getStatus());
            // 查询书名
            Book book = bookMapper.findById(record.getBookId());
            map.put("bookTitle", book != null ? book.getTitle() : "未知");
            result.add(map);
        }
        return result;
    }

    /** 按 id 查一条 */
    public BorrowRecord getById(Integer id) {
        if (id == null) {
            return null;
        }
        return borrowRecordMapper.findById(id);
    }
}