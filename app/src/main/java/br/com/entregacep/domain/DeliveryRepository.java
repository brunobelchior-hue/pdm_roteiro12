package br.com.entregacep.domain;
import java.util.List;
public interface DeliveryRepository {
    List<DeliveryPoint> list(boolean deleted);
    DeliveryPoint find(long id);
    void save(DeliveryPoint point);
    void setDeleted(long id, Long timestamp);
    Address lookup(String cep) throws Exception;
}
