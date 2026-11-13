package br.com.entregacep.domain;
import java.util.List;
public class DeliveryUseCases {
    private final DeliveryRepository repository;
    public DeliveryUseCases(DeliveryRepository repository) { this.repository=repository; }
    public static String normalizeCep(String value) {
        String cep=value == null ? "" : value.replaceAll("[^0-9]", "");
        if (cep.length()!=8) throw new IllegalArgumentException("Informe um CEP com 8 dígitos.");
        return cep;
    }
    public Address lookup(String cep) throws Exception { return repository.lookup(normalizeCep(cep)); }
    public List<DeliveryPoint> list(boolean deleted) { return repository.list(deleted); }
    public DeliveryPoint find(long id) { return repository.find(id); }
    public void save(DeliveryPoint p) {
        p.cep=normalizeCep(p.cep);
        if(p.name.trim().isEmpty() || p.recipient.trim().isEmpty() || p.number.trim().isEmpty() || p.street.trim().isEmpty() || p.city.trim().isEmpty() || !p.state.matches("[A-Z]{2}"))
            throw new IllegalArgumentException("Preencha nome do ponto, destinatário, número, rua, cidade e UF.");
        repository.save(p);
    }
    public void delete(long id) { repository.setDeleted(id,System.currentTimeMillis()); }
    public void restore(long id) { repository.setDeleted(id,null); }
}
