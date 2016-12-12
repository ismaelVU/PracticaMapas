package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.izv.dam.newquip.pojo.Mapas;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by dam on 07/12/2016.
 */
public class AyudanteOrm extends OrmLiteSqliteOpenHelper {
    public AyudanteOrm(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
    }

    public AyudanteOrm(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    private Dao<Mapas, Integer> simpleDao = null;
    private RuntimeExceptionDao<Mapas , Integer> simpleRunTime = null;


    public AyudanteOrm(Context context){
        super(context, "ormlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            TableUtils.createTable(connectionSource, Mapas.class);

        }catch (SQLException e){

        }
    }


    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    public Dao<Mapas , Integer> getDao() throws SQLException{
        if(simpleDao == null){
            simpleDao= getDao(Mapas.class);
        }
        return simpleDao;
    }

    public RuntimeExceptionDao <Mapas , Integer> getDataDao (){
        if(simpleRunTime== null){
            simpleRunTime= getRuntimeExceptionDao(Mapas.class);
        }
        return simpleRunTime;
    }


    @Override
    public void close(){
        super.close();
        simpleDao=null;
        simpleRunTime= null;
    }
}
