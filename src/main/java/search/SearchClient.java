package search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchClient {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

//        //create index
//        CreateIndexRequest request = new CreateIndexRequest("posts");
//        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
//        boolean success=createIndexResponse.isAcknowledged();

        //index docs

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "test");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
                .source(jsonMap);

        IndexResponse response=client.index(indexRequest,RequestOptions.DEFAULT);

        System.out.println(response.getResult());

        //or this way
        indexRequest = new IndexRequest("posts", "doc", "2")
                .source("user", "test",
                        "postDate", new Date(),
                        "message", "message2");

        response=client.index(indexRequest,RequestOptions.DEFAULT);
        System.out.println(response.getResult());

        //pr this way

        indexRequest = new IndexRequest(
                "posts",
                "doc",
                "3");
        String jsonString = "{" +
                "\"user\":\"test2\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"message3\"" +
                "}";
        indexRequest.source(jsonString, XContentType.JSON);
        response=client.index(indexRequest,RequestOptions.DEFAULT);
        System.out.println(response.getResult());


        //get
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");
        GetResponse getResponse=client.get(getRequest,RequestOptions.DEFAULT);
        System.out.println(getResponse.toString());

        //exclude fields
        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = new String[]{"message"};
        FetchSourceContext fetchSourceContext =
                new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
        getResponse=client.get(getRequest,RequestOptions.DEFAULT);
        System.out.println(getResponse.toString());

        //search
        SearchRequest searchRequest = new SearchRequest(); //Without arguments this runs against all indices.
        searchRequest.indices("posts");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); //Most search parameters are added here
        searchSourceBuilder.query(QueryBuilders.termQuery("user", "test"));
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS)); //set timeout
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.ASC));
        searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder); //Add the SearchSourceBuilder to the SeachRequest.

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);

        //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-search.html
        client.close();
    }
}

