package org.AlejandroValverde.testMockito.ejemplos.repositories;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();
}
