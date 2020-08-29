package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoOptions;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface MtoOptionsDao {
    @Delete({
        "delete from mto_options",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_options (id, key_, ",
        "type, value)",
        "values (#{id,jdbcType=BIGINT}, #{key,jdbcType=VARCHAR}, ",
        "#{type,jdbcType=INTEGER}, #{value,jdbcType=VARCHAR})"
    })
    int insert(MtoOptions record);

    @Select({
        "select",
        "id, key_, type, value",
        "from mto_options",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="value", property="value", jdbcType=JdbcType.VARCHAR)
    })
    MtoOptions selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, key_, type, value",
        "from mto_options"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="value", property="value", jdbcType=JdbcType.VARCHAR)
    })
    List<MtoOptions> selectAll();

    @Update({
        "update mto_options",
        "set key_ = #{key,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER},",
          "value = #{value,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoOptions record);
}