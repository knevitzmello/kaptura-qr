import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
public class Compilador
{
    private int x = 0;
    private int y = 0;
    //private String caminho = null;
    public Compilador()
    {
        
    }
    public void setCaminho(String caminho)
    {
        //this.caminho = caminho;
    }
    public void compila(BufferedImage bi, String caminho, int largura, int altura, int quantidade)
    {
        BufferedImage result = new BufferedImage(largura * quantidade/2, altura * quantidade/2, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        for(int i = 0; i < quantidade; i++)
        {
            g.drawImage(bi, x, y, null);
            x += bi.getWidth();
            if(x >= result.getWidth())
            {
                x = 0;
                y += bi.getHeight();
            }
        }
        try
            {
                ImageIO.write(result,"png",new File(caminho.substring(0, caminho.indexOf(".png")) + " compilado" + ".png"));
            }
            catch (java.io.IOException ioe)
            {
                ioe.printStackTrace();
            }
    }
}