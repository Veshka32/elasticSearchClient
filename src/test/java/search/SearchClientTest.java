package search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.joda.time.field.FieldUtils;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import sun.reflect.misc.FieldUtil;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchClientTest {
    @Mock
    RestHighLevelClient client;

    @InjectMocks
    SearchClient service;


    @org.junit.Test(expected = RuntimeException.class)
    public void search() throws IOException {
        when(client.search(any(SearchRequest.class),any(RequestOptions.class))).thenThrow(RuntimeException.class);
        service.search();

    }

    @org.junit.Test
    public void search1() throws IOException {
        SearchResponse response=new SearchResponse();
        when(client.search(any(SearchRequest.class),any(RequestOptions.class))).thenReturn(response);
        assertEquals(response,service.search());
    }



}