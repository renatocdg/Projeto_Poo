package controle;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import dao.*;
import modelo.*;

public class EmprestimoControle {
	
	private static List<Emprestimo> emprestimosAtivos = new ArrayList<>();
	private static int ultimoId = 0;

	public static class StatusObra {
		public static final String DISPONIVEL = "Disponível";
		public static final String EMPRESTADO = "Emprestado";
	}

	public static void registrarEmprestimo(String matricula, String codigoStr) throws Exception {
		
		try {
			// Validação dos dados de entrada
			if (matricula == null || matricula.trim().isEmpty() || codigoStr == null || codigoStr.trim().isEmpty()) {
				throw new Exception("Matrícula e código da obra são obrigatórios");
			}

			// Conversão e busca dos dados
			int codigo = Integer.parseInt(codigoStr.trim());
			Usuario usuario = UsuarioDao.buscarPorMatricula(matricula);
			Obra obra = ObraDao.buscarPorCodigo(codigo);

			// Validações de negócio
			if (usuario == null) {
				throw new Exception("Usuário não encontrado com a matrícula: " + matricula);
			}

			if (obra == null) {
				throw new Exception("Obra não encontrada com o código: " + codigo);
			}

			if (!obra.getStatus().equalsIgnoreCase(StatusObra.DISPONIVEL)) {
				throw new Exception("A obra '" + obra.getTitulo() + "' não está disponível para empréstimo");
			}

			// Verifica se o usuário já tem a obra emprestada
			if (usuarioPossuiObraEmprestada(usuario, obra)) {
				throw new Exception("Usuário já possui esta obra emprestada");
			}

			// Cálculo da data de devolução
			int diasEmprestimo = obra.getTempoEmprestimo();
			LocalDate dataEmprestimo = LocalDate.now();
			LocalDate dataDevolucaoPrevista = dataEmprestimo.plusDays(diasEmprestimo);

			// Criação do empréstimo
			Emprestimo novoEmprestimo = new Emprestimo(obra, usuario);
			novoEmprestimo.setId(++ultimoId);
			novoEmprestimo.setDataEmprestimo(dataEmprestimo);

			// Atualização do status da obra
			obra.setStatus(StatusObra.EMPRESTADO);
			ObraDao.atualizarObra(obra);

			// Registro do empréstimo
			emprestimosAtivos.add(novoEmprestimo);

			// Mensagem de sucesso com detalhes
			String mensagem = String.format(
					"Empréstimo registrado com sucesso!\n\n" + "Usuário: %s\n" + "Obra: %s\n" + "Data de devolução: %s",
					usuario.getNome(), obra.getTitulo(),
					dataDevolucaoPrevista.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

			JOptionPane.showMessageDialog(null, mensagem);

		} catch (NumberFormatException e) {
			throw new Exception("Código da obra deve ser um número válido");
		} catch (Exception e) {
			throw new Exception("Erro ao registrar empréstimo: " + e.getMessage());
		}
	}

	private static boolean usuarioPossuiObraEmprestada(Usuario usuario, Obra obra) {
		return emprestimosAtivos.stream().anyMatch(
				e -> e.getUsuario().equals(usuario) && e.getObra().equals(obra) && e.getDataDevolucao() == null);
	}

	public static void registrarDevolucao(String matricula, String codigo) throws Exception {
		try {
			int codigoObra = Integer.parseInt(codigo.trim());
			boolean encontrado = false;

			for (Emprestimo emp : emprestimosAtivos) {
				boolean usuarioOk = emp.getUsuario().getMatricula().equalsIgnoreCase(matricula.trim());
				boolean obraOk = emp.getObra().getCodigo() == codigoObra;
				boolean naoDevolvido = emp.getDataDevolucao() == null;

				if (usuarioOk && obraOk && naoDevolvido) {
					emp.setDataDevolucao(LocalDate.now());
					emp.getObra().setStatus(StatusObra.DISPONIVEL);
					ObraDao.atualizarObra(emp.getObra());

					// Verificar atraso
					LocalDate dataPrevista = emp.getDataEmprestimo().plusDays(emp.getObra().getTempoEmprestimo());
					if (LocalDate.now().isAfter(dataPrevista)) {
						long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, LocalDate.now());
						double valorMulta = diasAtraso * 1.5; // R$1.50 por dia de atraso

						String multaId = "M" + System.currentTimeMillis();
						Multa multa = new Multa(multaId, emp, valorMulta, (int) diasAtraso);
						MultaDao.carregarMultas();
					}

					encontrado = true;
					break;
				}
			}

			if (!encontrado) {
				throw new Exception("Empréstimo não encontrado ou já devolvido");
			}

		} catch (NumberFormatException e) {
			throw new Exception("Código da obra inválido. Digite apenas números.");
		}
	}

	// Métodos auxiliares para acesso aos empréstimos
	public static List<Emprestimo> getEmprestimosAtivos() {
		return new ArrayList<>(emprestimosAtivos);
	}

	public static List<Emprestimo> getHistoricoCompleto() {
		
		return new ArrayList<>(emprestimosAtivos);
	}

	public static void limparDados() {
		emprestimosAtivos.clear();
		ultimoId = 0;
	}
}