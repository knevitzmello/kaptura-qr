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

public class GeradorQR 
{
    private String caminho = null;
    private ArrayList<String> dadosLidos;
    private String aux = null;
    private String[] string = null;
    private int tamanho = 950;//400 funciona, 700 parece ser ideal
    private int tamanhoBloco = 400;
    private int alturaExtra = 600;//150 funciona
    private int posXLegenda = 10;//20 funciona
    private int qrPorLinha = 3; //4 
    private int qrPorColuna = 5; //7
    private int larguraExtra = 600;
    private int larguraFolha = (tamanho + larguraExtra) * qrPorColuna ;
    private int fonteLegenda = 52;//52
    private int margemLateral = 50;//25
    private int margemSuperior = 15;//15
    private int quebraLinha = 28;//18
    private int qrPorPagina = qrPorColuna * qrPorLinha;
    private int alturaFolha = (tamanho + alturaExtra) * qrPorLinha + fonteLegenda;
    public GeradorQR(String caminho)
    {
        this.caminho = caminho;
    }
    public void gerarQR(String[] dadosRecebidos, String nome) throws java.io.IOException
    {
        long startTime = System.nanoTime();
        int numeroDePaginas = (dadosRecebidos.length + qrPorPagina - 1) / qrPorPagina;
        int n = 0;
        String filePath = null;
        for(n = 0; n < numeroDePaginas - 1; n++)
        {
            String[] dados = Arrays.copyOfRange(dadosRecebidos, qrPorPagina * n, qrPorPagina * (n+1));
            filePath = caminho + "\\" + nome + " " + n + ".png";
            imprimeQR(dados, filePath, n);
        }
        String[] dados = Arrays.copyOfRange(dadosRecebidos, qrPorPagina * n, dadosRecebidos.length);
        filePath = caminho + "\\" + nome + " " + n + ".png";
        imprimeQR(dados, filePath, n);
        
        long elapsedTime = System.nanoTime() - startTime;
        //System.out.println("Total execution time to create 1000K objects in Java in millis: " + elapsedTime/1000000);
    }
    public void imprimeQR(String dados[], String nome, int pagina) throws java.io.IOException
    {
        Compilador compilador = new Compilador();
        String filePath = nome;
        int size = tamanho;
        String crunchifyFileType = "png";
        File crunchifyFile = new File(filePath);
        BufferedImage crunchifyImage = new BufferedImage(larguraFolha, alturaFolha, BufferedImage.TYPE_INT_RGB);
        crunchifyImage.createGraphics();
        Graphics2D crunchifyGraphics = (Graphics2D) crunchifyImage.getGraphics();
        crunchifyGraphics.setColor(Color.white);
        //crunchifyGraphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth + alturaExtra);
        crunchifyGraphics.fillRect(0, 0, larguraFolha, alturaFolha);
        crunchifyGraphics.setColor(Color.BLACK);
        Map<EncodeHintType, Object> crunchifyHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        crunchifyHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        crunchifyHintType.put(EncodeHintType.MARGIN, 1);
        Object put = crunchifyHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        QRCodeWriter mYQRCodeWriter = new QRCodeWriter();
        int CrunchifyWidth = 0;
        int CrunchifyHeight = 0;
        BitMatrix crunchifyBitMatrix = null;
        int dado = 0;
        String[] legenda;
        outerloop:
        for(int N_lin = 0; N_lin < qrPorLinha; N_lin++)// itera pelas linhas da folha final
        {
            for(int N_col = 0; N_col < qrPorColuna; N_col++)// itera pelas colunas da folha final
            {
                try
                {
                    crunchifyBitMatrix = mYQRCodeWriter.encode(dados[dado], BarcodeFormat.QR_CODE, size, size, crunchifyHintType);
                    CrunchifyWidth = crunchifyBitMatrix.getWidth();
                    CrunchifyHeight = crunchifyBitMatrix.getHeight();
                }
                catch (com.google.zxing.WriterException we)
                {
                    we.printStackTrace();
                }
                for (int i = 0; i < CrunchifyWidth; i++)//coordenadas relativas
                {
                    for (int j = 0; j < CrunchifyHeight; j++)//coordenadas relativas
                    {
                        if (crunchifyBitMatrix.get(i, j)) 
                        {
                            crunchifyGraphics.fillRect(i + (CrunchifyWidth + larguraExtra) * N_col, j + (CrunchifyHeight + alturaExtra) * N_lin, 1, 1);
                        }
                    }
                }
                legenda = dados[dado].substring(1).split("_");
                crunchifyGraphics.setFont(new Font("Arial", Font.PLAIN, fonteLegenda));
                int deslocamento = 0;
                for(int k = 0; k < legenda.length; k++)
                {
                    if(legenda[k].length() > quebraLinha)
                    {
                        deslocamento = fonteLegenda;
                        crunchifyGraphics.drawString(legenda[k].substring(0, quebraLinha), margemLateral + (CrunchifyWidth + larguraExtra) * N_col, margemSuperior + N_lin * (CrunchifyHeight + alturaExtra) + CrunchifyHeight + fonteLegenda*k);  
                        crunchifyGraphics.drawString(legenda[k].substring(quebraLinha, legenda[k].length()), margemLateral + (CrunchifyWidth + larguraExtra) * N_col, margemSuperior + N_lin * (CrunchifyHeight + alturaExtra) + CrunchifyHeight + fonteLegenda*k + deslocamento);  
                    }else
                    {
                        crunchifyGraphics.drawString(legenda[k], margemLateral + (CrunchifyWidth + larguraExtra) * N_col,margemSuperior + N_lin * (CrunchifyHeight + alturaExtra) + CrunchifyHeight + fonteLegenda*k + deslocamento);
                    }
                }
                dado++;
                if(dado >= dados.length)
                    break outerloop;
            }
        }
        ImageIO.write(crunchifyImage, crunchifyFileType, crunchifyFile);
        compilador.compila(crunchifyImage, filePath, larguraFolha, alturaFolha, 4);
    }
    public void gerarQR(String dado)
    {
        String filePath = caminho + "\\" + dado + ".png";
        int size = tamanho;
        String crunchifyFileType = "png";
        File crunchifyFile = new File(filePath);
        try {
            Map<EncodeHintType, Object> crunchifyHintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            crunchifyHintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            crunchifyHintType.put(EncodeHintType.MARGIN, 1);
            Object put = crunchifyHintType.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            QRCodeWriter mYQRCodeWriter = new QRCodeWriter();
            BitMatrix crunchifyBitMatrix = mYQRCodeWriter.encode(dado, BarcodeFormat.QR_CODE, size, size, crunchifyHintType);
            int CrunchifyWidth = crunchifyBitMatrix.getWidth();
            int CrunchifyHeight = crunchifyBitMatrix.getHeight();
            BufferedImage crunchifyImage = new BufferedImage(tamanhoBloco, tamanhoBloco, BufferedImage.TYPE_INT_RGB);
            crunchifyImage.createGraphics();
            Graphics2D crunchifyGraphics = (Graphics2D) crunchifyImage.getGraphics();
            crunchifyGraphics.setColor(Color.white);
            //crunchifyGraphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth + alturaExtra);
            crunchifyGraphics.fillRect(0, 0, tamanhoBloco, tamanhoBloco);
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
            //System.out.println(string.length);
            for(int i = 0; i < string.length; i++)
            {
                crunchifyGraphics.drawString(string[i], posXLegenda, 35 + CrunchifyHeight + fonteLegenda * i);
                //System.out.println(string[i]);
            }
            ImageIO.write(crunchifyImage, crunchifyFileType, crunchifyFile);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleta()
    {
        File file;
        for(String arquivo : dadosLidos)
        {
            file = new File(caminho + "\\" + arquivo);
            file.delete();
        }
    }
}
