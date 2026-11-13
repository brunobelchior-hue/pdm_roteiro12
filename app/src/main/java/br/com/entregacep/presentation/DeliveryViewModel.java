package br.com.entregacep.presentation;
import android.app.Application;
import androidx.lifecycle.*;
import br.com.entregacep.EntregaApp;
import br.com.entregacep.domain.*;
import java.util.List;
import java.util.concurrent.*;
public class DeliveryViewModel extends AndroidViewModel {
    public final MutableLiveData<List<DeliveryPoint>> points=new MutableLiveData<>();
    public final MutableLiveData<String> error=new MutableLiveData<>();
    private final ExecutorService worker=Executors.newSingleThreadExecutor();
    private final DeliveryUseCases cases;
    public DeliveryViewModel(Application app) {super(app);cases=((EntregaApp)app).useCases;}
    public void refresh(boolean deleted) {worker.execute(()->{try{points.postValue(cases.list(deleted));}catch(Exception e){error.postValue(e.getMessage());}});}
    public void toggle(long id,boolean restore,boolean deleted) {worker.execute(()->{try{if(restore)cases.restore(id);else cases.delete(id);points.postValue(cases.list(deleted));}catch(Exception e){error.postValue(e.getMessage());}});}
    @Override protected void onCleared(){worker.shutdown();}
}
