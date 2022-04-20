import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.BinaryBitmap;
import java.util.HashMap;
import java.util.Arrays;
import java.io.FileNotFoundException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.MultiFormatReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class GeradorQR2
{
    private String caminho = null;
    private ArrayList<String> dadosLidos;
    private String aux = null;
    private String[] string = null;
    private int tamanho = 950;//400 funciona, 700 parece ser ideal
    private int alturaExtra = 630;
    private int posXLegenda = 20;//20 funciona
    private int qrPorLinha = 4; //4 
    private int qrPorColuna = 4; //7
    private int espacoLateral = 200;
    private int larguraFolha = tamanho * qrPorLinha + qrPorLinha * espacoLateral;
    private int fonteLegenda = 52;//52
    private int margemSuperior = 15;//15
    
    private int quebraLinha = 28;//18
    private int qrPorPagina = qrPorColuna * qrPorLinha;
    private int alturaFolha = (tamanho + alturaExtra) * qrPorColuna + fonteLegenda;
    public GeradorQR2(String caminho)
    {
        this.caminho = caminho;
    }
    public void imprimeOperador(String operador)
    {
        BufferedImage bi = gerarQR(operador);
        try
        {
            ImageIO.write(bi,"png",new File(caminho + "\\" + "pagina " + operador +".png"));
        }catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public void imprimeOperador(String operador, boolean flag)
    {
        BufferedImage bi = gerarQR(operador);
        try
        {
            ImageIO.write(bi,"png",new File(caminho + "\\" + "Operadores" +".png"));
        }catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public BufferedImage gerarQR(String dado)
    {
        String filePath = caminho + "\\" + dado + ".png";
        String crunchifyFileType = "png";
        File crunchifyFile = new File(filePath);
        try {
            Map <EncodeHintType, Object> crunchifyHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            crunchifyHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            crunchifyHintType.put(EncodeHintType.MARGIN, 1);
            Object put = crunchifyHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            QRCodeWriter mYQRCodeWriter = new QRCodeWriter();
            BitMatrix crunchifyBitMatrix = mYQRCodeWriter.encode(dado, BarcodeFormat.QR_CODE, tamanho, tamanho, crunchifyHintType);
            int CrunchifyWidth = crunchifyBitMatrix.getWidth();
            int CrunchifyHeight = crunchifyBitMatrix.getHeight();
            BufferedImage crunchifyImage = new BufferedImage(tamanho, tamanho + alturaExtra, BufferedImage.TYPE_INT_RGB);
            crunchifyImage.createGraphics();
            Graphics2D crunchifyGraphics = (Graphics2D) crunchifyImage.getGraphics();
            crunchifyGraphics.setColor(Color.white);
            crunchifyGraphics.fillRect(0, 0, tamanho, tamanho + alturaExtra);
            crunchifyGraphics.setColor(Color.BLACK);
            for (int i = 0; i < CrunchifyWidth; i++) 
            {
                for (int j = 0; j < CrunchifyWidth; j++) 
                {
                    if (crunchifyBitMatrix.get(i, j)) 
                    {
                        crunchifyGraphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            crunchifyGraphics.setColor(Color.BLACK);
            crunchifyGraphics.setFont(new Font("Arial", Font.PLAIN, fonteLegenda));
            aux = dado.substring(1, dado.length());
            string = aux.split("_");
            String conteudo = "";
            for(int i = 0; i < string.length; i++)
            {
                switch(i)
                {
                    case 0:
                    conteudo = "OP: " + string[i];
                    break;
                    case 1:
                    conteudo = "PROD: " + string[i];
                    break;
                    case 2:
                    conteudo = "REF: " + string[i];
                    break;
                    case 3:
                    conteudo = "COR: " + string[i];
                    break;
                    case 4:
                    conteudo = "EMB: " + string[i];
                    break;
                    case 5:
                    conteudo = "CLI: " + string[i];
                    break;
                    case 6:
                    conteudo = "SAC: " + string[i];
                    break;
                    case 7:
                    conteudo = "QTD: " + string[i];
                    break;
                    case 8:
                    conteudo = "MQ: " + string[i];
                    break;
                    case 9:
                    conteudo = "TC: " + string[i];
                    break;
                    case 10:
                    conteudo = "DT: " + string[i];
                    break;
                    case 11:
                    conteudo = "FL: " + string[i];
                    break;
                }
                crunchifyGraphics.drawString(conteudo, posXLegenda, 35 + tamanho + fonteLegenda * i);
            }
            try
            {
                ImageIO.write(crunchifyImage,"png",new File(caminho + "\\" + dado.replace("_", " ") +".png"));
            }
            catch (java.io.IOException ioex)
            {
                ioex.printStackTrace();
            }
            return crunchifyImage;
        } catch (WriterException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    public void compilaImagens(String[] dados)
    {
        dadosLidos = new ArrayList<String>(Arrays.asList(dados));
        List<String> imagens = new ArrayList();
        int numeroDePaginas = (dadosLidos.size() + qrPorPagina - 1) / qrPorPagina;
        int j = 0;
        for(j = 0; j < numeroDePaginas-1; j++)
        {
            imagens = dadosLidos.subList(qrPorPagina * j, qrPorPagina * (j + 1));
            geraImagemCompilada(j, imagens);
        }
        imagens = dadosLidos.subList(qrPorPagina * j, dadosLidos.size());
        geraImagemCompilada(j, imagens);
    }
    public boolean isEvenlyDivisable(int a, int b) 
    {
        return a % b == 0;
    }
    public void geraImagemCompilada(int pagina, List<String> imagens)
    {
        BufferedImage result = new BufferedImage(larguraFolha, alturaFolha, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        g.drawRect(0, 0, larguraFolha, alturaFolha);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, larguraFolha, alturaFolha);
        int x1 = 0; int y1 = 10;
        BufferedImage bi;
        for(String arquivo : imagens)
        {
            bi = gerarQR(arquivo);
            g.drawImage(bi, x1, y1, null);
            x1 += tamanho + espacoLateral;
            if(x1 >= larguraFolha)
            {
                x1 = 0;
                y1 += tamanho + alturaExtra;
            }
        }
        pagina = pagina + 1;
        try
        {
            ImageIO.write(result,"png",new File(caminho + "\\" + "pagina " + pagina +".png"));
        }catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    
    
}
