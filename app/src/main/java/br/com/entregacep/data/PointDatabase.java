package br.com.entregacep.data;
import androidx.room.*;
@Database(entities={PointEntity.class},version=1,exportSchema=true)
public abstract class PointDatabase extends RoomDatabase { public abstract PointDao points(); }
