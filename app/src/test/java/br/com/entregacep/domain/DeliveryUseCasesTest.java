package br.com.entregacep.domain;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
public class DeliveryUseCasesTest {
    static class Memory implements DeliveryRepository {
        DeliveryPoint point; String queried;
        public List<DeliveryPoint> list(boolean deleted){return point!=null&&(point.deletedAt!=null)==deleted?Collections.singletonList(point):Collections.emptyList();}
        public DeliveryPoint find(long id){return point;}
        public void save(DeliveryPoint p){point=p;p.id=1;}
        public void setDeleted(long id,Long t){point.deletedAt=t;}
        public Address lookup(String cep){queried=cep;return new Address(cep,"Praça da Sé","Sé","São Paulo","SP");}
    }
    @Test public void formattedCepIsNormalizedBeforeApi() throws Exception {Memory m=new Memory();new DeliveryUseCases(m).lookup("01001-000");assertEquals("01001000",m.queried);}
    @Test public void invalidCepNeverReachesApi(){Memory m=new Memory();assertThrows(IllegalArgumentException.class,()->new DeliveryUseCases(m).lookup("123"));assertNull(m.queried);}
    @Test public void deletePreservesRecordAndRestoreReturnsIt(){Memory m=new Memory();DeliveryUseCases c=new DeliveryUseCases(m);DeliveryPoint p=valid();c.save(p);c.delete(p.id);assertTrue(c.list(false).isEmpty());assertEquals(1,c.list(true).size());assertNotNull(c.find(p.id).deletedAt);c.restore(p.id);assertEquals(1,c.list(false).size());assertNull(p.deletedAt);}
    @Test public void missingRecipientCannotBeSaved(){Memory m=new Memory();DeliveryPoint p=valid();p.recipient=" ";assertThrows(IllegalArgumentException.class,()->new DeliveryUseCases(m).save(p));assertNull(m.point);}
    static DeliveryPoint valid(){DeliveryPoint p=new DeliveryPoint();p.name="Loja";p.recipient="Recepção";p.cep="01001000";p.street="Praça da Sé";p.number="10";p.city="São Paulo";p.state="SP";return p;}
}
