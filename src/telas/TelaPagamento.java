package telas;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.swing.*;
import dao.*;
import modelo.*;

public class TelaPagamento extends JFrame {

	private JComboBox<String> comboMultas;
	private JComboBox<MetodoPagamento> comboMetodoPagamento;
	private JButton btnPagar;

	public TelaPagamento() {

		setTitle("Pagamento de Multa");
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(5, 1, 10, 10));

// Verificar permissões antes de continuar
		if (!verificarPermissao()) {
			return;
		}

		comboMultas = new JComboBox<>();
		carregarMultasPendentes();

		comboMetodoPagamento = new JComboBox<>(MetodoPagamento.values());
		btnPagar = new JButton("Realizar Pagamento");
		btnPagar.addActionListener(this::realizarPagamento);

		add(new JLabel("Selecione uma multa:"));
		add(comboMultas);
		add(new JLabel("Método de pagamento:"));
		add(comboMetodoPagamento);
		add(btnPagar);

		if (comboMultas.getItemCount() == 0) {
			JOptionPane.showMessageDialog(this, "Não há multas pendentes para pagamento.", "Informação",
					JOptionPane.INFORMATION_MESSAGE);
			btnPagar.setEnabled(false);
		}

		setVisible(true);
	}

	private boolean verificarPermissao() {
		Funcionario logado = FuncionarioDao.funcionarioLogado();
		if (logado == null || logado.getTipo() == TipoFuncionario.ESTAGIARIO) {
			JOptionPane.showMessageDialog(this, "Apenas bibliotecários e administradores podem receber pagamentos.",
					"Acesso Negado", JOptionPane.WARNING_MESSAGE);
			dispose();
			return false;
		}
		return true;
	}

	private void carregarMultasPendentes() {
		comboMultas.removeAllItems();
		List<Multa> multas = MultaDao.carregarMultas();

		if (multas.isEmpty()) {
			comboMultas.addItem("Nenhuma multa pendente");
			return;
		}

		for (Multa multa : multas) {
			if (!multa.isQuitada()) {
				Usuario usuario = recuperarUsuarioPorMulta(multa);
				comboMultas.addItem(String.format("%s | %s (Mat: %s) | R$%.2f | %d dias atraso", multa.getId(),
						usuario.getNome(), usuario.getMatricula(), multa.getValor(), multa.getDiasAtraso()));
			}
		}
	}

	private void realizarPagamento(ActionEvent e) {
		if (comboMultas.getSelectedIndex() <= 0) {
			JOptionPane.showMessageDialog(this, "Selecione uma multa válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<Multa> multas = MultaDao.carregarMultas();
		List<Multa> multasNaoQuitadas = multas.stream().filter(m -> !m.isQuitada()).toList();

		if (comboMultas.getSelectedIndex() > multasNaoQuitadas.size()) {
			JOptionPane.showMessageDialog(this, "Multa inválida ou já quitada.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Multa multaSelecionada = multasNaoQuitadas.get(comboMultas.getSelectedIndex() - 1);
		Usuario usuario = recuperarUsuarioPorMulta(multaSelecionada);

		// Confirmação
		int confirmacao = JOptionPane.showConfirmDialog(this,
				String.format("Confirmar pagamento?\n\nUsuário: %s\nMatrícula: %s\nValor: R$%.2f\nDias em atraso: %d",
						usuario.getNome(), usuario.getMatricula(), multaSelecionada.getValor(),
						multaSelecionada.getDiasAtraso()),
				"Confirmar Pagamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirmacao != JOptionPane.YES_OPTION) {
			return;
		}

		// Processar pagamento
		Pagamento pagamento = new Pagamento(UUID.randomUUID().toString(), multaSelecionada.getId(),
				multaSelecionada.getValor(), LocalDate.now(), (MetodoPagamento) comboMetodoPagamento.getSelectedItem(),
				usuario.getMatricula());

		// Atualizar multa
		multaSelecionada.quitar();
		multaSelecionada.setDataQuitacao(LocalDate.now());

		// Salvar alterações
		MultaDao.salvar(multas);

		List<Pagamento> pagamentos = PagamentoDao.carregar();
		pagamentos.add(pagamento);
		PagamentoDao.salvar(pagamentos);

		JOptionPane.showMessageDialog(this,
				String.format("Pagamento registrado com sucesso!\n\nComprovante: %s\nData: %s", pagamento.getId(),
						LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
				"Sucesso", JOptionPane.INFORMATION_MESSAGE);

		carregarMultasPendentes();
		btnPagar.setEnabled(comboMultas.getItemCount() > 0);
		comboMetodoPagamento.setSelectedIndex(0);
	}

	private Usuario recuperarUsuarioPorMulta(Multa multa) {
		return EmprestimoDao.carregar().stream()
				.filter(emp -> emp.getObra().getCodigo() == multa.getEmprestimo().getObra().getCodigo()).findFirst()
				.map(Emprestimo::getUsuario).orElse(new Usuario("", "Usuário Desconhecido", TipoUsuario.ALUNO, "", ""));
	}
}