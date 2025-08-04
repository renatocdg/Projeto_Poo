package telas;

import javax.swing.*;
import modelo.*;
import dao.UsuarioDao;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaUsuario extends JFrame {

	private JTextField campoMatricula, campoNome, campoEmail, campoTelefone;
	private JComboBox<TipoUsuario> comboTipo;
	private JComboBox<Usuario> comboUsuarios;
	private JButton btnSalvar, btnExcluir, btnLimpar;
	private Usuario usuarioSelecionado;

	public TelaUsuario() {
		configurarJanela();
		inicializarComponentes();
		configurarLayout();
		carregarUsuarios();
		adicionarListeners();
		setVisible(true);
	}

	private void configurarJanela() {
		setTitle("Gerenciamento de Usuários");
		setSize(500, 400);
		setLayout(new GridLayout(7, 2, 10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void inicializarComponentes() {
		comboUsuarios = new JComboBox<>();
		campoMatricula = new JTextField();
		campoNome = new JTextField();
		campoEmail = new JTextField();
		campoTelefone = new JTextField();
		comboTipo = new JComboBox<>(TipoUsuario.values());
		btnSalvar = new JButton("Salvar");
		btnExcluir = new JButton("Excluir");
		btnLimpar = new JButton("Limpar");
	}

	private void configurarLayout() {
		add(new JLabel("Usuário:"));
		add(comboUsuarios);
		add(new JLabel("Matrícula:"));
		add(campoMatricula);
		add(new JLabel("Nome:"));
		add(campoNome);
		add(new JLabel("Tipo:"));
		add(comboTipo);
		add(new JLabel("Email:"));
		add(campoEmail);
		add(new JLabel("Telefone:"));
		add(campoTelefone);
		add(btnLimpar);
		add(btnSalvar);
		add(new JLabel());
		add(btnExcluir);
	}

	private void adicionarListeners() {
		comboUsuarios.addActionListener(e -> selecionarUsuario());
		btnSalvar.addActionListener(this::salvarUsuario);
		btnExcluir.addActionListener(this::excluirUsuario);
		btnLimpar.addActionListener(e -> limparCampos());
	}

	private void carregarUsuarios() {
		comboUsuarios.removeAllItems();
		UsuarioDao.carregar().forEach(comboUsuarios::addItem);
		comboUsuarios.insertItemAt(null, 0);
		comboUsuarios.setSelectedIndex(0);
	}

	private void selecionarUsuario() {
		usuarioSelecionado = (Usuario) comboUsuarios.getSelectedItem();
		if (usuarioSelecionado != null) {
			campoMatricula.setText(usuarioSelecionado.getMatricula());
			campoNome.setText(usuarioSelecionado.getNome());
			comboTipo.setSelectedItem(usuarioSelecionado.getTipo());
			campoEmail.setText(usuarioSelecionado.getEmail());
			campoTelefone.setText(usuarioSelecionado.getTelefone());
		}
	}

	private void salvarUsuario(ActionEvent e) {
		String matricula = campoMatricula.getText().trim();
		String nome = campoNome.getText().trim();
		TipoUsuario tipo = (TipoUsuario) comboTipo.getSelectedItem();
		String email = campoEmail.getText().trim();
		String telefone = campoTelefone.getText().trim();

		if (matricula.isEmpty() || nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
			return;
		}

		if (!email.contains("@")) {
			JOptionPane.showMessageDialog(this, "Email inválido.");
			return;
		}

		List<Usuario> usuarios = UsuarioDao.carregar();

		if (usuarioSelecionado == null) {
			// Novo usuário
			if (UsuarioDao.buscarPorMatricula(matricula) != null) {
				JOptionPane.showMessageDialog(this, "Matrícula já cadastrada.");
				return;
			}
			usuarios.add(new Usuario(matricula, nome, tipo, email, telefone));
		} else {
			
			// Edição
			usuarioSelecionado.setMatricula(matricula);
			usuarioSelecionado.setNome(nome);
			usuarioSelecionado.setTipo(tipo);
			usuarioSelecionado.setEmail(email);
			usuarioSelecionado.setTelefone(telefone);
		}

		UsuarioDao.salvar(usuarios);
		JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
		carregarUsuarios();
		limparCampos();
	}

	private void excluirUsuario(ActionEvent e) {
		if (usuarioSelecionado == null) {
			JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir " + usuarioSelecionado.getNome() + "?", "Confirmar Exclusão",
				JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			List<Usuario> usuarios = UsuarioDao.carregar();
			usuarios.removeIf(u -> u.getMatricula().equals(usuarioSelecionado.getMatricula()));
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
		campoEmail.setText("");
		campoTelefone.setText("");
		comboUsuarios.setSelectedIndex(0);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new TelaUsuario());
	}
}