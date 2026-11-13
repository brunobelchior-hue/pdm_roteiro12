package br.com.entregacep.data;
import androidx.room.*;
import java.util.List;
@Dao public interface PointDao {
    @Query("SELECT * FROM delivery_points WHERE (:deleted = 0 AND deletedAt IS NULL) OR (:deleted = 1 AND deletedAt IS NOT NULL) ORDER BY name COLLATE NOCASE") List<PointEntity> list(boolean deleted);
    @Query("SELECT * FROM delivery_points WHERE id=:id") PointEntity find(long id);
    @Insert long insert(PointEntity point);
    @Update int update(PointEntity point);
    @Query("UPDATE delivery_points SET deletedAt=:timestamp WHERE id=:id") int setDeleted(long id, Long timestamp);
}
