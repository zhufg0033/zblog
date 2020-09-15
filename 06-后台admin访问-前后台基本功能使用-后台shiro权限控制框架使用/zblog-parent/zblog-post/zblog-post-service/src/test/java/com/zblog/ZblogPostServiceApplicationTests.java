package com.zblog;


import com.zblog.post.service.PostService;
import com.zblog.sharedb.dao.subtreasury.MtoPostDao;
import com.zblog.sharedb.dao.subtreasury.MtoUserDao;
import com.zblog.sharedb.entity.MtoPost;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Reference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(value = "com.zblog.sharedb.dao.subtreasury",sqlSessionFactoryRef = "sqlSessionFactoryBean")
@MapperScan(value = "com.zblog.sharedb.dao.master",sqlSessionFactoryRef = "sqlSessionFactoryDefault")
public class ZblogPostServiceApplicationTests {

//    @Re
//    MtoUserDao mtoUserDao;

//    @Reference
//    PostService postService;

    @Autowired
    MtoPostDao mtoPostDao;

    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {
//        System.out.println(postService.count());
//
//        List<MtoPost> mtoPosts = mtoPostDao.selectAll();
//
//        for(MtoPost mtoPost : mtoPosts){
    }

    @Test
    public void contextLoads2020() throws IOException {
        // jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        // filter
//        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","43");
//        boolQueryBuilder.filter(termQueryBuilder);
        // must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("summary","中泰");
        boolQueryBuilder.should(matchQueryBuilder);
        MatchQueryBuilder matchQueryBuilder_title = new MatchQueryBuilder("title",        "中泰"
        );
        boolQueryBuilder.should(matchQueryBuilder_title);
        MatchQueryBuilder matchQueryBuilder_tags = new MatchQueryBuilder("tags",        "中泰"
        );
        boolQueryBuilder.should(matchQueryBuilder_tags);
        // query
        searchSourceBuilder.query(boolQueryBuilder);
        // from
        searchSourceBuilder.from(0);
        // size
        searchSourceBuilder.size(20);
        // highlight
        searchSourceBuilder.highlight();

        String dslStr = searchSourceBuilder.toString();

        System.err.println(dslStr);

        Search get = new Search.Builder(dslStr).addIndex("zblogpost").addType("MtoPost").build();

        SearchResult execute = jestClient.execute(get);

        List<SearchResult.Hit<MtoPost,Void>> hits = execute.getHits(MtoPost.class);

        List<MtoPost> mtoPosts = new ArrayList<>();
        for(SearchResult.Hit<MtoPost,Void> hit:hits){
            MtoPost mtoPost = hit.source;
            mtoPosts.add(mtoPost);
        }

        System.out.println(mtoPosts);
    }

    /**
     * PUT zblogpost?include_type_name=true
     * {
     *   "mappings": {
     *     "MtoPost":{
     *       "properties":{
     *         "id":{
     *           "type":"keyword",
     *           "index":true
     *         },
     *         "authorId":{
     *           "type":"integer"
     *         },
     *         "channelId":{
     *           "type":"integer"
     *         },
     *         "comments":{
     *           "type":"integer"
     *         },
     *         "created":{
     *           "type":"date",
     *           "format":"yyyy-MM-dd HH:mm:ss"
     *         },
     *         "favors":{
     *           "type":"integer"
     *         },
     *         "featured":{
     *           "type":"integer"
     *         },
     *         "status":{
     *           "type":"integer"
     *         },
     *         "summary":{
     *           "type":"text",
     *           "analyzer":"ik_max_word"
     *         },
     *         "tags":{
     *           "type":"text",
     *           "analyzer":"ik_max_word"
     *         },
     *         "thumbnail":{
     *           "type":"text"
     *         },
     *         "title":{
     *           "type":"text",
     *           "analyzer":"ik_max_word"
     *         },
     *         "views":{
     *           "type":"integer"
     *         },
     *         "weight":{
     *           "type":"integer"
     *         }
     *       }
     *
     *     }
     *   }
     *}
     *
     */

}