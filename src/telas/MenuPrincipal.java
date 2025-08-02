package telas;

import javax.swing.*;
import modelo.*;
import relatorios.RelatorioPdf;
import java.awt.*;

public class MenuPrincipal extends JFrame {

	public MenuPrincipal(Funcionario funcionario) {
		configurarJanela(funcionario);
		adicionarComponentes(funcionario);
		setVisible(true);
	}

	private void configurarJanela(Funcionario funcionario) {
		setTitle("Menu - " + funcionario.getNome() + " (" + funcionario.getTipo() + ")");
		setSize(350, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);
	}

	private void adicionarComponentes(Funcionario funcionario) {
		// Botões comuns a todos
		adicionarBotao("Consultar Obras", () -> new TelaConsultaObras());

		// Botões por tipo de funcionário
		switch (funcionario.getTipo()) {
		case ADMINISTRADOR:
			adicionarBotao("Cadastrar Funcionário", () -> new CadastroFuncionario());
			adicionarBotao("Cadastrar Usuário", () -> new TelaUsuario());
			adicionarBotao("Cadastrar Obra", () -> new TelaObra());
			adicionarBotao("Registrar Empréstimo", () -> new TelaEmprestimo());
			adicionarBotao("Registrar Devolução", () -> new TelaDevolucao());
			adicionarBotao("Pagamento de Multas", () -> new TelaPagamento());
			adicionarBotao("Gerar Relatórios", this::exibirMenuRelatorios);
			break;

		case BIBLIOTECARIO:
			adicionarBotao("Cadastrar Usuário", () -> new TelaUsuario());
			adicionarBotao("Cadastrar Obra", () -> new TelaObra());
			adicionarBotao("Registrar Empréstimo", () -> new TelaEmprestimo());
			adicionarBotao("Registrar Devolução", () -> new TelaDevolucao());
			adicionarBotao("Pagamento de Multas", () -> new TelaPagamento());
			adicionarBotao("Gerar Relatórios", this::exibirMenuRelatorios);
			break;

		case ESTAGIARIO:
			adicionarBotao("Registrar Devolução", () -> new TelaDevolucao());
			break;
		}
	}

	private void exibirMenuRelatorios() {
		JDialog dialog = new JDialog(this, "Gerar Relatórios", true);
		dialog.setSize(300, 200);
		dialog.setLayout(new GridLayout(3, 1, 10, 10));

		adicionarBotaoRelatorio(dialog, "Empréstimos do Mês", RelatorioPdf::gerarRelatorioEmprestimosDoMes);
		adicionarBotaoRelatorio(dialog, "Obras Mais Emprestadas", RelatorioPdf::gerarRelatorioObrasMaisEmprestadas);
		adicionarBotaoRelatorio(dialog, "Usuários com Atrasos", RelatorioPdf::gerarRelatorioUsuariosComMaisAtrasos);

		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private void adicionarBotao(String texto, Runnable acao) {
		JButton btn = new JButton(texto);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.addActionListener(e -> acao.run());
		add(btn);
	}

	private void adicionarBotaoRelatorio(JDialog dialog, String texto, Runnable acao) {
		JButton btn = new JButton(texto);
		btn.addActionListener(e -> {
			acao.run();
			dialog.dispose();
		});
		dialog.add(btn);
	}
}