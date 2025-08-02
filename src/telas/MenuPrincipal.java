package telas;

import javax.swing.*;
import java.awt.*;
import modelo.*;
import relatorios.RelatorioPdf;

public class MenuPrincipal extends JFrame {
	public MenuPrincipal(Funcionario funcionario) {
		configurarJanela(funcionario);
		adicionarComponentes(funcionario);
		setVisible(true);
	}

	private void configurarJanela(Funcionario funcionario) {
		setTitle("Menu Principal - " + funcionario.getNome() + " (" + funcionario.getTipo() + ")");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void adicionarComponentes(Funcionario funcionario) {
		// Painel principal com borda
		JPanel painelPrincipal = new JPanel();
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

		// Cabeçalho
		JLabel lblTitulo = new JLabel("Menu Principal");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		painelPrincipal.add(lblTitulo);

		JLabel lblUsuario = new JLabel("Usuário: " + funcionario.getNome());
		lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
		painelPrincipal.add(lblUsuario);

		JLabel lblTipo = new JLabel("Perfil: " + funcionario.getTipo());
		lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
		painelPrincipal.add(lblTipo);

		painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

		// Painel de botões
		JPanel painelBotoes = new JPanel();
		painelBotoes.setLayout(new GridLayout(0, 1, 5, 5));

		adicionarBotaoMenu(painelBotoes, "Consultar Obras", () -> new TelaConsultaObras());

// Botões por tipo de funcionário
		switch (funcionario.getTipo()) {
		case ADMINISTRADOR:
			adicionarBotaoMenu(painelBotoes, "Cadastrar Funcionário", () -> new CadastroFuncionario());
			adicionarBotaoMenu(painelBotoes, "Cadastrar Usuário", () -> new TelaUsuario());
			adicionarBotaoMenu(painelBotoes, "Cadastrar Obra", () -> new TelaObra());
			adicionarBotaoMenu(painelBotoes, "Registrar Empréstimo", () -> new TelaEmprestimo());
			adicionarBotaoMenu(painelBotoes, "Registrar Devolução", () -> new TelaDevolucao());
			adicionarBotaoMenu(painelBotoes, "Pagamento de Multas", () -> new TelaPagamento());
			adicionarBotaoMenu(painelBotoes, "Gerar Relatórios", this::exibirMenuRelatorios);
			break;

		case BIBLIOTECARIO:
			adicionarBotaoMenu(painelBotoes, "Cadastrar Usuário", () -> new TelaUsuario());
			adicionarBotaoMenu(painelBotoes, "Cadastrar Obra", () -> new TelaObra());
			adicionarBotaoMenu(painelBotoes, "Registrar Empréstimo", () -> new TelaEmprestimo());
			adicionarBotaoMenu(painelBotoes, "Registrar Devolução", () -> new TelaDevolucao());
			adicionarBotaoMenu(painelBotoes, "Pagamento de Multas", () -> new TelaPagamento());
			adicionarBotaoMenu(painelBotoes, "Gerar Relatórios", this::exibirMenuRelatorios);
			break;

		case ESTAGIARIO:
			adicionarBotaoMenu(painelBotoes, "Registrar Devolução", () -> new TelaDevolucao());
			break;
		}

		painelPrincipal.add(painelBotoes);
		add(painelPrincipal);
	}

	private void adicionarBotaoMenu(JPanel painel, String texto, Runnable acao) {
		JButton btn = new JButton(texto);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, btn.getMinimumSize().height));
		btn.addActionListener(e -> acao.run());
		painel.add(btn);
	}

	private void exibirMenuRelatorios() {
		JDialog dialog = new JDialog(this, "Gerar Relatórios", true);
		dialog.setSize(300, 200);
		dialog.setLayout(new GridLayout(0, 1, 10, 10));
		dialog.setLocationRelativeTo(this);

		JPanel painel = new JPanel();
		painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

		JLabel titulo = new JLabel("Selecione o Relatório");
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		painel.add(titulo);
		painel.add(Box.createRigidArea(new Dimension(0, 15)));

		adicionarBotaoRelatorio(painel, "Empréstimos do Mês", RelatorioPdf::gerarRelatorioEmprestimosDoMes);
		adicionarBotaoRelatorio(painel, "Obras Mais Emprestadas", RelatorioPdf::gerarRelatorioObrasMaisEmprestadas);
		adicionarBotaoRelatorio(painel, "Usuários com Atrasos", RelatorioPdf::gerarRelatorioUsuariosComMaisAtrasos);

		dialog.add(painel);
		dialog.setVisible(true);
	}

	private void adicionarBotaoRelatorio(JPanel painel, String texto, Runnable acao) {
		JButton btn = new JButton(texto);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.addActionListener(e -> {
			acao.run();
			((JDialog) btn.getTopLevelAncestor()).dispose();
		});
		painel.add(btn);
		painel.add(Box.createRigidArea(new Dimension(0, 5)));
	}
}