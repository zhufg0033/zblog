package com.zblog.sharedb.dao.subtreasury;

import com.zblog.sharedb.entity.MtoResource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface MtoResourceDao {
    @Delete({
        "delete from mto_resource",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_resource (id, amount, ",
        "create_time, md5, ",
        "path, update_time)",
        "values (#{id,jdbcType=BIGINT}, #{amount,jdbcType=BIGINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{md5,jdbcType=VARCHAR}, ",
        "#{path,jdbcType=VARCHAR}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(MtoResource record);

    @Select({
        "select",
        "id, amount, create_time, md5, path, update_time",
        "from mto_resource",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="md5", property="md5", jdbcType=JdbcType.VARCHAR),
        @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    MtoResource selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, amount, create_time, md5, path, update_time",
        "from mto_resource"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="md5", property="md5", jdbcType=JdbcType.VARCHAR),
        @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<MtoResource> selectAll();

    @Update({
        "update mto_resource",
        "set amount = #{amount,jdbcType=BIGINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "md5 = #{md5,jdbcType=VARCHAR},",
          "path = #{path,jdbcType=VARCHAR},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoResource record);

    @Select({
            "select",
            "id, amount, create_time, md5, path, update_time",
            "from mto_resource",
            "where md5=#{md5,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="md5", property="md5", jdbcType=JdbcType.VARCHAR),
            @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    MtoResource findByMd5(String md5);

    @Select({
            "<script>",
            "select",
            "id, amount, create_time, md5, path, update_time",
            "from mto_resource",
            "where md5 in ",
            "<foreach item='item' index='index' collection='adds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="md5", property="md5", jdbcType=JdbcType.VARCHAR),
            @Result(column="path", property="path", jdbcType=JdbcType.VARCHAR),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<MtoResource> findByMd5In(@Param("adds") List<String> adds);

    @Update({
        "<script>",
        "update mto_resource",
        "set amount = amount+#{increment,jdbcType=BIGINT}",
        "where md5 in ",
        "<foreach item='item' index='index' collection='adds' open='(' separator=',' close=')'>",
        "#{item}",
        "</foreach>",
        "</script>"
    })
    void updateAmount(@Param("adds")List<String> adds, int increment);

    @Update({
            "<script>",
            "update mto_resource",
            "set amount = amount+#{increment,jdbcType=BIGINT}",
            "where id in ",
            "<foreach item='item' index='index' collection='rids' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"
    })
    void updateAmountByIds(@Param("rids")List<Long> rids, int increment);
}