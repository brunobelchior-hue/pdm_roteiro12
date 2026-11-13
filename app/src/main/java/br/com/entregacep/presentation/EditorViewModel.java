package br.com.entregacep.presentation;
import android.app.Application;
import androidx.lifecycle.*;
import br.com.entregacep.EntregaApp;
import br.com.entregacep.domain.*;
import java.util.concurrent.*;
public class EditorViewModel extends AndroidViewModel {
    public final MutableLiveData<DeliveryPoint> loaded=new MutableLiveData<>();
    public final MutableLiveData<Address> address=new MutableLiveData<>();
    public final MutableLiveData<String> error=new MutableLiveData<>();
    public final MutableLiveData<Boolean> busy=new MutableLiveData<>(false),saved=new MutableLiveData<>(false);
    public String verifiedCep="";
    private final DeliveryUseCases cases;
    private final ExecutorService worker=Executors.newSingleThreadExecutor();
    public EditorViewModel(Application app){super(app);cases=((EntregaApp)app).useCases;}
    public void load(long id){worker.execute(()->{try{DeliveryPoint p=cases.find(id);if(p==null||p.deletedAt!=null)throw new IllegalStateException("Ponto indisponível.");verifiedCep=p.cep;loaded.postValue(p);}catch(Exception e){error.postValue(e.getMessage());}});}
    public void lookup(String cep){if(Boolean.TRUE.equals(busy.getValue()))return;busy.setValue(true);worker.execute(()->{try{Address a=cases.lookup(cep);verifiedCep=a.cep;address.postValue(a);}catch(Exception e){error.postValue(e instanceof java.io.IOException?"Falha na consulta: "+e.getMessage():e.getMessage());}finally{busy.postValue(false);}});}
    public void save(DeliveryPoint p){if(Boolean.TRUE.equals(busy.getValue()))return;try{if(!DeliveryUseCases.normalizeCep(p.cep).equals(verifiedCep))throw new IllegalArgumentException("Consulte o CEP antes de salvar.");}catch(Exception e){error.setValue(e.getMessage());return;}busy.setValue(true);worker.execute(()->{try{cases.save(p);saved.postValue(true);}catch(Exception e){error.postValue(e.getMessage());}finally{busy.postValue(false);}});}
    @Override protected void onCleared(){worker.shutdown();}
}
