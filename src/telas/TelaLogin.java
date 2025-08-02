package telas;

import javax.swing.*;
import modelo.*;
import dao.FuncionarioDao;

public class TelaLogin extends JFrame {
	private JTextField campoLogin = new JTextField(15);
	private JPasswordField campoSenha = new JPasswordField(15);
	private JComboBox<TFuncionario> comboTipo = new JComboBox<>(TFuncionario.values());

	public TelaLogin() {
		
		setTitle("Login");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		add(new JLabel("Login:"));
		add(campoLogin);
		add(new JLabel("Senha:"));
		add(campoSenha);
		add(new JLabel("Tipo:"));
		add(comboTipo);

		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(e -> autenticar());
		add(btnEntrar);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void autenticar() {
		String login = campoLogin.getText();
		String senha = new String(campoSenha.getPassword());
		TFuncionario tipo = (TFuncionario) comboTipo.getSelectedItem();

		Funcionario funcionario = FuncionarioDao.autenticar(login, senha, tipo);
		if (funcionario != null) {
			new MenuPrincipal(funcionario);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Login ou senha incorretos!");
		}
	}

	public static void main(String[] args) {
		new TelaLogin();
	}
}