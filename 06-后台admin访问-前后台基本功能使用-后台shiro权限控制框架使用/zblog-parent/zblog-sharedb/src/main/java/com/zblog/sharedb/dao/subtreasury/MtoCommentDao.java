package com.zblog.sharedb.dao.subtreasury;

import com.zblog.sharedb.entity.MtoComment;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

public interface MtoCommentDao {
    @Delete({
        "delete from mto_comment",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_comment (id, author_id, ",
        "content, created, ",
        "pid, post_id, status)",
        "values (#{id,jdbcType=BIGINT}, #{authorId,jdbcType=BIGINT}, ",
        "#{content,jdbcType=VARCHAR}, #{created,jdbcType=TIMESTAMP}, ",
        "#{pid,jdbcType=BIGINT}, #{postId,jdbcType=BIGINT}, #{status,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(MtoComment record);

    @Select({
        "select",
        "id, author_id, content, created, pid, post_id, status",
        "from mto_comment",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    MtoComment selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, author_id, content, created, pid, post_id, status",
        "from mto_comment where status = 0 ",
        "order by id desc"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<MtoComment> selectAll();

    @Update({
        "update mto_comment",
        "set author_id = #{authorId,jdbcType=BIGINT},",
          "content = #{content,jdbcType=VARCHAR},",
          "created = #{created,jdbcType=TIMESTAMP},",
          "pid = #{pid,jdbcType=BIGINT},",
          "post_id = #{postId,jdbcType=BIGINT},",
          "status = #{status,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoComment record);

    @Select({
            "select",
            "id, author_id, content, created, pid, post_id, status",
            "from mto_comment",
            "where authorId = #{authorId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<MtoComment> findAllByAuthorId(long authorId);

    @Select({
            "<script>",
            "select",
            "id, author_id, content, created, pid, post_id, status",
            "from mto_comment",
            "where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<MtoComment> findAllById(@Param("ids") List<Long> ids);

    @Update({
            "update mto_comment",
            "set status = 1 ",
            "where post_id = #{postId,jdbcType=BIGINT}"
    })
    int removeByPostId(long postId);

    @Select({"select count(*) from mto_comment"})
    long count();

    @Select({"select count(*) from mto_comment where author_id = #{authorId,jdbcType=BIGINT} and post_id = #{toId,jdbcType=BIGINT}"})
    long countByAuthorIdAndPostId(long authorId, long toId);

    @Update({
            "<script>",
            "update mto_comment",
            "set status = 1 ",
            "where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    int removeByIdIn(@Param("ids")List<Long> ids);

    @Select({
            "select",
            "id, author_id, content, created, pid, post_id, status",
            "from mto_comment",
            "where post_id = #{postId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="pid", property="pid", jdbcType=JdbcType.BIGINT),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)
    })
    List<MtoComment> findAllByPostId(long postId);
}