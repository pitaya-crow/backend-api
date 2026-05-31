package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE user_name = #{userName} AND password = #{password}")
    User login(@Param("userName") String userName, @Param("password") String password);
    
    @Insert("INSERT INTO user(person_id, user_name, password, user_type_id, status) " +
            "VALUES ((SELECT IFNULL(MAX(person_id), 0) + 1 FROM user t), #{userName}, #{password}, 1, 1)")
    int register(@Param("userName") String userName,@Param("password") String password);
    
    @Select("SELECT COUNT(*) FROM user")
    int countUsers();
    
    @Select("SELECT * FROM user ORDER BY personId DESC LIMIT 10")
    List<User> getActiveUsers();
    
    @Select("SELECT * FROM user LIMIT #{pageSize} OFFSET #{offset}")
    List<User> findUserByPage(@Param("pageSize") int pageSize,@Param("offset") int offset);
    
    @Select("SELECT COUNT(*) FROM user WHERE status=1")
    int countActiveUsers();
    
    @Update("UPDATE user SET status=#{status} WHERE personId=#{personId} ")
    int updateUserStatus(@Param("status") int status,@Param("personId") long personId);
    
    @Update("UPDATE user SET user_name=#{userName}, user_type_id=#{userTypeId}, status=#{status} WHERE personId=#{personId}")
    int updateUser(User user);
    
    @Select("SELECT * FROM user WHERE personId = #{personId}")
    User findById(@Param("personId") Integer personId);
}
