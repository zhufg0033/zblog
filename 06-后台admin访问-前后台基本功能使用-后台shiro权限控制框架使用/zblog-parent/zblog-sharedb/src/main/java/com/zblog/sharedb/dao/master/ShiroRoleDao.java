package com.zblog.sharedb.dao.master;

import com.zblog.sharedb.entity.ShiroRole;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Mapper
public interface ShiroRoleDao {
    @Delete({
        "delete from shiro_role",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into shiro_role (id, description, ",
        "name, status)",
        "values (#{id,jdbcType=BIGINT}, #{description,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(ShiroRole record);

    @Select({
        "select",
        "id, description, name, status",
        "from shiro_role",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    ShiroRole selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, description, name, status",
        "from shiro_role"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<ShiroRole> selectAll();

    @Update({
        "update shiro_role",
        "set description = #{description,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ShiroRole record);

    @Select({
            "<script>",
            "select",
            "id, description, name, status",
            "from shiro_role",
            "<if test=\"ids != null and ids.size > 0\">",
            " where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<ShiroRole> findAllByIds(@Param("ids") List<Long> ids);

    @Select({
            "<script>",
            "select",
            "id, description, name, status",
            "from shiro_role",
            "<if test=\"name != null \">",
            "where name like #{name,jdbcType=VARCHAR}",
            "</if>",
            "</script>"

    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<ShiroRole> findAll(String name);

    @Select({
            "select",
            "id, description, name, status",
            "from shiro_role",
            "where status = #{statusNormal,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<ShiroRole> findAllByStatus(int statusNormal);
}