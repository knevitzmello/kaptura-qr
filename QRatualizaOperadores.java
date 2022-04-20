import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.BufferedReader;
import javax.swing.filechooser.*;
import java.io.FileReader;
import java.util.Vector;

public class QRatualizaOperadores
{
    private JButton buttonSelecionarArquivo = new JButton("Selecione arquivo");
    private JButton buttonSelecionarDestino = new JButton("Selecionar destino");
    private JButton buttonGerarQR = new JButton("Gerar QRcode");
    private JComboBox comboOperadores = new JComboBox();
    //private String[] operadores;

    private JFrame frame = new JFrame("QR");
    private String[] campos = {"Operador"};
    private int tamanho = campos.length;
    private boolean fim = false;
    private JTextField textFieldsCategoria[] = new JTextField[tamanho];
    private JLabel labelsCategoria[] = new JLabel[tamanho];
    private JFileChooser chooser;
    private String caminho = null;
    private String caminhoOPs = null;
    private String caminhoBiblioteca = null;
    private String caminhoDestino = null;
    private boolean bibliotecaSelecionada = false;
    private boolean arquivoSelecionado = false;
    private boolean selecaoValida = false;
    private boolean destinoSelecionado = false;
    private Vector<String> operadores = new Vector<String>();

    public QRatualizaOperadores()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void addListeners()
    {
        buttonSelecionarArquivo.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt", "text");
                    fileChooser.setFileFilter(filter);
                    int option = fileChooser.showOpenDialog(frame);
                    if(option == JFileChooser.APPROVE_OPTION)
                    {
                        File file = fileChooser.getSelectedFile();
                        caminhoBiblioteca = file.getAbsolutePath();
                        System.out.println("teste");
                        try 
                        {
                            operadores.clear();
                            DefaultComboBoxModel dml = new DefaultComboBoxModel();
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = br.readLine()) != null) 
                            {
                                System.out.println("teste " + line);
                                operadores.addElement(line);
                                dml.addElement(line);
                            }
                            if(!operadores.isEmpty())
                            {
                                System.out.println("teste 2");
                                comboOperadores.setModel(dml);
                                bibliotecaSelecionada = true;
                            } else
                                bibliotecaSelecionada = false;
                            
                        }catch (Exception ex) 
                        {
                            bibliotecaSelecionada = false;
                            ex.printStackTrace();
                        }
                    }else
                    {
                        bibliotecaSelecionada = false;
                        buttonSelecionarArquivo.setText("Selecionar arquivo");
                    }
                }
            });
        buttonSelecionarDestino.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int option = fileChooser.showOpenDialog(frame);
                    if(option == JFileChooser.APPROVE_OPTION)
                    {
                        File file = fileChooser.getSelectedFile();
                        caminhoDestino = file.getAbsolutePath();
                        buttonSelecionarDestino.setText("Pasta: " + file.getName());
                        destinoSelecionado = true;
                    }else
                    {
                        buttonSelecionarDestino.setText("Selecione pasta");
                        destinoSelecionado = false;
                    }
                }
            });
        buttonGerarQR.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    String campos[] = new String[tamanho];
                    String texto = "";
                    String erro = "Verifique campos";
                    StringBuilder builder = new StringBuilder();
                    builder.append("%");
                    for(String operador: operadores)
                    {
                        builder.append(operador);
                        builder.append("_");
                    }
                    texto = builder.toString();
                    texto = texto.substring(0, texto.length()-1);
                    
                    if(destinoSelecionado == false)//verifica se há um caminho
                    {
                        JOptionPane.showMessageDialog(null, "Verifique caminho selecionado");
                    }
                    else
                    {
                        GeradorQR2 geradorQR2 = new GeradorQR2(caminhoDestino);//chama o gerador de QRcode
                        geradorQR2.imprimeOperador(texto, true);
                        JOptionPane.showMessageDialog(null, "Concluido\n\nverifique arquivo no destino");
                    }
                    
                }
            });
    }
    public void GUI()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        JPanel paineis[] = new JPanel[tamanho+2];
        Container container = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        container.setLayout(layout);
        container.setPreferredSize(new Dimension(450,75));
        
        
        buttonSelecionarArquivo.setPreferredSize(new Dimension(200, 25));
        layout.putConstraint(SpringLayout.WEST, buttonSelecionarArquivo, 5, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, buttonSelecionarArquivo, 5, SpringLayout.NORTH, container);

        comboOperadores.setPreferredSize(new Dimension(200, 25));
        layout.putConstraint(SpringLayout.WEST, comboOperadores, 30, SpringLayout.EAST, buttonSelecionarArquivo);
        layout.putConstraint(SpringLayout.NORTH, comboOperadores, 5, SpringLayout.NORTH, container);

        buttonSelecionarDestino.setPreferredSize(new Dimension(200, 25));
        layout.putConstraint(SpringLayout.WEST, buttonSelecionarDestino, 5, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.NORTH, buttonSelecionarDestino, 5, SpringLayout.SOUTH, buttonSelecionarArquivo);

        buttonGerarQR.setPreferredSize(new Dimension(200, 25));
        layout.putConstraint(SpringLayout.WEST, buttonGerarQR, 30, SpringLayout.EAST, buttonSelecionarDestino);
        layout.putConstraint(SpringLayout.NORTH, buttonGerarQR, 5, SpringLayout.SOUTH, comboOperadores);

        container.add(buttonSelecionarArquivo);
        container.add(comboOperadores);
        container.add(buttonGerarQR);
        container.add(buttonSelecionarDestino);
        //container.add(voltar);
        //WEST == ESQUERDA
        //EAST == DIREITA

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        this.addListeners();
        
    }

    public static boolean isNumeric(String strNum) 
    {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
