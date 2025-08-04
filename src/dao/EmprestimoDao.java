package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.*;

public class EmprestimoDao {
	private static List<Emprestimo> emprestimos = new ArrayList<>();
	private static int ultimoId = 0;

	public static List<Emprestimo> carregar() {
		return new ArrayList<>(emprestimos); // Retorna cópia para evitar modificações externas
	}

	public static void salvar(Emprestimo novoEmprestimo) {
		novoEmprestimo.setId(++ultimoId); // Atribui um ID único
		emprestimos.add(novoEmprestimo);
	}

	public static void salvar(List<Emprestimo> lista) {
		emprestimos.clear();
		lista.forEach(emp -> {
			if (emp.getId() == 0) {
				emp.setId(++ultimoId);
			}
			emprestimos.add(emp);
		});
	}

	// Método para limpar todos os empréstimos (útil para testes)
	public static void limparDados() {
		emprestimos.clear();
		ultimoId = 0;
	}
}