package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.ShiroRolePermission;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

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