package br.com.entregacep.data;
import org.junit.*;
import static org.junit.Assert.*;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class BrasilApiTest {
    private MockWebServer server;
    private RoomDeliveryRepository repository;
    @Before public void setup() throws Exception {server=new MockWebServer();server.start();BrasilApi api=new Retrofit.Builder().baseUrl(server.url("/")).client(new OkHttpClient()).addConverterFactory(GsonConverterFactory.create()).build().create(BrasilApi.class);repository=new RoomDeliveryRepository(null,api);}
    @After public void close() throws Exception {server.shutdown();}
    @Test public void mapsAddressAndUsesCorrectRoute() throws Exception {server.enqueue(new MockResponse().setBody("{\"cep\":\"01001000\",\"state\":\"SP\",\"city\":\"São Paulo\",\"street\":\"Praça da Sé\",\"neighborhood\":\"Sé\"}"));assertEquals("São Paulo",repository.lookup("01001000").city);assertEquals("/api/cep/v1/01001000",server.takeRequest().getPath());}
    @Test public void notFoundProducesUsefulError(){server.enqueue(new MockResponse().setResponseCode(404).setBody("{}"));assertEquals("CEP não encontrado.",assertThrows(java.io.IOException.class,()->repository.lookup("00000000")).getMessage());}
}
