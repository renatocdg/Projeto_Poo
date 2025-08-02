package telas;

import modelo.*;
import dao.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CadastroFuncionario extends JFrame {
	private JTextField campoNome;
	private JTextField campoLogin;
	private JPasswordField campoSenha;
	private JComboBox<TFuncionario> comboTipo;
	private JButton botaoSalvar;
	private JButton botaoExcluir;
	private JComboBox<Funcionario> comboFuncionarios;
	private Funcionario funcionarioSelecionado;

	public CadastroFuncionario() {
		setTitle("Gerenciamento de Funcionários");
		setSize(400, 350);
		setLayout(new GridLayout(7, 2, 10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Componentes
		comboFuncionarios = new JComboBox<>();
		campoNome = new JTextField();
		campoLogin = new JTextField();
		campoSenha = new JPasswordField();
		comboTipo = new JComboBox<>(TFuncionario.values());
		botaoSalvar = new JButton("Salvar");
		botaoExcluir = new JButton("Excluir");
		JButton botaoNovo = new JButton("Novo");

		// Layout
		add(new JLabel("Funcionário:"));
		add(comboFuncionarios);
		add(new JLabel("Nome:"));
		add(campoNome);
		add(new JLabel("Login:"));
		add(campoLogin);
		add(new JLabel("Senha:"));
		add(campoSenha);
		add(new JLabel("Tipo:"));
		add(comboTipo);
		add(botaoNovo);
		add(botaoSalvar);
		add(new JLabel());
		add(botaoExcluir);

		// Carrega funcionários
		carregarFuncionarios();

		// Listeners
		comboFuncionarios.addActionListener(e -> selecionarFuncionario());
		botaoSalvar.addActionListener(this::salvarFuncionario);
		botaoExcluir.addActionListener(this::excluirFuncionario);
		botaoNovo.addActionListener(e -> limparCampos());

		setVisible(true);
	}

	private void carregarFuncionarios() {
		comboFuncionarios.removeAllItems();
		List<Funcionario> funcionarios = FuncionarioDao.carregar();
		funcionarios.forEach(comboFuncionarios::addItem);
		comboFuncionarios.insertItemAt(null, 0);
		comboFuncionarios.setSelectedIndex(0);
	}

	private void selecionarFuncionario() {
		funcionarioSelecionado = (Funcionario) comboFuncionarios.getSelectedItem();
		if (funcionarioSelecionado != null) {
			campoNome.setText(funcionarioSelecionado.getNome());
			campoLogin.setText(funcionarioSelecionado.getLogin());
			campoSenha.setText(funcionarioSelecionado.getSenha());
			comboTipo.setSelectedItem(funcionarioSelecionado.getTipo());
		}
	}

	private void salvarFuncionario(ActionEvent e) {
		String nome = campoNome.getText().trim();
		String login = campoLogin.getText().trim();
		String senha = new String(campoSenha.getPassword()).trim();
		TFuncionario tipo = (TFuncionario) comboTipo.getSelectedItem();

		if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
			return;
		}

		List<Funcionario> lista = FuncionarioDao.carregar();

		if (funcionarioSelecionado == null) {
			// Novo funcionário
			boolean loginJaExiste = lista.stream().anyMatch(f -> f.getLogin().equalsIgnoreCase(login));
			if (loginJaExiste) {
				JOptionPane.showMessageDialog(this, "Login já cadastrado.");
				return;
			}
			funcionarioSelecionado = new Funcionario(nome, login, senha, tipo);
			lista.add(funcionarioSelecionado);
		} else {
			// Edição
			funcionarioSelecionado.setNome(nome);
			funcionarioSelecionado.setLogin(login);
			funcionarioSelecionado.setSenha(senha);
			funcionarioSelecionado.setTipo(tipo);
		}

		FuncionarioDao.salvar(lista);
		JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
		carregarFuncionarios();
	}

	private void excluirFuncionario(ActionEvent e) {
		if (funcionarioSelecionado == null) {
			JOptionPane.showMessageDialog(this, "Selecione um funcionário para excluir.");
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir o funcionário " + funcionarioSelecionado.getNome() + "?",
				"Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			List<Funcionario> lista = FuncionarioDao.carregar();
			lista.remove(funcionarioSelecionado);
			FuncionarioDao.salvar(lista);
			JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
			limparCampos();
			carregarFuncionarios();
		}
	}

	private void limparCampos() {
		funcionarioSelecionado = null;
		campoNome.setText("");
		campoLogin.setText("");
		campoSenha.setText("");
		comboTipo.setSelectedIndex(0);
		comboFuncionarios.setSelectedIndex(0);
	}
}