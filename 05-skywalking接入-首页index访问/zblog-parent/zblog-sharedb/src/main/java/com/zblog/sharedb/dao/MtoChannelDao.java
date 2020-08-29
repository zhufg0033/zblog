package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoChannel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MtoChannelDao {
    @Delete({
        "delete from mto_channel",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into mto_channel (id, key_, ",
        "name, status, thumbnail, ",
        "weight)",
        "values (#{id,jdbcType=INTEGER}, #{key,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, #{thumbnail,jdbcType=VARCHAR}, ",
        "#{weight,jdbcType=INTEGER})"
    })
    int insert(MtoChannel record);

    @Select({
        "select",
        "id, key_, name, status, thumbnail, weight",
        "from mto_channel",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
        @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    MtoChannel selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, key_, name, status, thumbnail, weight",
        "from mto_channel",
        "order by weight , id desc"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
        @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoChannel> selectAll();

    @Update({
        "update mto_channel",
        "set key_ = #{key,jdbcType=VARCHAR},",
          "name = #{name,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "thumbnail = #{thumbnail,jdbcType=VARCHAR},",
          "weight = #{weight,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(MtoChannel record);

    @Select({
            "select",
            "id, key_, name, status, thumbnail, weight",
            "from mto_channel",
            "where status = #{status,jdbcType=INTEGER}",
            "order by weight , id desc"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoChannel> findAllByStatus(int status);

    @Select({
            "<script>",
            "select",
            "id, key_, name, status, thumbnail, weight",
            "from mto_channel",
            "where id in ",
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoChannel> findAllById(@Param("ids")List<Integer> ids);

    @Select({"select coalesce(max(weight), 0) from mto_channel"})
    int maxWeight();

    @Select({"select count(*) from mto_channel"})
    int count();

    @Select({
            "select",
            "id, key_, name, status, thumbnail, weight",
            "from mto_channel",
            "where status = #{statusClosed,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="thumbnail", property="thumbnail", jdbcType=JdbcType.VARCHAR),
            @Result(column="weight", property="weight", jdbcType=JdbcType.INTEGER)
    })
    List<MtoChannel> findAll(int statusClosed);
}