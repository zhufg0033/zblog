package com.zblog.post.service.impl;


import com.zblog.post.service.PostSearchService;
import com.zblog.post.service.aspect.PostStatusFilter;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.dao.subtreasury.MtoPostDao;
import com.zblog.sharedb.dao.subtreasury.MtoUserDao;
import com.zblog.sharedb.entity.MtoPost;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.sharedb.vo.UserVO;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Service;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * @author : langhsu
 * @version : 1.0
 * @date : 2019/1/18
 */
@Service
@Transactional
public class PostSearchServiceImpl implements PostSearchService {

    @Autowired
    private MtoPostDao mtoPostDao;

    @Autowired
    private MtoUserDao mtoUserDao;

    @Autowired
    JestClient jestClient;

    @Override
    @PostStatusFilter
    public DataPageVO<PostVO> search(PageModuls pageModuls, String term) throws Exception {

        // jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // must  全部匹配
        // shold  或者匹配
        MatchQueryBuilder matchQueryBuilder_summary = new MatchQueryBuilder("summary",term);
        boolQueryBuilder.should(matchQueryBuilder_summary);
        MatchQueryBuilder matchQueryBuilder_title = new MatchQueryBuilder("title",term);
        boolQueryBuilder.should(matchQueryBuilder_title);
        MatchQueryBuilder matchQueryBuilder_tags = new MatchQueryBuilder("tags",term);
        boolQueryBuilder.should(matchQueryBuilder_tags);

        // query
        searchSourceBuilder.query(boolQueryBuilder);
        // from
        searchSourceBuilder.from(pageModuls.getPageNo()-1);
        // size
        searchSourceBuilder.size(pageModuls.getPageSize());
        // highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder() ;
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.field("title");
        highlightBuilder.field("summary");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        String dslStr = searchSourceBuilder.toString();

        System.err.println(dslStr);

        Search get = new Search.Builder(dslStr).addIndex("zblogpost").addType("MtoPost").build();

        SearchResult execute = jestClient.execute(get);

        List<SearchResult.Hit<MtoPost,Void>> hits = execute.getHits(MtoPost.class);

        List<PostVO> postVOS = new ArrayList<>();
        for(SearchResult.Hit<MtoPost,Void> hit:hits){
            MtoPost mtoPost = hit.source;

            PostVO postVO = PostBeanMapUtils.copy(mtoPost);

            Map<String, List<String>> highlight = hit.highlight;
            if(highlight!=null) {
                if(highlight.get("title")!=null) {
                    String title = highlight.get("title").get(0);
                    postVO.setTitle(title);
                }
                if(highlight.get("summary")!=null) {
                    String summary = highlight.get("summary").get(0);
                    postVO.setSummary(summary);
                }

            }

            postVOS.add(postVO);
        }
        buildUsers(postVOS);
        return new DataPageVO<>(pageModuls,postVOS,postVOS.size());
    }

    @Override
    public void resetIndexes() {
        List<MtoPost> mtoPosts = mtoPostDao.selectAll();

        for(MtoPost mtoPost : mtoPosts){
            Index put = new Index.Builder(mtoPost).index("zblogpost").type("MtoPost").id(mtoPost.getId()+"").build();
            try {
                jestClient.execute(put);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void buildUsers(List<PostVO> list) {

        if (null == list || list.size() == 0) {
            return;
        }
        HashSet<Long> uids = new HashSet<>();
        list.forEach(n -> uids.add(n.getAuthorId()));

        List<Long> uidList = new ArrayList<>();
        uidList.addAll(uids);

        List<MtoUser> allById = mtoUserDao.findAllById(uidList);
        Map<Long, UserVO> userMap = new HashMap<>();
        if(allById != null) {
            allById.forEach(p -> userMap.put(p.getId(), PostBeanMapUtils.copy(p)));
            list.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
        }
    }
}
