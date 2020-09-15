package com.zblog.sharedb.dao.subtreasury;

import com.zblog.sharedb.entity.MtoPostResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
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
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
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

    @Insert({
            "<script>",
            "insert into mto_post_resource  (id, path, ",
            "post_id, resource_id, ",
            "sort)",
            "values " ,
            "<foreach item='item' index='index' collection='prs' open='(' separator=',' close=')'>",
            "(#{item.id,jdbcType=BIGINT}, #{item.path,jdbcType=VARCHAR}, ",
            "#{item.postId,jdbcType=BIGINT}, #{item.resourceId,jdbcType=BIGINT}, ",
            "#{item.sort,jdbcType=INTEGER})",
            "</foreach>",
            "</script>"
    })
    int insertAll(List<MtoPostResource> prs);

    @Delete({
            "<script>",
            "delete from mto_post_resource",
            "where post_id = #{postId,jdbcType=BIGINT}",
            "and resource_id in ",
            "<foreach item='item' index='index' collection='rids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    void deleteByPostIdAndResourceIdIn(Long postId, @Param("rids") List<Long> rids);

    @Select({
            "select",
            "id, path, post_id, resource_id, sort",
            "from mto_post_resource",
            "where post_id = #{postId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="resource_id", property="resourceId", jdbcType=JdbcType.BIGINT),
            @Result(column="sort", property="sort", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPostResource> findByPostId(long postId);

    @Delete({
            "delete from mto_post_resource",
            "where post_id = #{postId,jdbcType=BIGINT}"
    })
    void deleteByPostId(long postId);
}