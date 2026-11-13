package br.com.entregacep;
import android.app.Application;
import androidx.room.Room;
import br.com.entregacep.data.*;
import br.com.entregacep.domain.DeliveryUseCases;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
public class EntregaApp extends Application {
    public DeliveryUseCases useCases;
    @Override public void onCreate() {
        super.onCreate();
        PointDatabase db=Room.databaseBuilder(this,PointDatabase.class,"entrega-cep.db").build();
        OkHttpClient http=new OkHttpClient.Builder().connectTimeout(15,TimeUnit.SECONDS).readTimeout(15,TimeUnit.SECONDS).callTimeout(25,TimeUnit.SECONDS).build();
        BrasilApi api=new Retrofit.Builder().baseUrl("https://brasilapi.com.br/").client(http).addConverterFactory(GsonConverterFactory.create()).build().create(BrasilApi.class);
        useCases=new DeliveryUseCases(new RoomDeliveryRepository(db.points(),api));
    }
}
