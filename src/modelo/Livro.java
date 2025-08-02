package modelo;

public class Livro extends Obra {

//construtor
	public Livro(int Codigo, String Titulo, String Autor, int AnoPublicacao, String Status) {
		super(Codigo, Titulo, Autor, AnoPublicacao, Status);
	}

	public int getTempoEmprestimo() {
		return 7;
	}
}