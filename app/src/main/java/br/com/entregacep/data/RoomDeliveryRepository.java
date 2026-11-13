package br.com.entregacep.data;
import br.com.entregacep.domain.*;
import java.util.*;
import java.io.IOException;
import retrofit2.Response;
public class RoomDeliveryRepository implements DeliveryRepository {
    private final PointDao dao;
    private final BrasilApi api;
    public RoomDeliveryRepository(PointDao dao, BrasilApi api) {this.dao=dao; this.api=api;}
    public List<DeliveryPoint> list(boolean deleted) { List<DeliveryPoint> result=new ArrayList<>(); for(PointEntity e:dao.list(deleted)) result.add(from(e)); return result; }
    public DeliveryPoint find(long id) { PointEntity e=dao.find(id); return e==null ? null : from(e); }
    public void save(DeliveryPoint p) { PointEntity e=to(p); if(p.id==0) p.id=dao.insert(e); else if(dao.update(e)!=1) throw new IllegalStateException("Registro não encontrado."); }
    public void setDeleted(long id, Long timestamp) { if(dao.setDeleted(id,timestamp)!=1) throw new IllegalStateException("Registro não encontrado."); }
    public Address lookup(String cep) throws IOException {
        Response<BrasilApi.CepDto> response=api.lookup(cep).execute();
        if(!response.isSuccessful()) {
            if(response.errorBody()!=null) response.errorBody().close();
            throw new IOException(response.code()==404 ? "CEP não encontrado." : "Não foi possível consultar o CEP. Tente novamente.");
        }
        BrasilApi.CepDto d=response.body();
        if(d==null || d.city==null || d.state==null) throw new IOException("Resposta de CEP incompleta.");
        return new Address(cep,safe(d.street),safe(d.neighborhood),d.city,d.state);
    }
    private static String safe(String s) { return s==null?"":s; }
    public static PointEntity to(DeliveryPoint p) { PointEntity e=new PointEntity(); e.id=p.id; e.name=p.name; e.recipient=p.recipient; e.cep=p.cep; e.street=p.street; e.number=p.number; e.complement=p.complement; e.neighborhood=p.neighborhood; e.city=p.city; e.state=p.state; e.instructions=p.instructions; e.deletedAt=p.deletedAt; return e; }
    public static DeliveryPoint from(PointEntity e) { DeliveryPoint p=new DeliveryPoint(); p.id=e.id; p.name=e.name; p.recipient=e.recipient; p.cep=e.cep; p.street=e.street; p.number=e.number; p.complement=e.complement; p.neighborhood=e.neighborhood; p.city=e.city; p.state=e.state; p.instructions=e.instructions; p.deletedAt=e.deletedAt; return p; }
}
