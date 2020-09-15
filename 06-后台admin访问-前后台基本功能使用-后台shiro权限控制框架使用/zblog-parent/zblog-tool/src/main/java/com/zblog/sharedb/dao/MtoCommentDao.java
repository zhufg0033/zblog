package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoComment;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

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
        "from mto_comment"
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
}