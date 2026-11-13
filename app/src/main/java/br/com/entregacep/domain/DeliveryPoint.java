package br.com.entregacep.domain;

/** Domain model independent of Android, Room and Retrofit. */
public class DeliveryPoint {
    public long id;
    public String name="", recipient="", cep="", street="", number="", complement="", neighborhood="", city="", state="", instructions="";
    public Long deletedAt;
}
