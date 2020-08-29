package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.ShiroUserRole;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ShiroUserRoleDao {
    @Delete({
        "delete from shiro_user_role",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into shiro_user_role (id, role_id, ",
        "user_id)",
        "values (#{id,jdbcType=BIGINT}, #{roleId,jdbcType=BIGINT}, ",
        "#{userId,jdbcType=BIGINT})"
    })
    int insert(ShiroUserRole record);

    @Select({
        "select",
        "id, role_id, user_id",
        "from shiro_user_role",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    ShiroUserRole selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, role_id, user_id",
        "from shiro_user_role"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<ShiroUserRole> selectAll();

    @Update({
        "update shiro_user_role",
        "set role_id = #{roleId,jdbcType=BIGINT},",
          "user_id = #{userId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ShiroUserRole record);
}