package com.zblog.sharedb.dao.subtreasury;

import com.zblog.sharedb.entity.MtoUserOauth;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
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
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
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

    @Select({
            "select",
            "id, user_id, access_token, expire_in, oauth_code, oauth_type, oauth_user_id, ",
            "refresh_token",
            "from mto_user_oauth",
            "where access_token = #{oauth_token,jdbcType=VARCHAR}"
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
    MtoUserOauth findByAccessToken(String oauth_token);

    @Select({
            "select",
            "id, user_id, access_token, expire_in, oauth_code, oauth_type, oauth_user_id, ",
            "refresh_token",
            "from mto_user_oauth",
            "where user_id = #{userId,jdbcType=BIGINT}"
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
    MtoUserOauth findByUserId(long userId);

    @Select({
            "select",
            "id, user_id, access_token, expire_in, oauth_code, oauth_type, oauth_user_id, ",
            "refresh_token",
            "from mto_user_oauth",
            "where oauth_user_id = #{oauthUserId,jdbcType=VARCHAR}"
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
    MtoUserOauth findByOauthUserId(String oauthUserId);
}