package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoTag;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface MtoTagDao {
    @Delete({
        "delete from mto_tag",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_tag (id, created, ",
        "description, latest_post_id, ",
        "name, posts, thumbnail, ",
        "updated)",
        "values (#{id,jdbcType=BIGINT}, #{created,jdbcType=TIMESTAMP}, ",
        "#{description,jdbcType=VARCHAR}, #{latestPostId,jdbcType=BIGINT}, ",
        "#{name,jdbcType=VARCHAR}, #{posts,jdbcType=INTEGER}, #{thumbnail,jdbcType=VARCHAR}, ",
        "#{updated,jdbcType=TIMESTAMP})"
    })
    int insert(MtoTag record);

    @Select({
        "select",
        "id, created, description, latest_post_id, name, posts, thumbnail, updated",
        "from mto_tag",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="latest_post_id", property="latestPostId", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
        @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
        @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP)
    })
    MtoTag selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, created, description, latest_post_id, name, posts, thumbnail, updated",
        "from mto_tag"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="latest_post_id", property="latestPostId", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="posts", property="posts", jdbcType=JdbcType.INTEGER),
        @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
        @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP)
    })
    List<MtoTag> selectAll();

    @Update({
        "update mto_tag",
        "set created = #{created,jdbcType=TIMESTAMP},",
          "description = #{description,jdbcType=VARCHAR},",
          "latest_post_id = #{latestPostId,jdbcType=BIGINT},",
          "name = #{name,jdbcType=VARCHAR},",
          "posts = #{posts,jdbcType=INTEGER},",
          "thumbnail = #{thumbnail,jdbcType=VARCHAR},",
          "updated = #{updated,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoTag record);
}