package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoMessage;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface MtoMessageDao {
    @Delete({
        "delete from mto_message",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_message (id, created, ",
        "event, from_id, post_id, ",
        "status, user_id)",
        "values (#{id,jdbcType=BIGINT}, #{created,jdbcType=TIMESTAMP}, ",
        "#{event,jdbcType=INTEGER}, #{fromId,jdbcType=BIGINT}, #{postId,jdbcType=BIGINT}, ",
        "#{status,jdbcType=INTEGER}, #{userId,jdbcType=BIGINT})"
    })
    int insert(MtoMessage record);

    @Select({
        "select",
        "id, created, event, from_id, post_id, status, user_id",
        "from mto_message",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="event", property="event", jdbcType=JdbcType.INTEGER),
        @Result(column="from_id", property="fromId", jdbcType=JdbcType.BIGINT),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    MtoMessage selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, created, event, from_id, post_id, status, user_id",
        "from mto_message"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="event", property="event", jdbcType=JdbcType.INTEGER),
        @Result(column="from_id", property="fromId", jdbcType=JdbcType.BIGINT),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<MtoMessage> selectAll();

    @Update({
        "update mto_message",
        "set created = #{created,jdbcType=TIMESTAMP},",
          "event = #{event,jdbcType=INTEGER},",
          "from_id = #{fromId,jdbcType=BIGINT},",
          "post_id = #{postId,jdbcType=BIGINT},",
          "status = #{status,jdbcType=INTEGER},",
          "user_id = #{userId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoMessage record);

    @Select({
            "select",
            "id, created, event, from_id, post_id, status, user_id",
            "from mto_message",
            "where user_id = #{userId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="event", property="event", jdbcType=JdbcType.INTEGER),
            @Result(column="from_id", property="fromId", jdbcType=JdbcType.BIGINT),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<MtoMessage> findAllByUserId(long userId);

    @Update({
            "delete from mto_message",
            "where post_id = #{postId,jdbcType=BIGINT}"
    })
    int deleteByPostId(long postId);

    @Select({"select count(*) from mto_channel where user_id = #{userId,jdbcType=BIGINT} and status = #{unread,jdbcType=BIGINT}"})
    int countByUserIdAndStatus(long userId, int unread);

    @Update({
            "update from mto_message set status = 1",
            "where user_id = #{userId,jdbcType=BIGINT}"
    })
    void updateReadedByUserId(long userId);
}