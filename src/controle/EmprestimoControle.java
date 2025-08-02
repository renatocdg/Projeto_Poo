package controle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import dao.*;
import modelo.*;

public class EmprestimoControle {
	private static List<Emprestimo> lista = EmprestimoDao.carregar();

	public static void registrarEmprestimo(String matricula, String codigoStr) throws Exception {

// Converter para int (se necessário)
		int codigo = Integer.parseInt(codigoStr);

// Buscar obra
		Obra obra = ObraDao.buscarPorCodigo(codigo);
		if (obra == null || !obra.getStatus().equalsIgnoreCase("disponível")) {
			throw new Exception("Obra não disponível para empréstimo");
		}
	}

	private static Usuario buscarUsuario(String matricula) throws Exception {
		Usuario usuario = UsuarioDao.carregar().stream().filter(u -> u.getMatricula().equalsIgnoreCase(matricula))
				.findFirst().orElse(null);

		if (usuario == null) {
			throw new Exception("Usuário não encontrado");
		}
		return usuario;
	}

	public static void registrarDevolucao(String matricula, String codigo) throws Exception {
		List<Emprestimo> emprestimos = EmprestimoDao.carregar();
		List<Multa> multas = MultaDao.carregarMultas();

		boolean encontrado = false;

		for (Emprestimo emp : emprestimos) {
			boolean usuarioOk = emp.getUsuario().getMatricula().equalsIgnoreCase(matricula);
			boolean obraOk = emp.getObra().getCodigo() == Integer.parseInt(codigo);
			boolean naoDevolvido = emp.getDataDevolucao() == null;

			if (usuarioOk && obraOk && naoDevolvido) {
				emp.setDataDevolucao(LocalDate.now());

				emp.getObra().setStatus("disponível");
				atualizarStatusObra(emp.getObra().getCodigo(), "disponível");

				if (emp.getDataDevolucao().isBefore(LocalDate.now())) {
					long diasAtraso = ChronoUnit.DAYS.between(emp.getDataDevolucao(), LocalDate.now());
					double valorMulta = diasAtraso * 1.5;

					Multa multa = new Multa("1", emp, valorMulta, (int) diasAtraso);
					multas.add(multa);
					MultaDao.salvar(multas);
				}

				EmprestimoDao.salvar(emprestimos);
				encontrado = true;
				break;
			}
		}

		if (!encontrado) {
			throw new Exception("Empréstimo não encontrado ou já devolvido.");
		}
	}

	private static void atualizarStatusObra(int codigo, String status) {
		List<Obra> obras = ObraDao.carregarTodas();
		obras.stream().filter(o -> o.getCodigo() == codigo).findFirst().ifPresent(o -> o.setStatus(status));
		ObraDao.salvarTodas(obras);
	}
}