package modelo;

public abstract class Obra {

	protected int Codigo;
	protected String Titulo;
	protected String Autor;
	protected int AnoPublicacao;
	protected String Status;

// Construtor
	public Obra(int Codigo, String Titulo, String Autor, int AnoPublicacao, String Status) {

		this.Codigo = Codigo;
		this.Titulo = Titulo;
		this.Autor = Autor;
		this.AnoPublicacao = AnoPublicacao;
		this.Status = Status;
	}

// Método tempo de empréstimo
	public abstract int getTempoEmprestimo();

// Método para visualizar o atributo status
	public String getStatus() {
		return Status;
	}

// Método para setar o status da obra
	public void setStatus(String status) {
		this.Status = status;
	}

	public int getCodigo() {
		return Codigo;
	}

	public String getTitulo() {
		return Titulo;
	}

	public String getAutor() {
		return Autor;
	}

	public int getAnoPublicacao() {
		return AnoPublicacao;
	}
}