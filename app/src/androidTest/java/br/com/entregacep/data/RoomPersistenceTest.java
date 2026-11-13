package br.com.entregacep.data;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.room.Room;
import org.junit.*;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class RoomPersistenceTest {
    @Test public void editAndSoftDeleteSurviveDatabaseReopen(){
        android.content.Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        String name="persistence-test-"+System.nanoTime()+".db";
        PointDatabase db=Room.databaseBuilder(context,PointDatabase.class,name).build();
        try {
            PointEntity e=new PointEntity();e.name="Depósito";long id=db.points().insert(e);e.id=id;e.name="Depósito editado";assertEquals(1,db.points().update(e));db.points().setDeleted(id,123L);db.close();
            db=Room.databaseBuilder(context,PointDatabase.class,name).build();
            assertTrue(db.points().list(false).isEmpty());assertEquals("Depósito editado",db.points().list(true).get(0).name);assertEquals(Long.valueOf(123),db.points().find(id).deletedAt);
            db.points().setDeleted(id,null);assertEquals(1,db.points().list(false).size());
        } finally {db.close();context.deleteDatabase(name);}
    }
}
