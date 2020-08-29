package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoUserOauth;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface MtoUserOauthDao {
    @Delete({
        "delete from mto_user_oauth",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_user_oauth (id, user_id, ",
        "access_token, expire_in, ",
        "oauth_code, oauth_type, ",
        "oauth_user_id, refresh_token)",
        "values (#{id,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, ",
        "#{accessToken,jdbcType=VARCHAR}, #{expireIn,jdbcType=VARCHAR}, ",
        "#{oauthCode,jdbcType=VARCHAR}, #{oauthType,jdbcType=INTEGER}, ",
        "#{oauthUserId,jdbcType=VARCHAR}, #{refreshToken,jdbcType=VARCHAR})"
    })
    int insert(MtoUserOauth record);

    @Select({
        "select",
        "id, user_id, access_token, expire_in, oauth_code, oauth_type, oauth_user_id, ",
        "refresh_token",
        "from mto_user_oauth",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="access_token", property="accessToken", jdbcType=JdbcType.VARCHAR),
        @Result(column="expire_in", property="expireIn", jdbcType=JdbcType.VARCHAR),
        @Result(column="oauth_code", property="oauthCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="oauth_type", property="oauthType", jdbcType=JdbcType.INTEGER),
        @Result(column="oauth_user_id", property="oauthUserId", jdbcType=JdbcType.VARCHAR),
        @Result(column="refresh_token", property="refreshToken", jdbcType=JdbcType.VARCHAR)
    })
    MtoUserOauth selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, user_id, access_token, expire_in, oauth_code, oauth_type, oauth_user_id, ",
        "refresh_token",
        "from mto_user_oauth"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="access_token", property="accessToken", jdbcType=JdbcType.VARCHAR),
        @Result(column="expire_in", property="expireIn", jdbcType=JdbcType.VARCHAR),
        @Result(column="oauth_code", property="oauthCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="oauth_type", property="oauthType", jdbcType=JdbcType.INTEGER),
        @Result(column="oauth_user_id", property="oauthUserId", jdbcType=JdbcType.VARCHAR),
        @Result(column="refresh_token", property="refreshToken", jdbcType=JdbcType.VARCHAR)
    })
    List<MtoUserOauth> selectAll();

    @Update({
        "update mto_user_oauth",
        "set user_id = #{userId,jdbcType=BIGINT},",
          "access_token = #{accessToken,jdbcType=VARCHAR},",
          "expire_in = #{expireIn,jdbcType=VARCHAR},",
          "oauth_code = #{oauthCode,jdbcType=VARCHAR},",
          "oauth_type = #{oauthType,jdbcType=INTEGER},",
          "oauth_user_id = #{oauthUserId,jdbcType=VARCHAR},",
          "refresh_token = #{refreshToken,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoUserOauth record);
}