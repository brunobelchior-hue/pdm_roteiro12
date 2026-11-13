package br.com.entregacep.domain;
public class Address {
    public final String cep, street, neighborhood, city, state;
    public Address(String cep, String street, String neighborhood, String city, String state) {
        this.cep=cep; this.street=street; this.neighborhood=neighborhood; this.city=city; this.state=state;
    }
}
