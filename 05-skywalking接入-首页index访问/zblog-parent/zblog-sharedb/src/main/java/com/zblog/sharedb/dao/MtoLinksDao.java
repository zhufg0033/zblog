package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoLinks;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface MtoLinksDao {
    @Delete({
        "delete from mto_links",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_links (id, created, ",
        "name, updated, ",
        "url)",
        "values (#{id,jdbcType=BIGINT}, #{created,jdbcType=TIMESTAMP}, ",
        "#{name,jdbcType=VARCHAR}, #{updated,jdbcType=TIMESTAMP}, ",
        "#{url,jdbcType=VARCHAR})"
    })
    int insert(MtoLinks record);

    @Select({
        "select",
        "id, created, name, updated, url",
        "from mto_links",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR)
    })
    MtoLinks selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, created, name, updated, url",
        "from mto_links"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="updated", property="updated", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="url", property="url", jdbcType=JdbcType.VARCHAR)
    })
    List<MtoLinks> selectAll();

    @Update({
        "update mto_links",
        "set created = #{created,jdbcType=TIMESTAMP},",
          "name = #{name,jdbcType=VARCHAR},",
          "updated = #{updated,jdbcType=TIMESTAMP},",
          "url = #{url,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoLinks record);
}