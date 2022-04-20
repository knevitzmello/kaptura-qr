import javax.swing.*;
public class Main
{
    private boolean finaliza = false;
    public static void main(String[] args)
    {
        Object[] options = {"OPERAÇÃO", "OPERADORES"};
        Compara compara = new Compara();
        boolean autoriza = false;
        try
        {
            autoriza = compara.licensa();
        }
        catch (java.text.ParseException pe)
        {
            pe.printStackTrace();
        }
        
        if(autoriza)
        {
                switch(JOptionPane.showOptionDialog(null, "Selecione para gerar QRcode", "Gerador de QRCode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]))
            {
                case 0:
                {
                    QRoperacao operacao = new QRoperacao();
                    operacao.GUI();
                    break;
                }
                case 1:
                {
                    QRatualizaOperadores operadores = new QRatualizaOperadores();
                    operadores.GUI();
                    //QRoperador operador = new QRoperador();
                    //operador.GUI();
                    break;
                }
                case -1:
                {
                    break;            
                }
            }
        }else
        {
            JOptionPane.showMessageDialog(null, "Sua licença expirou! Contate desenvolvedor em arthurknev@gmail.com");
        }
    }
}
