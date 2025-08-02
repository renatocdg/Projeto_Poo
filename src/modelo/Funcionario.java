package modelo;

public class Funcionario  {
	
	private String nome;
	private String login;
	private String senha;
	private TFuncionario tipo;

	public Funcionario(String nome, String login, String senha, TFuncionario tipo) {
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

	public TFuncionario getTipo() {
		return tipo;
	}

	public void setTipo(TFuncionario tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return nome + " (" + tipo + ")";
	}
}