package telas;

import javax.swing.*;
import java.awt.*;
import modelo.*;
import dao.*;

public class TelaLogin extends JFrame {
	
    private JTextField campoLogin = new JTextField(15);
    private JPasswordField campoSenha = new JPasswordField(15);
    private JComboBox<TipoFuncionario> comboTipo = new JComboBox<>(TipoFuncionario.values());

    public TelaLogin() {
        setTitle("Login - Sistema Biblioteca");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel principal com borda e padding
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        
        // Título
        JLabel titulo = new JLabel("Acesso ao Sistema");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelPrincipal.add(titulo);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campos do formulário
        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new GridLayout(0, 1, 5, 5));
        
        adicionarCampo(painelCampos, "Login:", campoLogin);
        adicionarCampo(painelCampos, "Senha:", campoSenha);
        
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.add(new JLabel("Tipo:"));
        painelTipo.add(comboTipo);
        painelCampos.add(painelTipo);
        
        painelPrincipal.add(painelCampos);
        painelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setPreferredSize(new Dimension(100, 30));
        btnEntrar.addActionListener(e -> autenticar());
        
        painelPrincipal.add(btnEntrar);
        
        add(painelPrincipal);
        setVisible(true);
    }

    private void adicionarCampo(JPanel painel, String label, JComponent campo) {
        JPanel painelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelCampo.add(new JLabel(label));
        campo.setPreferredSize(new Dimension(200, 25));
        painelCampo.add(campo);
        painel.add(painelCampo);
    }

    private void autenticar() {
        String login = campoLogin.getText();
        String senha = new String(campoSenha.getPassword());
        TipoFuncionario tipo = (TipoFuncionario) comboTipo.getSelectedItem();

        Funcionario funcionario = FuncionarioDao.autenticar(login, senha, tipo);
        if (funcionario != null) {
            new MenuPrincipal(funcionario);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login ou senha incorretos!", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TelaLogin();
        });
    }
}