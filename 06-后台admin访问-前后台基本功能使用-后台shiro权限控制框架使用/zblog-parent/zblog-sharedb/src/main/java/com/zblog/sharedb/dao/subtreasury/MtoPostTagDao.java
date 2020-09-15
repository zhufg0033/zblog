package com.zblog.sharedb.dao.subtreasury;

import com.zblog.sharedb.entity.MtoPostTag;
import com.zblog.sharedb.vo.PageModuls;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

@Mapper
public interface MtoPostTagDao {
    @Delete({
        "delete from mto_post_tag",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_post_tag (id, post_id, ",
        "tag_id, weight)",
        "values (#{id,jdbcType=BIGINT}, #{postId,jdbcType=BIGINT}, ",
        "#{tagId,jdbcType=BIGINT}, #{weight,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(MtoPostTag record);

    @Select({
        "select",
        "id, post_id, tag_id, weight",
        "from mto_post_tag",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="tag_id", property="tagId", jdbcType=JdbcType.BIGINT),
        @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT)
    })
    MtoPostTag selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, post_id, tag_id, weight",
        "from mto_post_tag"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="tag_id", property="tagId", jdbcType=JdbcType.BIGINT),
        @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT)
    })
    List<MtoPostTag> selectAll();

    @Update({
        "update mto_post_tag",
        "set post_id = #{postId,jdbcType=BIGINT},",
          "tag_id = #{tagId,jdbcType=BIGINT},",
          "weight = #{weight,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoPostTag record);

    @Select({
            "select",
            "id, post_id, tag_id, weight",
            "from mto_post_tag",
            "where tag_id = #{tagId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="tag_id", property="tagId", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT)
    })
    List<MtoPostTag> findAllByTagId(Long tagId);

    @Select({
            "select",
            "id, post_id, tag_id, weight",
            "from mto_post_tag",
            "where tag_id = #{tagId,jdbcType=BIGINT} AND post_id=#{postId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
            @Result(column="tag_id", property="tagId", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT)
    })
    MtoPostTag findByPostIdAndTagId(long postId, Long tagId);

    @Select({
            "select",
            "tag_id",
            "from mto_post_tag",
            "where post_id=#{postId,jdbcType=BIGINT}"
    })
    List<Long> findTagIdByPostId(long postId);

    @Delete({
            "delete from mto_post_tag",
            "where post_id = #{postId,jdbcType=BIGINT}"
    })
    void deleteByPostId(long postId);
}