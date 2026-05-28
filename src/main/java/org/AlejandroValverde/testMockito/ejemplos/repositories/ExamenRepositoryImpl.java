package org.AlejandroValverde.testMockito.ejemplos.repositories;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository{


    @Override
    public List<Examen> findAll() {
        return Arrays.asList(new Examen(5L,"Matematicas"),
                new Examen(6L,"Lengua"),
                new Examen(7L,"Historia"),
                new Examen(8L,"Fisica"));
    }
}
