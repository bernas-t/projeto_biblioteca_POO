package repository;

import exception.PersistenciaException;
import java.util.List;
import model.Identificavel;

public interface Repositorio<T extends Identificavel> {
    void adicionar(T item) throws PersistenciaException;

    T buscarPorId(int id);

    List<T> listarTodos();

    void atualizar(T item) throws PersistenciaException;

    void remover(int id) throws PersistenciaException;

    int proximoId();
}
