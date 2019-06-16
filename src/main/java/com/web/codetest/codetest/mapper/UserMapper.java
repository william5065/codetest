package com.web.codetest.codetest.mapper;

import com.web.codetest.codetest.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    @Delete("delete from user where token=#{token}")
    void deleteToken(@Param("token") String token);
}
