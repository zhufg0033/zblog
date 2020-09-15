package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoPostResource;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface MtoPostResourceDao {
    @Delete({
        "delete from mto_post_resource",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_post_resource (id, path, ",
        "post_id, resource_id, ",
        "sort)",
        "values (#{id,jdbcType=BIGINT}, #{path,jdbcType=VARCHAR}, ",
        "#{postId,jdbcType=BIGINT}, #{resourceId,jdbcType=BIGINT}, ",
        "#{sort,jdbcType=INTEGER})"
    })
    int insert(MtoPostResource record);

    @Select({
        "select",
        "id, path, post_id, resource_id, sort",
        "from mto_post_resource",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="resource_id", property="resourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER)
    })
    MtoPostResource selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, path, post_id, resource_id, sort",
        "from mto_post_resource"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="resource_id", property="resourceId", jdbcType=JdbcType.BIGINT),
        @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPostResource> selectAll();

    @Update({
        "update mto_post_resource",
        "set path = #{path,jdbcType=VARCHAR},",
          "post_id = #{postId,jdbcType=BIGINT},",
          "resource_id = #{resourceId,jdbcType=BIGINT},",
          "sort = #{sort,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoPostResource record);
}