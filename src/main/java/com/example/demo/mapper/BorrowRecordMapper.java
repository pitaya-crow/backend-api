
package com.example.demo.mapper;

import com.example.demo.entity.BorrowRecord;
import org.apache.ibatis.annotations.*;

        import java.util.List;
import java.util.Map;

@Mapper
public interface BorrowRecordMapper {

    // ========== 借还书业务（需要新增的） ==========
    @Select("SELECT COUNT(*) FROM borrow_record")
    int countBorrowRecords();

    @Insert("INSERT INTO borrow_record(book_id, user_id, borrowed_at, due_at, status, renew_count) " +
            "VALUES(#{bookId}, #{userId}, #{borrowedAt}, #{dueAt}, #{status}, #{renewCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BorrowRecord record);

    @Select("SELECT * FROM borrow_record WHERE id = #{id}")
    BorrowRecord findById(Integer id);

    @Select("SELECT * FROM borrow_record WHERE user_id = #{userId} ORDER BY borrowed_at DESC")
    List<BorrowRecord> findByUserId(Integer userId);

    @Select("SELECT * FROM borrow_record ORDER BY borrowed_at DESC")
    List<BorrowRecord> findAll();

    /** 未还记录：还书前检查用 */
    @Select("SELECT * FROM borrow_record WHERE book_id = #{bookId} AND user_id = #{userId} " +
            "AND returned_at IS NULL LIMIT 1")
    BorrowRecord findActiveBorrow(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

    @Update("UPDATE borrow_record SET returned_at = #{returnedAt}, status = #{status} WHERE id = #{id}")
    int updateReturn(BorrowRecord record);

    // ========== 管理端统计（修正后的） ==========

    @Select("SELECT COUNT(*) FROM borrow_record")
    int countTotalBorrows();

    @Select("SELECT COUNT(*) FROM borrow_record WHERE returned_at IS NOT NULL")
    int countReturnedBooks();

    @Select("SELECT COUNT(*) FROM borrow_record " +
            "WHERE YEAR(borrowed_at) = YEAR(CURDATE()) AND MONTH(borrowed_at) = MONTH(CURDATE())")
    int countThisMonthBorrows();

    @Select("SELECT * FROM borrow_record ORDER BY borrowed_at DESC LIMIT 10")
    List<BorrowRecord> findRecentBorrows();

    /** 逾期记录：已借出、超过应还日期、未归还 */
    @Select("SELECT * FROM borrow_record WHERE status = 1 AND due_at < NOW() AND returned_at IS NULL ORDER BY due_at ASC")
    List<BorrowRecord> findOverdueRecords();

    /** 逾期数量 */
    @Select("SELECT COUNT(*) FROM borrow_record WHERE status = 1 AND due_at < NOW() AND returned_at IS NULL")
    int countOverdue();

    /**
     * 借阅趋势（按月）
     * date=月份, borrowCount=当月借出笔数, returnCount=当月借出且已还笔数（课设简化口径）
     */
    @Select("SELECT DATE_FORMAT(borrowed_at, '%Y-%m') AS date, " +
            "COUNT(*) AS borrowCount, " +
            "SUM(CASE WHEN returned_at IS NOT NULL THEN 1 ELSE 0 END) AS returnCount " +
            "FROM borrow_record " +
            "WHERE borrowed_at IS NOT NULL " +
            "GROUP BY DATE_FORMAT(borrowed_at, '%Y-%m') " +
            "ORDER BY date ASC")
    List<Map<String, Object>> findBorrowTrends();
}
