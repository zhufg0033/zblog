package com.zblog.sharedb.dao.subtreasury;

import com.zblog.sharedb.entity.MtoSecurityCode;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface MtoSecurityCodeDao {
    @Delete({
        "delete from mto_security_code",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_security_code (id, code, ",
        "created, expired, ",
        "key_, status, target, ",
        "type)",
        "values (#{id,jdbcType=BIGINT}, #{code,jdbcType=VARCHAR}, ",
        "#{created,jdbcType=TIMESTAMP}, #{expired,jdbcType=TIMESTAMP}, ",
        "#{key,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, #{target,jdbcType=VARCHAR}, ",
        "#{type,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(MtoSecurityCode record);

    @Select({
        "select",
        "id, code, created, expired, key_, status, target, type",
        "from mto_security_code",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="expired", property="expired", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="target", property="target", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER)
    })
    MtoSecurityCode selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, code, created, expired, key_, status, target, type",
        "from mto_security_code"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="expired", property="expired", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="target", property="target", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER)
    })
    List<MtoSecurityCode> selectAll();

    @Update({
        "update mto_security_code",
        "set code = #{code,jdbcType=VARCHAR},",
          "created = #{created,jdbcType=TIMESTAMP},",
          "expired = #{expired,jdbcType=TIMESTAMP},",
          "key_ = #{key,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "target = #{target,jdbcType=VARCHAR},",
          "type = #{type,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoSecurityCode record);

    @Select({
            "select",
            "id, code, created, expired, key_, status, target, type",
            "from mto_security_code",
            "where key_ = #{key,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="expired", property="expired", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="target", property="target", jdbcType=JdbcType.VARCHAR),
            @Result(column="type", property="type", jdbcType=JdbcType.INTEGER)
    })
    MtoSecurityCode findByKey(String key);

    @Select({
            "select",
            "id, code, created, expired, key_, status, target, type",
            "from mto_security_code",
            "where key_ = #{key,jdbcType=VARCHAR} and type = #{type,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="expired", property="expired", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="key_", property="key", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="target", property="target", jdbcType=JdbcType.VARCHAR),
            @Result(column="type", property="type", jdbcType=JdbcType.INTEGER)
    })
    MtoSecurityCode findByKeyAndType(String key, int type);
}