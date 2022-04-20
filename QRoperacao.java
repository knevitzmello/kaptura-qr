import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

class QRoperacao
{
    private JButton selecionarCaminho = new JButton("Selecione pasta");
    private JButton gerarQR = new JButton("Gerar QRcode");
    private JButton voltar = new JButton("voltar");
    private JFrame frame = new JFrame("QR");
    private String[] campos = {
            "Op","Produto","Referência",
            "Cor","Embalagem","Cliente",
            "Divisor","Quantidade","Maquina",
            "Tempo Ciclo", "Data (usar -)"};
    private int tamanho = campos.length;
    
    private boolean fim = false;
    private JTextField textFieldsCategoria[] = new JTextField[tamanho];
    private JLabel labelsCategoria[] = new JLabel[tamanho];
    private JFileChooser chooser;
    private String caminho = null;
    public QRoperacao()
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

    public void GUI()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        JPanel paineis[] = new JPanel[tamanho+2];
        Container container = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        container.setLayout(layout);
        container.setPreferredSize(new Dimension(300,500));
        for(int i = 0; i < tamanho; i++)
        {
            labelsCategoria[i] = new JLabel(campos[i]);
            labelsCategoria[i].setFont(new Font("Calibri", Font.PLAIN, 18));
            textFieldsCategoria[i] = new JTextField(10);
            container.add(labelsCategoria[i]);
            container.add(textFieldsCategoria[i]);
            layout.putConstraint(SpringLayout.WEST, labelsCategoria[i], 5, SpringLayout.WEST, container);
            //lado esquerdo da label a 5 pixels do lado esquerdo do container
            layout.putConstraint(SpringLayout.NORTH, labelsCategoria[i], 30 * i, SpringLayout.NORTH, container);
            //lado de cima da label a 5 * pos * i pixels do lado de cima do container            
            layout.putConstraint(SpringLayout.NORTH, textFieldsCategoria[i], 30 * i, SpringLayout.NORTH, container);
            //lado de cima do textField a 5 * pos * i do lado de cima do container
            layout.putConstraint(SpringLayout.WEST, textFieldsCategoria[i], 100, SpringLayout.WEST, container);
            //lado esquerdo do textField a 100 pixels do lado esquerdo do container
            layout.putConstraint(SpringLayout.EAST, textFieldsCategoria[i], 15, SpringLayout.EAST, container);

        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date2 = new Date();
        textFieldsCategoria[tamanho-1].setText(sdf.format(date2).toString());

        layout.putConstraint(SpringLayout.WEST, selecionarCaminho, 5, SpringLayout.WEST, container);
        layout.putConstraint(SpringLayout.SOUTH, selecionarCaminho, 1, SpringLayout.SOUTH, container);

        layout.putConstraint(SpringLayout.WEST, gerarQR, 15, SpringLayout.EAST, selecionarCaminho);
        layout.putConstraint(SpringLayout.SOUTH, gerarQR, 1, SpringLayout.SOUTH, container);
        layout.putConstraint(SpringLayout.WEST, voltar, 15, SpringLayout.EAST, gerarQR);
        layout.putConstraint(SpringLayout.SOUTH, voltar, 1, SpringLayout.SOUTH, container);
        container.add(selecionarCaminho);
        container.add(gerarQR);
        //container.add(voltar);
        //WEST == ESQUERDA
        //EAST == DIREITA

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        selecionarCaminho.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int option = fileChooser.showOpenDialog(frame);
                    if(option == JFileChooser.APPROVE_OPTION)
                    {
                        File file = fileChooser.getSelectedFile();
                        caminho = file.getAbsolutePath();
                        selecionarCaminho.setText("Pasta: " + file.getName());
                    }else
                    {
                        selecionarCaminho.setText("Selecione pasta");
                    }
                }
            });
        voltar.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {

                }
            });
        gerarQR.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    String campos[] = new String[tamanho];
                    String texto = "";
                    String erro = "Verifique campos";
                    StringBuilder builder = new StringBuilder();
                    builder.append("@");
                    int i = 0;
                    for(i = 0; i < tamanho; i++)//percorre todos os textFields
                    {
                        campos[i] = textFieldsCategoria[i].getText().toString();
                        if(campos[6] != null)
                            if(!isNumeric(campos[6]))
                            {
                                erro = erro + ("\nVerifique Divisor");
                                break;
                            }
                        if(campos[7] != null)
                            if(!isNumeric(campos[7]))
                            {
                                erro = erro + ("\nVerifique Quantidade");
                                break;
                            }
                        if(campos[i].contains("/") || campos[i].contains("?") || campos[i].contains("_") || campos[i].contains("\\")|| campos[i].contains("."))
                        {
                            erro = erro + ("\nEvite caracteres especiais (/, ?, _, \\, .)");
                            break;
                        }
                        if(campos[i].length() <= 0 || campos[i] == null)//verifica se nao é vazio
                        {
                            break;
                        }else
                        {
                            builder.append(campos[i]);
                            builder.append("_");
                        }
                    }
                    texto = builder.toString();
                    //texto = texto.substring(0, texto.length()-1);
                    if(i < tamanho)
                    {
                        JOptionPane.showMessageDialog(null, erro);
                    }else
                    {
                        if(caminho == null)//verifica se há um caminho
                        {
                            JOptionPane.showMessageDialog(null, "Selecione pasta de destino");
                        }
                        else
                        {
                            GeradorQR2 geradorQR2 = new GeradorQR2(caminho);//chama o gerador de QRcode
                            int filhas = Math.round(Float.parseFloat(campos[7])/Float.parseFloat(campos[6]));
                            String[] dados = new String[filhas];
                            int filhaUser = 0;
                            for (int filha = 0; filha < filhas; filha++) 
                            {
                                filhaUser = filha + 1;
                                dados[filha] = texto + filhaUser;
                            }
                            geradorQR2.compilaImagens(dados);
                            JOptionPane.showMessageDialog(null, "Concluido\n\n\nverifique arquivo no destino");
                        }
                    }
                }
            });
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