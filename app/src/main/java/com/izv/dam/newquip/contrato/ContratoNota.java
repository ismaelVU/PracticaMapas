package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Mapas;
import com.izv.dam.newquip.pojo.Nota;

import java.util.ArrayList;
import java.util.List;

public interface ContratoNota {

    interface InterfaceModelo {

        void close();

        Nota getNota(long id);

        long saveNota(Nota n);

        void saveMap(Mapas mapa);


        List<Mapas> getMapas(long idNota );
    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        long onSaveNota(Nota n);

        void onSaveMap(Mapas mapa);


        List<Mapas> onGetMapas( long idNota );
    }

    interface InterfaceVista {

        void mostrarNota(Nota n);

    }

}