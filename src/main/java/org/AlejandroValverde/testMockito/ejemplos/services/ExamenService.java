package org.AlejandroValverde.testMockito.ejemplos.services;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);
}
