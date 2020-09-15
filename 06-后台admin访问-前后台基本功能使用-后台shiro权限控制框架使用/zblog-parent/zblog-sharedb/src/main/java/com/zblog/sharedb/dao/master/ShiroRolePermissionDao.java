package com.zblog.sharedb.dao.master;

import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.entity.ShiroRolePermission;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ShiroRolePermissionDao {
    @Delete({
        "delete from shiro_role_permission",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into shiro_role_permission (id, permission_id, ",
        "role_id)",
        "values (#{id,jdbcType=BIGINT}, #{permissionId,jdbcType=BIGINT}, ",
        "#{roleId,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(ShiroRolePermission record);

    @Select({
        "select",
        "id, permission_id, role_id",
        "from shiro_role_permission",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.BIGINT),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT)
    })
    ShiroRolePermission selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, permission_id, role_id",
        "from shiro_role_permission"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.BIGINT),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT)
    })
    List<ShiroRolePermission> selectAll();

    @Update({
        "update shiro_role_permission",
        "set permission_id = #{permissionId,jdbcType=BIGINT},",
          "role_id = #{roleId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ShiroRolePermission record);

    @Select({
            "select",
            "id, permission_id, role_id",
            "from shiro_role_permission",
            "where role_id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="permission_id", property="permissionId", jdbcType=JdbcType.BIGINT),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.BIGINT)
    })
    List<ShiroRolePermission> findAllByRoleId(Long id);

    @Delete({
            "delete from shiro_role_permission",
            "where role_id = #{roleId,jdbcType=BIGINT}"
    })
    void deleteByRoleId(Long roleId);

    @Insert({
            "<script>",
            "insert into shiro_role_permission (id, permission_id, ",
            "role_id)",
            "values " ,
            "<foreach item='item' index='index' collection='rps' open='(' separator=',' close=')'>",
            "(#{item.id,jdbcType=BIGINT}, #{item.permissionId,jdbcType=BIGINT}, ",
            "#{item.roleId,jdbcType=BIGINT})",
            "</foreach>",
            "</script>"
    })
    void insertAll(@Param("rps") List<ShiroRolePermission> rps);

}