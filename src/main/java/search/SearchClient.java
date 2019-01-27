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
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchClient {

//        //or this way
//        indexRequest = new IndexRequest("posts", "doc", "2")
//                .source("user", "test",
//                        "postDate", new Date(),
//                        "message", "message2");
//
//        response=client.index(indexRequest,RequestOptions.DEFAULT);
//        System.out.println(response.getResult());
//
//        //or this way
//
//        indexRequest = new IndexRequest(
//                "posts",
//                "doc",
//                "3");
//        String jsonString = "{" +
//                "\"user\":\"test2\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"message3\"" +
//                "}";
//        indexRequest.source(jsonString, XContentType.JSON);
//        response=client.index(indexRequest,RequestOptions.DEFAULT);

    public static void main(String[] args) throws IOException {
        final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

//        //create index
//        CreateIndexRequest request = new CreateIndexRequest("besy");
//        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
//        System.out.println(createIndexResponse.isAcknowledged());
//
//        //index docs
//        Map<String,Object> jsonMap=new HashMap<>();
//        jsonMap.put("order","123");
//        jsonMap.put("date", LocalDate.now());
//        jsonMap.put("client", "bob");
//        jsonMap.put("notes","test order");
//
//        IndexRequest indexRequest = new IndexRequest("besy","booking","1").source(jsonMap);
//        IndexResponse response=client.index(indexRequest,RequestOptions.DEFAULT);
//        System.out.println(response.getResult());
//
//        jsonMap.put("order","456");
//        jsonMap.put("date",LocalDate.now().minusDays(1));
//        jsonMap.put("client","bob");
//        jsonMap.put("notes","bob second order");
//        indexRequest = new IndexRequest("besy","booking","2").source(jsonMap);
//        indexRequest.source(jsonMap);
//        response=client.index(indexRequest,RequestOptions.DEFAULT);
//        System.out.println(response.getResult());
//
//        jsonMap.put("order","789");
//        jsonMap.put("date",LocalDate.now());
//        jsonMap.put("client","alice");
//        jsonMap.put("notes","alice order");
//        indexRequest = new IndexRequest("besy","booking","3").source(jsonMap);
//        indexRequest.source(jsonMap);
//        response=client.index(indexRequest,RequestOptions.DEFAULT);
//        System.out.println(response.getResult());

//        //get
//        GetRequest getRequest = new GetRequest(
//                "besy",
//                "booking",
//                "1");
//        GetResponse getResponse=client.get(getRequest,RequestOptions.DEFAULT);
//        System.out.println(getResponse.toString());
//
//        //exclude fields
//        String[] includes = Strings.EMPTY_ARRAY;
//        String[] excludes = new String[]{"message"};
//        FetchSourceContext fetchSourceContext =
//                new FetchSourceContext(true, includes, excludes);
//        getRequest.fetchSourceContext(fetchSourceContext);
//        getResponse=client.get(getRequest,RequestOptions.DEFAULT);
//        System.out.println(getResponse.toString());

        //search
        SearchRequest searchRequest = new SearchRequest(); //Without arguments this runs against all indices.
        searchRequest.indices("besy");
        searchRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);

        SearchSourceBuilder builder = new SearchSourceBuilder(); //Most search parameters are added here
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS)); //set timeout
        builder.query(QueryBuilders.matchAllQuery()); //get all
        searchRequest.source(builder); //Add the SearchSourceBuilder to the SearchRequest.
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);

        builder = new SearchSourceBuilder(); //Most search parameters are added here
        builder.query(new MatchQueryBuilder());
        searchRequest.source(builder); //Add the SearchSourceBuilder to the SearchRequest.
        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(searchResponse);

        //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-search.html
        client.close();
    }
}

