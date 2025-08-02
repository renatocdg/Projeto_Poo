package modelo;

public class Artigo extends Obra {

// construtor
	public Artigo(int Codigo, String Titulo, String Autor, int AnoPublicacao, String Status) {
		super(Codigo, Titulo, Autor, AnoPublicacao, Status);
	}

	public int getTempoEmprestimo() {
		return 2;
	}
}
