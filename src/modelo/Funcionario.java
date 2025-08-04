package modelo;

public class Funcionario  {
	
	private String nome;
	private String login;
	private String senha;
	private TipoFuncionario tipo;

	public Funcionario(String nome, String login, String senha, TipoFuncionario tipo) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoFuncionario getTipo() {
		return tipo;
	}

	public void setTipo(TipoFuncionario tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return nome + " (" + tipo + ")";
	}
}