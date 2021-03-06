package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoFavorite;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface MtoFavoriteDao {
    @Delete({
        "delete from mto_favorite",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_favorite (id, created, ",
        "post_id, user_id)",
        "values (#{id,jdbcType=BIGINT}, #{created,jdbcType=TIMESTAMP}, ",
        "#{postId,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT})"
    })
    int insert(MtoFavorite record);

    @Select({
        "select",
        "id, created, post_id, user_id",
        "from mto_favorite",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    MtoFavorite selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, created, post_id, user_id",
        "from mto_favorite"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="created", property="created", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="post_id", property="postId", jdbcType=JdbcType.BIGINT),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT)
    })
    List<MtoFavorite> selectAll();

    @Update({
        "update mto_favorite",
        "set created = #{created,jdbcType=TIMESTAMP},",
          "post_id = #{postId,jdbcType=BIGINT},",
          "user_id = #{userId,jdbcType=BIGINT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoFavorite record);
}