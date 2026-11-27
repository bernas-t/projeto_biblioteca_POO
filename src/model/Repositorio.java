package model;
import exception.PersistenciaException;
import java.util.List;

public interface Repositorio<T> {
    void adicionar(T item) throws PersistenciaException;
    T buscarPorId(int id);
    List<T> listarTodos();
    void atualizar(T item) throws PersistenciaException;
    void remover(int id) throws PersistenciaException;
}
