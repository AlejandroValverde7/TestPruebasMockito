package org.AlejandroValverde.testMockito.ejemplos.repositories;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public Examen guardar(Examen examen) {
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        try{
            System.out.println("Examen repo otro");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return Datos.EXAMENES;
    }
}
