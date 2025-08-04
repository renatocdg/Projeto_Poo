package controle;

import java.util.List;
import java.util.ArrayList;
import modelo.*;
import dao.*;

public class ControleUsuario {

	//Classes de exceção
	public static class UsuarioJaCadastradoException extends Exception {
		public UsuarioJaCadastradoException(String matricula) {
			super("Já existe um usuário cadastrado com a matrícula: " + matricula);
		}
	}

	public static class UsuarioNaoEncontradoException extends Exception {
		public UsuarioNaoEncontradoException(String matricula) {
			super("Usuário não encontrado com a matrícula: " + matricula);
		}
	}

	public static class DadosInvalidosException extends Exception {
		public DadosInvalidosException(String mensagem) {
			super(mensagem);
		}
	}

	private List<Usuario> listaUsuarios;

	//Construtor
	public ControleUsuario() {
		this.listaUsuarios = UsuarioDao.carregar();
	}

	//Métodos principais
	public void cadastrarUsuario(Usuario usuario) throws UsuarioJaCadastradoException, DadosInvalidosException {

		validarDadosUsuario(usuario);

		if (buscarPorMatricula(usuario.getMatricula()) != null) {
			throw new UsuarioJaCadastradoException(usuario.getMatricula());
		}

		listaUsuarios.add(usuario);
		UsuarioDao.salvar(listaUsuarios);
	}

	public void editarUsuario(String matricula, String novoTelefone, String novoEmail)
			throws UsuarioNaoEncontradoException, DadosInvalidosException {

		Usuario usuario = buscarPorMatricula(matricula);
		if (usuario == null) {
			throw new UsuarioNaoEncontradoException(matricula);
		}

		if (novoTelefone == null || novoTelefone.trim().isEmpty()) {
			throw new DadosInvalidosException("Telefone não pode ser vazio");
		}

		if (novoEmail == null || !novoEmail.contains("@")) {
			throw new DadosInvalidosException("E-mail inválido");
		}

		usuario.setTelefone(novoTelefone);
		usuario.setEmail(novoEmail);
		UsuarioDao.salvar(listaUsuarios);
	}

	public void excluirUsuario(String matricula) throws UsuarioNaoEncontradoException {
		Usuario usuario = buscarPorMatricula(matricula);
		if (usuario == null) {
			throw new UsuarioNaoEncontradoException(matricula);
		}

		listaUsuarios.remove(usuario);
		UsuarioDao.salvar(listaUsuarios);
	}

	//Métodos de consulta
	public Usuario buscarPorMatricula(String matricula) {
		return listaUsuarios.stream().filter(u -> u.getMatricula().equalsIgnoreCase(matricula)).findFirst().orElse(null);
	}

	public List<Usuario> listarTodos() {
		return new ArrayList<>(listaUsuarios); // Retorna cópia para evitar modificações externas
	}

	//Métodos auxiliares
	private void validarDadosUsuario(Usuario usuario) throws DadosInvalidosException {
		if (usuario.getMatricula() == null || usuario.getMatricula().trim().isEmpty()) {
			throw new DadosInvalidosException("Matrícula não pode ser vazia");
		}

		if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
			throw new DadosInvalidosException("Nome não pode ser vazio");
		}

		if (usuario.getTelefone() == null || usuario.getTelefone().trim().isEmpty()) {
			throw new DadosInvalidosException("Telefone não pode ser vazio");
		}

		if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
			throw new DadosInvalidosException("E-mail inválido");
		}
	}
}