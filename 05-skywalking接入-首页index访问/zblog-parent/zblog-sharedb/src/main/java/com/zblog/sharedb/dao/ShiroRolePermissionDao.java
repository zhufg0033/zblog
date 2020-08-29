package com.zblog.sharedb.dao;

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
}