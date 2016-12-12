package com.izv.dam.newquip.vistas.notas;

import android.content.Context;

import com.izv.dam.newquip.basedatos.AyudanteOrm;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Mapas;
import com.izv.dam.newquip.pojo.Nota;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class ModeloNota implements ContratoNota.InterfaceModelo {

    private GestionNota gn = null;
    private Dao<Mapas, Integer> dao;

    public ModeloNota(Context c) throws SQLException {


        gn = new GestionNota(c);

        AyudanteOrm ayu = new AyudanteOrm(c);
        ayu.getWritableDatabase();
        dao = ayu.getDao();
    }

    @Override
    public void close() {
        gn.close();
    }

    @Override
    public Nota getNota(long id) {
        return gn.get(id);
    }

    @Override
    public long saveNota(Nota n) {
        long r;
        if(n.getId()==0) {
             r = this.insertNota(n);
        } else {
            r = this.updateNota(n);
        }
        return r;
    }

    @Override
    public void saveMap(Mapas mapa) {

        try {
            dao.create(mapa);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Mapas> getMapas(long idNota) {

        QueryBuilder<Mapas, Integer> query = dao.queryBuilder();

        try {
            query.setWhere(query.where().eq("idNota", idNota));

            return dao.query(query.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private long deleteNota(Nota n) {
        return gn.delete(n);
    }

    private long insertNota(Nota n) {
        if(n.getNota().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            return 0;
        }
        return gn.insert(n);
    }

    private long updateNota(Nota n) {
        if(n.getNota().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            this.deleteNota(n);
            gn.delete(n);
            return 0;
        }
        return gn.update(n);
    }
}