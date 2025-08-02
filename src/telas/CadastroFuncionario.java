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
	private JButton botaoCadastrar;

	public CadastroFuncionario() {
		setTitle("Cadastro de Funcionário");
		setSize(350, 250);
		setLayout(new GridLayout(5, 2, 10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		campoNome = new JTextField();
		campoLogin = new JTextField();
		campoSenha = new JPasswordField();
		comboTipo = new JComboBox<>(TFuncionario.values());
		botaoCadastrar = new JButton("Cadastrar");

		add(new JLabel("Nome:"));
		add(campoNome);
		add(new JLabel("Login:"));
		add(campoLogin);
		add(new JLabel("Senha:"));
		add(campoSenha);
		add(new JLabel("Tipo:"));
		add(comboTipo);
		add(new JLabel());
		add(botaoCadastrar);

		botaoCadastrar.addActionListener(this::cadastrarFuncionario);

		setVisible(true);
	}

	private void cadastrarFuncionario(ActionEvent e) {
		String nome = campoNome.getText().trim();
		String login = campoLogin.getText().trim();
		String senha = new String(campoSenha.getPassword()).trim();
		TFuncionario tipo = (TFuncionario) comboTipo.getSelectedItem();

		if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
			return;
		}

		List<Funcionario> lista = FuncionarioDao.carregar();

		boolean loginJaExiste = lista.stream().anyMatch(f -> f.getLogin().equalsIgnoreCase(login));

		if (loginJaExiste) {
			JOptionPane.showMessageDialog(this, "Login já cadastrado.");
			return;
		}

		Funcionario novo = new Funcionario(nome, login, senha, tipo);
		lista.add(novo);
		FuncionarioDao.salvar(lista);

		JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
		limparCampos();
	}

	private void limparCampos() {
		campoNome.setText("");
		campoLogin.setText("");
		campoSenha.setText("");
		comboTipo.setSelectedIndex(0);
	}
}