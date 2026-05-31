package com.example.demo.mapper;

import com.example.demo.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookMapper {
    @Select("SELECT * FROM book")
    List<Book> findALL();

    @Select("SELECT id,title, author, isbn, total, available, create_time, rating ,category,borrow_count FROM book WHERE id = #{id}")
    Book findById(Integer id);


    @Select("SELECT * FROM book ORDER BY borrow_count DESC LIMIT 10")
    List<Book> findBorrowTop10();

    @Select("SELECT * FROM book ORDER BY borrow_count DESC LIMIT 10")
    List<Book> getHotBooks();

    @Select("SELECT id, title, author, borrow_count FROM book ORDER BY borrow_count DESC LIMIT 8")
    List<Book> findHotBooksTop8();

    @Select("SELECT * FROM book ORDER BY rating DESC LIMIT 10")
    List<Book> findRatingTop10();

    @Insert("INSERT INTO book(title, author, isbn, total, available, create_time,category,borrow_count) " +
            "VALUES(#{title}, #{author}, #{isbn}, #{total}, #{available}, #{createTime}, #{category},#{borrowCount}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Book book);

    @Update("UPDATE book SET title=#{title}, author=#{author}, isbn=#{isbn}, total=#{total}, " +
            "available=#{available}, create_time=#{createTime}, category=#{category},borrow_count=#{borrowCount} WHERE id=#{id}")
    int update(Book book);

    @Delete("DELETE FROM book WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT COUNT(*) FROM book")
    int countBooks();

    @Select("SELECT category, COUNT(*) AS count FROM book GROUP BY category ORDER BY count DESC")
    List<Map<String, Object>> findCategoryStats();

    @Update("UPDATE book SET rating = #{rating} WHERE id = #{id}")
    int updateRating(@Param("id") Integer id, @Param("rating") BigDecimal rating);

}


