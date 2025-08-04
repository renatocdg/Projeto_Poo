package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.*;

public class FuncionarioDao {
	
	private static final List<Funcionario> FUNCIONARIOS_PREDEFINIDOS = new ArrayList<>();
	private static Funcionario funcionarioLogado = null;

	// Bloco estático para inicializar os funcionários
	static {
		FUNCIONARIOS_PREDEFINIDOS.add(new Funcionario("Renato Muniz", "admin", "123", TipoFuncionario.ADMINISTRADOR));
		FUNCIONARIOS_PREDEFINIDOS
				.add(new Funcionario("Kaio Victor", "bibliotecario", "123", TipoFuncionario.BIBLIOTECARIO));
		FUNCIONARIOS_PREDEFINIDOS.add(new Funcionario("Juan Gabriel", "estagiario", "123", TipoFuncionario.ESTAGIARIO));
	}

	public static List<Funcionario> carregar() {
		return new ArrayList<>(FUNCIONARIOS_PREDEFINIDOS); // Retorna uma cópia
	}

	public static Funcionario autenticar(String login, String senha, TipoFuncionario tipo) {
		for (Funcionario f : FUNCIONARIOS_PREDEFINIDOS) {
			if (f.getLogin().equals(login) && f.getSenha().equals(senha) && f.getTipo() == tipo) {
				funcionarioLogado = f;
				return f;
			}
		}
		return null;
	}

	// Mantenha os demais métodos (funcionarioLogado, logout, temPermissao, etc.)
	public static Funcionario funcionarioLogado() {
		return funcionarioLogado;
	}

	public static void logout() {
		funcionarioLogado = null;
	}

	public static boolean temPermissao(TipoFuncionario tipoRequerido) {
		return funcionarioLogado != null && funcionarioLogado.getTipo() == tipoRequerido;
	}
}