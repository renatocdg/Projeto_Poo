package dao;

import modelo.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ObraDao {
	private static List<Obra> obras = new ArrayList<>();
	private static AtomicInteger ultimoCodigo = new AtomicInteger(3); // Começa com 3 por causa dos dados iniciais

	// Dados iniciais
	static {
		obras.add(new Livro(1, "Dom Casmurro", "Machado de Assis", 1899, "Disponível", 15));
		obras.add(new Revista(2, "Ciência Hoje", "Sociedade Brasileira para o Progresso da Ciência", 2023, "Disponível",
				7));
		obras.add(new Artigo(3, "Padrões de Design", "Erich Gamma", 1994, "Disponível", 5));
	}

	public static List<Obra> carregarTodas() {
		return new ArrayList<>(obras); // Retorna cópia para evitar modificações externas
	}

	public static Obra buscarPorCodigo(int codigo) {
		return obras.stream().filter(o -> o.getCodigo() == codigo).findFirst().orElse(null);
	}

	public static boolean salvarTodas(List<Obra> novasObras) {
		obras = new ArrayList<>(novasObras);
		atualizarUltimoCodigo();
		return true;
	}

	public static boolean adicionarObra(Obra obra) {
		try {
			// Verifica se código já existe
			if (buscarPorCodigo(obra.getCodigo()) != null) {
				return false;
			}

			// Atribui novo código se necessário
			if (obra.getCodigo() == 0) {
				obra.setCodigo(ultimoCodigo.incrementAndGet());
			} else {
				// Atualiza último código se for maior
				ultimoCodigo.updateAndGet(current -> Math.max(current, obra.getCodigo()));
			}

			obras.add(obra);
			return true;
		} catch (Exception e) {
			System.err.println("Erro ao adicionar obra: " + e.getMessage());
			return false;
		}
	}

	public static boolean atualizarObra(Obra obraAtualizada) {
		try {
			for (int i = 0; i < obras.size(); i++) {
				if (obras.get(i).getCodigo() == obraAtualizada.getCodigo()) {
					obras.set(i, obraAtualizada);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			System.err.println("Erro ao atualizar obra: " + e.getMessage());
			return false;
		}
	}

	public static boolean removerObra(int codigo) {
		return obras.removeIf(o -> o.getCodigo() == codigo);
	}

	private static void atualizarUltimoCodigo() {
		ultimoCodigo.set(obras.stream().mapToInt(Obra::getCodigo).max().orElse(0));
	}

	// Método auxiliar para debug
	public static void imprimirObras() {
		System.out.println("\n=== LISTA DE OBRAS ===");
		System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s%n", "Código", "Título", "Tipo", "Status", "Dias Empr.");

		obras.forEach(o -> System.out.printf("%-5d | %-20s | %-10s | %-10s | %-10d%n", o.getCodigo(), o.getTitulo(),
				o.getClass().getSimpleName(), o.getStatus(), o.getTempoEmprestimo()));
	}
}