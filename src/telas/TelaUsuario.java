package telas;

import javax.swing.*;
import modelo.*;
import dao.UsuarioDao;

public class TelaUsuario extends JFrame {
	private JTextField campoMatricula = new JTextField(15);
	private JTextField campoNome = new JTextField(15);
	private JComboBox<TipoUsuario> comboTipo = new JComboBox<>(TipoUsuario.values());
	private JTextField campoTelefone = new JTextField(15);
	private JTextField campoEmail = new JTextField(15);

	public TelaUsuario() {
		setTitle("Cadastro de Usuário");
		setSize(300, 250);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		add(new JLabel("Matrícula:"));
		add(campoMatricula);
		add(new JLabel("Nome:"));
		add(campoNome);
		add(new JLabel("Tipo:"));
		add(comboTipo);
		add(new JLabel("Telefone:"));
		add(campoTelefone);
		add(new JLabel("Email:"));
		add(campoEmail);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(e -> salvarUsuario());
		add(btnSalvar);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void salvarUsuario() {
		Usuario usuario = new Usuario(campoMatricula.getText(), campoNome.getText(),
				(TipoUsuario) comboTipo.getSelectedItem(), campoTelefone.getText(), campoEmail.getText());

		UsuarioDao.adicionarUsuario(usuario);
		JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
		dispose();
	}
}