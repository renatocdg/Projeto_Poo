package dao;

import modelo.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
	private static List<Usuario> usuarios = new ArrayList<>();

	// Dados iniciais de usuários

	static {
		usuarios.add(new Usuario("1", "João Silva", TipoUsuario.ALUNO, "joao@email.com", "11987654321"));
		usuarios.add(new Usuario("2", "Maria Souza", TipoUsuario.PROFESSOR, "maria@email.com", "11912345678"));
		usuarios.add(new Usuario("3", "Carlos Oliveira", TipoUsuario.SERVIDOR, "carlos@email.com", "11955556666"));
	}

	public static List<Usuario> carregar() {
		return new ArrayList<>(usuarios);
	}

	public static void salvar(List<Usuario> novaLista) {
		usuarios = new ArrayList<>(novaLista);
	}

	public static Usuario buscarPorMatricula(String matricula) {
		return usuarios.stream().filter(u -> u.getMatricula().equalsIgnoreCase(matricula.trim())).findFirst()
				.orElse(null);
	}

	// Método para debug
	public static void imprimirUsuarios() {
		System.out.println("LISTA DE USUÁRIOS");
		usuarios.forEach(u -> System.out
				.println("Matrícula: " + u.getMatricula() + " | Nome: " + u.getNome() + " | Tipo: " + u.getTipo()));
	}
}