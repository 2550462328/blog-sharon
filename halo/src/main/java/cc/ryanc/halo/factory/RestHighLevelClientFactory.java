package cc.ryanc.halo.factory;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class RestHighLevelClientFactory {

    private static final String HOST = "localhost";
    private static final int PORT = 9200;

    private static RestHighLevelClient client;

    public static RestHighLevelClient instance(){
        if(client == null){
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, PORT, "http")));
        }
        return client;
    }
}
