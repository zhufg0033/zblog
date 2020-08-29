package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoComment;
import com.zblog.sharedb.entity.MtoUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface MtoUserDao {
    @Delete({
        "delete from mto_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_user (id, username, ",
        "name, avatar, email, ",
        "password, status, ",
        "created, updated, ",
        "last_login, gender, ",
        "role_id, comments, ",
        "posts, signature, ",
        "sign_db)",
        "values (#{id,jdbcType=BIGINT}, #{username,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{avatar,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
        "#{created,jdbcType=TIMESTAMP}, #{updated,jdbcType=TIMESTAMP}, ",
        "#{lastLogin,jdbcType=TIMESTAMP}, #{gender,jdbcType=INTEGER}, ",
        "#{roleId,jdbcType=INTEGER}, #{comments,jdbcType=INTEGER}, ",
        "#{posts,jdbcType=INTEGER}, #{signature,jdbcType=VARCHAR}, ",
        "#{signDb,jdbcType=VARCHAR})"
    })
    int insert(MtoUser record);

    @Select({
        "select",
        "id, username, name, avatar, email, password, status, created, updated, last_login, ",
        "gender, role_id, comments, posts, signature, sign_db",
        "from mto_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_login", property="lastLogin", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gender", property="gender", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
        @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
        @Result(column="signature", property="signature", jdbcType=JdbcType.VARCHAR),
        @Result(column="sign_db", property="signDb", jdbcType=JdbcType.VARCHAR)
    })
    MtoUser selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, username, name, avatar, email, password, status, created, updated, last_login, ",
        "gender, role_id, comments, posts, signature, sign_db",
        "from mto_user"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_login", property="lastLogin", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gender", property="gender", jdbcType=JdbcType.INTEGER),
        @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
        @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
        @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
        @Result(column="signature", property="signature", jdbcType=JdbcType.VARCHAR),
        @Result(column="sign_db", property="signDb", jdbcType=JdbcType.VARCHAR)
    })
    List<MtoUser> selectAll();

    @Update({
        "update mto_user",
        "set username = #{username,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "avatar = #{avatar,jdbcType=VARCHAR},",
          "email = #{email,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "created = #{created,jdbcType=TIMESTAMP},",
          "updated = #{updated,jdbcType=TIMESTAMP},",
          "last_login = #{lastLogin,jdbcType=TIMESTAMP},",
          "gender = #{gender,jdbcType=INTEGER},",
          "role_id = #{roleId,jdbcType=INTEGER},",
          "comments = #{comments,jdbcType=INTEGER},",
          "posts = #{posts,jdbcType=INTEGER},",
          "signature = #{signature,jdbcType=VARCHAR},",
          "sign_db = #{signDb,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoUser record);

    @Select({
            "select",
            "id, username, name, avatar, email, password, status, created, updated, last_login, ",
            "gender, role_id, comments, posts, signature, sign_db",
            "from mto_user",
            "where username = #{userName,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_login", property="lastLogin", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gender", property="gender", jdbcType=JdbcType.INTEGER),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
            @Result(column="signature", property="signature", jdbcType=JdbcType.VARCHAR),
            @Result(column="sign_db", property="signDb", jdbcType=JdbcType.VARCHAR)
    })
    MtoUser findByUsername(String userName);

    @Select({
            "select",
            "id, username, name, avatar, email, password, status, created, updated, last_login, ",
            "gender, role_id, comments, posts, signature, sign_db",
            "from mto_user",
            "where email = #{email,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_login", property="lastLogin", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gender", property="gender", jdbcType=JdbcType.INTEGER),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
            @Result(column="signature", property="signature", jdbcType=JdbcType.VARCHAR),
            @Result(column="sign_db", property="signDb", jdbcType=JdbcType.VARCHAR)
    })
    MtoUser findByEmail(String email);


    @Select({
            "<script>",
            "select",
            "id, username, name, avatar, email, password, status, created, updated, last_login, ",
            "gender, role_id, comments, posts, signature, sign_db",
            "from mto_user",
            "where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="avatar", property="avatar", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_login", property="lastLogin", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gender", property="gender", jdbcType=JdbcType.INTEGER),
            @Result(column="role_id", property="roleId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
            @Result(column="signature", property="signature", jdbcType=JdbcType.VARCHAR),
            @Result(column="sign_db", property="signDb", jdbcType=JdbcType.VARCHAR)
    })
    List<MtoUser> findAllById(@Param("ids") List<Long> ids);

//    @Update({
//          "<script>",
//            "update mto_user set posts = posts+#{increment,JdbcType.INTEGER} where id = #{id,jdbcType=BIGINT}",
//          "</script>"
//    })
//    int updatePosts(@Param("id") long id, @Param("increment") int increment);
//
//    @Update({
//            "<script>",
//            "update mto_user set comments = comments+#{increment,JdbcType.INTEGER} where id in ",
//            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
//            "#{item}",
//            "</foreach>",
//            "</script>"
//    })
//    int updateComments(@Param("ids") Collection<Long> ids, @Param("increment") int increment);
}