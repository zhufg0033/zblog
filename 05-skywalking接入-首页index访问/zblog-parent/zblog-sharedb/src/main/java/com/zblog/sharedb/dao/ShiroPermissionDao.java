package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.ShiroPermission;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ShiroPermissionDao {
    @Delete({
        "delete from shiro_permission",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into shiro_permission (id, description, ",
        "name, parent_id, version, ",
        "weight)",
        "values (#{id,jdbcType=BIGINT}, #{description,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{parentId,jdbcType=BIGINT}, #{version,jdbcType=INTEGER}, ",
        "#{weight,jdbcType=INTEGER})"
    })
    int insert(ShiroPermission record);

    @Select({
        "select",
        "id, description, name, parent_id, version, weight",
        "from shiro_permission",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.BIGINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    ShiroPermission selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, description, name, parent_id, version, weight",
        "from shiro_permission"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="parent_id", property="parentId", jdbcType=JdbcType.BIGINT),
        @Result(column="version", property="version", jdbcType=JdbcType.INTEGER),
        @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<ShiroPermission> selectAll();

    @Update({
        "update shiro_permission",
        "set description = #{description,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "parent_id = #{parentId,jdbcType=BIGINT},",
          "version = #{version,jdbcType=INTEGER},",
          "weight = #{weight,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ShiroPermission record);
}