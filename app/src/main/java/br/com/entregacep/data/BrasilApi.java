package br.com.entregacep.data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface BrasilApi {
    @GET("api/cep/v1/{cep}") Call<CepDto> lookup(@Path("cep") String cep);
    class CepDto { public String cep, state, city, neighborhood, street; }
}
