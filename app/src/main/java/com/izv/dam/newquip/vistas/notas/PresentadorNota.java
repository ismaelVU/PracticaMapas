package com.izv.dam.newquip.vistas.notas;

import android.content.Context;

import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Mapas;
import com.izv.dam.newquip.pojo.Nota;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PresentadorNota implements ContratoNota.InterfacePresentador {

    private ContratoNota.InterfaceModelo modelo;
    private ContratoNota.InterfaceVista vista;

    public PresentadorNota(ContratoNota.InterfaceVista vista) throws SQLException {
        this.vista = vista;
        this.modelo = new ModeloNota((Context)vista);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public long onSaveNota(Nota n) {
        return this.modelo.saveNota(n);
    }

    @Override
    public void onSaveMap(Mapas mapa) {

        modelo.saveMap(mapa);
    }

    @Override
    public List<Mapas> onGetMapas(long idNota) {

        return modelo.getMapas(idNota);
    }

}