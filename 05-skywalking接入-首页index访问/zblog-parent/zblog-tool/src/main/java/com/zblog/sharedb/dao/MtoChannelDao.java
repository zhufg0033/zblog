package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoChannel;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

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
        "from mto_channel"
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
}