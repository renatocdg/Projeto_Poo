package telas;

import javax.swing.*;
import modelo.*;
import dao.UsuarioDao;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaUsuario extends JFrame {

	private JTextField campoMatricula, campoNome, campoTelefone, campoEmail;
	private JComboBox<TipoUsuario> comboTipo;
	private JComboBox<Usuario> comboUsuarios;
	private JButton btnSalvar, btnExcluir;
	private Usuario usuarioSelecionado;

	public TelaUsuario() {

		setTitle("Gerenciamento de Usuários");
		setSize(400, 400);
		setLayout(new GridLayout(8, 2, 10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		comboUsuarios = new JComboBox<>();
		campoMatricula = new JTextField();
		campoNome = new JTextField();
		comboTipo = new JComboBox<>(TipoUsuario.values());
		campoTelefone = new JTextField();
		campoEmail = new JTextField();
		btnSalvar = new JButton("Salvar");
		btnExcluir = new JButton("Excluir");
		JButton btnNovo = new JButton("Novo");

		// Layout
		add(new JLabel("Usuário:"));
		add(comboUsuarios);
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
		add(btnNovo);
		add(btnSalvar);
		add(new JLabel());
		add(btnExcluir);

		// Carrega usuários
		carregarUsuarios();

		// Listeners
		comboUsuarios.addActionListener(e -> selecionarUsuario());
		btnSalvar.addActionListener(this::salvarUsuario);
		btnExcluir.addActionListener(this::excluirUsuario);
		btnNovo.addActionListener(e -> limparCampos());

		setVisible(true);
	}

	private void carregarUsuarios() {
		comboUsuarios.removeAllItems();
		List<Usuario> usuarios = UsuarioDao.carregar();
		usuarios.forEach(comboUsuarios::addItem);
		comboUsuarios.insertItemAt(null, 0);
		comboUsuarios.setSelectedIndex(0);
	}

	private void selecionarUsuario() {
		usuarioSelecionado = (Usuario) comboUsuarios.getSelectedItem();
		if (usuarioSelecionado != null) {
			campoMatricula.setText(usuarioSelecionado.getMatricula());
			campoNome.setText(usuarioSelecionado.getNome());
			comboTipo.setSelectedItem(usuarioSelecionado.getTipo());
			campoTelefone.setText(usuarioSelecionado.getTelefone());
			campoEmail.setText(usuarioSelecionado.getEmail());
		}
	}

	private void salvarUsuario(ActionEvent e) {
		String matricula = campoMatricula.getText().trim();
		String nome = campoNome.getText().trim();
		TipoUsuario tipo = (TipoUsuario) comboTipo.getSelectedItem();
		String telefone = campoTelefone.getText().trim();
		String email = campoEmail.getText().trim();

		if (matricula.isEmpty() || nome.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Matrícula e nome são obrigatórios.");
			return;
		}

		List<Usuario> usuarios = UsuarioDao.carregar();

		if (usuarioSelecionado == null) {
			// Novo usuário
			boolean matriculaJaExiste = usuarios.stream().anyMatch(u -> u.getMatricula().equalsIgnoreCase(matricula));
			if (matriculaJaExiste) {
				JOptionPane.showMessageDialog(this, "Matrícula já cadastrada.");
				return;
			}
			usuarioSelecionado = new Usuario(matricula, nome, tipo, telefone, email);
			usuarios.add(usuarioSelecionado);
		} else {
			// Edição
			usuarioSelecionado.setMatricula(matricula);
			usuarioSelecionado.setNome(nome);
			usuarioSelecionado.setTipo(tipo);
			usuarioSelecionado.setTelefone(telefone);
			usuarioSelecionado.setEmail(email);
		}

		UsuarioDao.salvar(usuarios);
		JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
		carregarUsuarios();
	}

	private void excluirUsuario(ActionEvent e) {
		if (usuarioSelecionado == null) {
			JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir o usuário " + usuarioSelecionado.getNome() + "?", "Confirmar Exclusão",
				JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			List<Usuario> usuarios = UsuarioDao.carregar();
			usuarios.remove(usuarioSelecionado);
			UsuarioDao.salvar(usuarios);
			JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
			limparCampos();
			carregarUsuarios();
		}
	}

	private void limparCampos() {
		usuarioSelecionado = null;
		campoMatricula.setText("");
		campoNome.setText("");
		comboTipo.setSelectedIndex(0);
		campoTelefone.setText("");
		campoEmail.setText("");
		comboUsuarios.setSelectedIndex(0);
	}
}