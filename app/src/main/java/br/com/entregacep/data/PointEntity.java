package br.com.entregacep.data;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName="delivery_points")
public class PointEntity {
    @PrimaryKey(autoGenerate=true) public long id;
    public String name, recipient, cep, street, number, complement, neighborhood, city, state, instructions;
    public Long deletedAt;
}
