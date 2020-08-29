package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoPost;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper
public interface MtoPostDao {
    @Delete({
        "delete from mto_post",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_post (id, author_id, ",
        "channel_id, comments, ",
        "created, favors, ",
        "featured, status, ",
        "summary, tags, thumbnail, ",
        "title, views, weight)",
        "values (#{id,jdbcType=BIGINT}, #{authorId,jdbcType=BIGINT}, ",
        "#{channelId,jdbcType=INTEGER}, #{comments,jdbcType=INTEGER}, ",
        "#{created,jdbcType=TIMESTAMP}, #{favors,jdbcType=INTEGER}, ",
        "#{featured,jdbcType=INTEGER}, #{status,jdbcType=INTEGER}, ",
        "#{summary,jdbcType=VARCHAR}, #{tags,jdbcType=VARCHAR}, #{thumbnail,jdbcType=VARCHAR}, ",
        "#{title,jdbcType=VARCHAR}, #{views,jdbcType=INTEGER}, #{weight,jdbcType=INTEGER})"
    })
    int insert(MtoPost record);

    @Select({
        "select",
        "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
        "tags, thumbnail, title, views, weight",
        "from mto_post",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
        @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
        @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
        @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
        @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
        @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
        @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    MtoPost selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
        "tags, thumbnail, title, views, weight",
        "from mto_post"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
        @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
        @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
        @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
        @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
        @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
        @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> selectAll();

    @Update({
        "update mto_post",
        "set author_id = #{authorId,jdbcType=BIGINT},",
          "channel_id = #{channelId,jdbcType=INTEGER},",
          "comments = #{comments,jdbcType=INTEGER},",
          "created = #{created,jdbcType=TIMESTAMP},",
          "favors = #{favors,jdbcType=INTEGER},",
          "featured = #{featured,jdbcType=INTEGER},",
          "status = #{status,jdbcType=INTEGER},",
          "summary = #{summary,jdbcType=VARCHAR},",
          "tags = #{tags,jdbcType=VARCHAR},",
          "thumbnail = #{thumbnail,jdbcType=VARCHAR},",
          "title = #{title,jdbcType=VARCHAR},",
          "views = #{views,jdbcType=INTEGER},",
          "weight = #{weight,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoPost record);

    @Select({
            "<script>",
            "select",
            "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
            "tags, thumbnail, title, views, weight",
            "from mto_post",
            "where 1=1  " ,
            "<if test=\"channelId > 0\">",
            "and channel_id = #{channelId,jdbcType=BIGINT}",
            "</if>",
            "<if test=\"excludeChannelIds != null and excludeChannelIds.size > 0\">",
            " and channel_id not in ",
            "<foreach item='item' index='index' collection='excludeChannelIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
            @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
            @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
            @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> findAllByChannel(int channelId, @Param("excludeChannelIds") List<Integer> excludeChannelIds);

    @Select({
            "<script>",
            "select",
            "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
            "tags, thumbnail, title, views, weight",
            "from mto_post",
            "<if test=\"excludeChannelIds != null and excludeChannelIds.size > 0\">",
            " where channel_id not in ",
            "<foreach item='item' index='index' collection='excludeChannelIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
            @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
            @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
            @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> findAllByExcludeChannels(@Param("excludeChannelIds")List<Integer> excludeChannelIds);


    @Select({
            "select",
            "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
            "tags, thumbnail, title, views, weight",
            "from mto_post",
            "where channel_id = #{channelId,jdbcType=BIGINT}",
            "and title like #{title,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
            @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
            @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
            @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> findAllByChannelAndTile(int channelId, String title);

    @Select({
            "select",
            "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
            "tags, thumbnail, title, views, weight",
            "from mto_post",
            "where author_id = #{userId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
            @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
            @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
            @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> findAllByAuthorId(long userId);

    @Select({
            "<script>",
            "select",
            "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
            "tags, thumbnail, title, views, weight",
            "from mto_post",
            "<if test=\"ids != null and ids.size > 0\">",
            " where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
            @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
            @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
            @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> findAllById(@Param("ids") List<Long> ids);

    @Select({
            "<script>",
            "select",
            "id, author_id, channel_id, comments, created, favors, featured, status, summary, ",
            "tags, thumbnail, title, views, weight",
            "from mto_post",
            "<if test=\"ids != null and ids.size > 0\">",
            " where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
            @Result(column="channel_id", property="channelId", jdbcType=JdbcType.INTEGER),
            @Result(column="comments", property="comments", jdbcType=JdbcType.INTEGER),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="favors", property="favors", jdbcType=JdbcType.INTEGER),
            @Result(column="featured", property="featured", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="summary", property="summary", jdbcType=JdbcType.VARCHAR),
            @Result(column="tags", property="tags", jdbcType=JdbcType.VARCHAR),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
            @Result(column="views", property="views", jdbcType=JdbcType.INTEGER),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoPost> findAllById0(@Param("ids") List<Long> ids);

    @Select({"select coalesce(max(weight), 0) from mto_post"})
    int maxWeight();

    @Select({"select count(*) from mto_post"})
    long count();
}