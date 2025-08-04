package modelo;

public class Livro extends Obra {

//construtor
	public Livro(int Codigo, String Titulo, String Autor, int AnoPublicacao, String Status, int TempoEmprestimo) {
		super(Codigo, Titulo, Autor, AnoPublicacao, Status, TempoEmprestimo);
	}

	public int getTempoEmprestimo() {
		return 7;
	}
}