import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class Principal extends JFrame {

    private String arq;

    public static void main(String[] args) {

        Principal ok = new Principal();
        ok.setVisible(true);
    }

    public String getArq() {
        return arq;
    }

    public void setArq(String arq) {
        this.arq = arq;
    }

    //declarando componentes
    JPanel pnlGeral, pnlTopo, pnlMeio, pnlBot;
    JLabel lblCnpj, lblConvenio, lblSufixoChaveNf,lblNumNFInicio, lblNumNfFim,lblNumNfGerado,
            lblDtEmissaoInicio, lblDtEmissaoFim, lblDtEmissaoGerada, lblValorTotalInicio, lblValorTotalFim, lblValorTotalGerado,
            lblDesconto, lblQtdeBoleto, lblXML, lblXMLEscolhido,
            lblResultado, lblData, lblPagamento, lblNumProposta;
    JTextArea txtXML, txtResultado;
    JTextField txtCnpj, txtCnpjConvenio,txtSufixoChaveNf, txtNumNfInicio, txtNumNfFim, txtDtEmissaoInicio, txtDtEmissaoFim,
            txtValorTotalInicio, txtValorTotalFim, txtDesconto, txtQtdeBoleto;
    JButton btnChoose, btnConfigPath, btnSalvarResultado, btnGerarNf, btnGerarLote;
    JScrollPane scrlPnlTxtXml, scrlPnlTxtResultado;
    ImageIcon dolar;
    Color temaText, temaTextBg, temaTxtAreaBorda,temaTxtAreaBg,  temaFaixaInferior, temaFaixaSuperior, temaFaixaMeio;
    public Principal() {
        tema();

        setTitle("Gerador de NFs");//definindo o titulo da janela
        setSize(1120, 700);
        setLocationRelativeTo(null);//centralizando a janela

        //definindo operação padrão para o botão fechar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//sem esta linha a janela não fecha pelo botão fechar

        String pathname = "src/main/resources/dolar.jpg";

        dolar = new ImageIcon(pathname);
        setIconImage(dolar.getImage());

        pnlGeral = new JPanel();
        pnlGeral.setLayout(null);
        add(pnlGeral);

        pnlTopo = new JPanel();
        pnlTopo.setBounds(0, 0, 1120, 35);
        pnlTopo.setBackground(temaFaixaSuperior);

        btnChoose = new JButton("Selecionar XML base");//configurando o botao ok
        pnlTopo.add(btnChoose);//adicionando o botao configurado a janela
        btnChoose.addActionListener(new escolheArquivo());
        pnlTopo.setLayout(new FlowLayout());


        btnConfigPath = new JButton("Configurar diretorio de saida");//configurando o botao ok
        pnlTopo.add(btnConfigPath);//adicionando o botao configurado a janela
        btnConfigPath.addActionListener(new definirPath());
        pnlTopo.setLayout(new FlowLayout());

        pnlMeio = new JPanel();
        pnlMeio.setLayout(null);
        pnlMeio.setBounds(0, 35, 1180, 594);
        pnlMeio.setBackground(temaFaixaMeio);

        lblXML = new JLabel("XML base");
        lblXML.setBounds(15, 5, 350, 15);
        pnlMeio.add(lblXML);

        lblXMLEscolhido = new JLabel();
        lblXMLEscolhido.setBounds(15, 20, 350, 15);
        pnlMeio.add(lblXMLEscolhido);

        txtXML = new JTextArea();
        txtXML.setBorder(BorderFactory.createLineBorder(temaTxtAreaBorda));
        txtXML.setForeground(temaText);
        txtXML.setBackground(temaTxtAreaBg);
        txtXML.setBounds(10, 40, 480, 540);
        pnlMeio.add(txtXML);
        scrlPnlTxtXml = new JScrollPane();

        lblCnpj = new JLabel("CNPJ emissor:");//configurando o label
        lblCnpj.setBounds(510, 8, 90, 25);
        pnlMeio.add(lblCnpj);//adicionando o label configurado a janela

        txtCnpj = new JTextField(14);
        txtCnpj.setText("65072595000136");
        txtCnpj.setBounds(600, 5, 120, 25);
        txtCnpj.addFocusListener(new SelecionarTexto(txtCnpj));
        txtCnpj.addKeyListener(new limitaTexto(14, txtCnpj));
        pnlMeio.add(txtCnpj);//adicionando o campo de texto configurado a janela

        lblConvenio = new JLabel("CNPJ convenio:");//configurando o label
        lblConvenio.setBounds(740, 8, 90, 25);
        pnlMeio.add(lblConvenio);//adicionando o label configurado a janela

        txtCnpjConvenio = new JTextField(14);
        txtCnpjConvenio.setText("22767849000128");
        txtCnpjConvenio.setBounds(850, 5, 120, 25);
        txtCnpjConvenio.addFocusListener(new SelecionarTexto(txtCnpjConvenio));
        txtCnpjConvenio.addKeyListener(new limitaTexto(14, txtCnpjConvenio));
        pnlMeio.add(txtCnpjConvenio);//adicionando o campo de texto configurado a janela

        lblSufixoChaveNf = new JLabel("Sufixo para Chave Nf:");//configurando o label
        lblSufixoChaveNf.setBounds(510, 40, 200, 25);
        pnlMeio.add(lblSufixoChaveNf);//adicionando o label configurado a janela
        String sufixo = "";
        txtSufixoChaveNf = new JTextField(7);
        try {
            sufixo = lerSufixo();
        } catch (IOException e) {
            sufixo = "30583210001";
            throw new RuntimeException(e);
        }
        txtSufixoChaveNf.setText(sufixo);
        txtSufixoChaveNf.setBounds(640, 40, 120, 25);
        String sufixoToolTip = "<html>Valor que será utilizado no fim da ChNFe para criar a nova NF</html>" +
                "<br>Clique em Salvar resultado para armazenar o xml gerado</html>";
        byte[] sufixoToolTipArr = sufixoToolTip.getBytes();
        txtSufixoChaveNf.setToolTipText(new String(sufixoToolTipArr, StandardCharsets.UTF_8));
        txtSufixoChaveNf.addFocusListener(new SelecionarTexto(txtSufixoChaveNf));
        txtSufixoChaveNf.addKeyListener(new limitaTexto(12, txtSufixoChaveNf));
        pnlMeio.add(txtSufixoChaveNf);//adicionando o campo de texto configurado a janela

        lblDtEmissaoInicio = new JLabel("Range de dias para data emissao:");//configurando o label
        lblDtEmissaoInicio.setBounds(510, 70, 210, 25);
        pnlMeio.add(lblDtEmissaoInicio);//adicionando o label configurado a janela

        txtDtEmissaoInicio = new JTextField(7);
        txtDtEmissaoInicio.setText("3");
        txtDtEmissaoInicio.setHorizontalAlignment(SwingConstants.RIGHT);
        txtDtEmissaoInicio.setBounds(745, 70, 30, 25);
        txtDtEmissaoInicio.addFocusListener(new SelecionarTexto(txtDtEmissaoInicio));
        txtDtEmissaoInicio.addKeyListener(new limitaTexto(3, txtDtEmissaoInicio));
        pnlMeio.add(txtDtEmissaoInicio);//adicionando o campo de texto configurado a janela

        lblDtEmissaoFim = new JLabel("a");//configurando o label
        lblDtEmissaoFim.setBounds(790, 70, 30, 25);
        pnlMeio.add(lblDtEmissaoFim);//adicionando o label configurado a janela

        txtDtEmissaoFim = new JTextField(7);
        txtDtEmissaoFim.setText("180");
        txtDtEmissaoFim.setBounds(815, 70, 30, 25);
        txtDtEmissaoFim.addFocusListener(new SelecionarTexto(txtDtEmissaoFim));
        txtDtEmissaoFim.addKeyListener(new limitaTexto(3, txtDtEmissaoFim));
        pnlMeio.add(txtDtEmissaoFim);//adicionando o campo de texto configurado a janela

        lblDtEmissaoGerada = new JLabel();//configurando o label
        lblDtEmissaoGerada.setBounds(950, 70, 120, 25);
        pnlMeio.add(lblDtEmissaoGerada);//adicionando o label configurado a janela

        lblNumNFInicio = new JLabel("Range de Numero da NF > Inicio:");//configurando o label
        lblNumNFInicio.setBounds(510, 100, 190, 25);
        pnlMeio.add(lblNumNFInicio);//adicionando o label configurado a janela

        txtNumNfInicio = new JTextField(7);//configurando o campo de texto pro tamanho 8
        txtNumNfInicio.setText("70000");
        txtNumNfInicio.setHorizontalAlignment(SwingConstants.RIGHT);
        txtNumNfInicio.setBounds(705, 100, 70, 25);
        txtNumNfInicio.addFocusListener(new SelecionarTexto(txtNumNfInicio));
        txtNumNfInicio.addKeyListener(new limitaTexto(7, txtNumNfInicio));
        pnlMeio.add(txtNumNfInicio);//adicionando o campo de texto configurado a janela

        lblNumNfFim = new JLabel("a");//configurando o label
        lblNumNfFim.setBounds(790, 100, 30, 25);
        pnlMeio.add(lblNumNfFim);//adicionando o label configurado a janela

        txtNumNfFim = new JTextField(7);
        txtNumNfFim.setText("77000");
        txtNumNfFim.setBounds(815, 100, 70, 25);
        txtNumNfFim.addFocusListener(new SelecionarTexto(txtNumNfFim));
        txtNumNfFim.addKeyListener(new limitaTexto(7, txtNumNfFim));
        pnlMeio.add(txtNumNfFim);//adicionando o campo de texto configurado a janela

        lblNumNfGerado = new JLabel();//configurando o label
        lblNumNfGerado.setBounds(950, 100, 120, 25);
        pnlMeio.add(lblNumNfGerado);//adicionando o label configurado a janela

        lblValorTotalInicio = new JLabel("Range de Valor Total:");//configurando o label
        lblValorTotalInicio.setBounds(510, 130, 140, 25);
        pnlMeio.add(lblValorTotalInicio);//adicionando o label configurado a janela

        txtValorTotalInicio = new JTextField(7);
        txtValorTotalInicio.setText("200,53");
        txtValorTotalInicio.setHorizontalAlignment(SwingConstants.RIGHT);
        txtValorTotalInicio.setBounds(655, 130, 120, 25);
        txtValorTotalInicio.addFocusListener(new SelecionarTexto(txtValorTotalInicio));
        txtValorTotalInicio.addKeyListener(new limitaTexto(14, txtValorTotalInicio));
        pnlMeio.add(txtValorTotalInicio);//adicionando o campo de texto configurado a janela

        lblValorTotalFim = new JLabel("a");//configurando o label
        lblValorTotalFim.setBounds(790, 130, 30, 25);
        pnlMeio.add(lblValorTotalFim);//adicionando o label configurado a janela

        txtValorTotalFim = new JTextField(7);
        txtValorTotalFim.setText("15800,10");
        txtValorTotalFim.setBounds(815, 130, 120, 25);
        txtValorTotalFim.addFocusListener(new SelecionarTexto(txtValorTotalFim));
        txtValorTotalFim.addKeyListener(new limitaTexto(14, txtValorTotalFim));
        pnlMeio.add(txtValorTotalFim);//adicionando o campo de texto configurado a janela

        lblValorTotalGerado = new JLabel();//configurando o label
        lblValorTotalGerado.setBounds(950, 130, 120, 25);
        pnlMeio.add(lblValorTotalGerado);//adicionando o label configurado a janela

        lblQtdeBoleto = new JLabel("Qtde de Boleto(s):");//configurando o label
        lblQtdeBoleto.setBounds(510, 160, 110, 25);
        pnlMeio.add(lblQtdeBoleto);//adicionando o label configurado a janela

        txtQtdeBoleto = new JTextField(7);
        txtQtdeBoleto.setText("1");
        txtQtdeBoleto.setBounds(625, 160, 25, 25);
        txtQtdeBoleto.addFocusListener(new SelecionarTexto(txtQtdeBoleto));
        txtQtdeBoleto.addKeyListener(new limitaTexto(2, txtQtdeBoleto));
        pnlMeio.add(txtQtdeBoleto);//adicionando o campo de texto configurado a janela

        lblDesconto = new JLabel("Desconto:");//configurando o label
        lblDesconto.setBounds(660, 160, 60, 25);
        pnlMeio.add(lblDesconto);//adicionando o label configurado a janela

        txtDesconto = new JTextField(7);
        txtDesconto.setText("0,0");
        txtDesconto.setBounds(725, 160, 120, 25);
        txtDesconto.addFocusListener(new SelecionarTexto(txtDesconto));
        txtDesconto.addKeyListener(new limitaTexto(14, txtDesconto));
        pnlMeio.add(txtDesconto);//adicionando o campo de texto configurado a janela

        lblResultado = new JLabel("NF Gerada (?)");
        String resultadoToolTip = "<html>Selecione um XML base e clique em Gerar NF para ver o resultado aqui" +
                "<br>Clique em Salvar resultado para armazenar o xml gerado</html>";
        byte[] resultadoToolTipArr = resultadoToolTip.getBytes();
        lblResultado.setToolTipText(new String(resultadoToolTipArr, StandardCharsets.UTF_8));
        lblResultado.setBounds(510, 190, 150, 30);
        pnlMeio.add(lblResultado);

        txtResultado = new JTextArea();
        txtResultado.setBounds(510, 220, 580, 360);
        txtResultado.setBorder(BorderFactory.createLineBorder(temaTxtAreaBorda));
        txtResultado.setForeground(temaText);
        txtResultado.setBackground(temaTxtAreaBg);
        txtResultado.setToolTipText(new String(resultadoToolTipArr, StandardCharsets.UTF_8));
        pnlMeio.add(txtResultado);
        scrlPnlTxtResultado = new JScrollPane();
        mudarCorDeFundoDosJFieldTexts(pnlMeio, temaTextBg, temaText);

        pnlBot = new JPanel();
        pnlBot.setLayout(null);
        pnlBot.setBounds(0, 629, 1120, 35);
        pnlBot.setBackground(temaFaixaInferior);

        lblData = new JLabel();
        lblData.setBounds(10, 4, 250, 25);
        pnlBot.add(lblData);

        lblPagamento = new JLabel();
        lblPagamento.setBounds(210, 4, 250, 20);
        pnlBot.add(lblPagamento);

        lblNumProposta = new JLabel();
        lblNumProposta.setBounds(480, 4, 200, 20);
        pnlBot.add(lblNumProposta);

        btnGerarNf = new JButton("Gerar NF");
        btnGerarNf.setBounds(740, 4, 90, 25);
        btnGerarNf.addActionListener(new gerarNf());
        btnGerarNf.setEnabled(false);
        String f5ToolTip = "<html>Preencha o CPF e Numero da proposta, então atualize." +
                "<br>(este botão fica ativo após escolher um arquivo.)</html>";
        byte[] f5ToolTipArr = f5ToolTip.getBytes();
        btnGerarNf.setToolTipText(new String(f5ToolTipArr, StandardCharsets.UTF_8));
        pnlBot.add(btnGerarNf);

        btnSalvarResultado = new JButton("Salvar Resultado");
        btnSalvarResultado.setBounds(840, 4, 140, 25);
        btnSalvarResultado.addActionListener(new Salvar());
        btnSalvarResultado.setEnabled(false);
        pnlBot.add(btnSalvarResultado);

        btnGerarLote = new JButton("Gerar Lote");
        btnGerarLote.setBounds(990, 4, 100, 25);
        btnGerarLote.addActionListener(new gerarLote());
        btnGerarLote.setEnabled(false);
        pnlBot.add(btnGerarLote);

        pnlGeral.add(pnlTopo);
        pnlGeral.add(pnlMeio);
        pnlGeral.add(pnlBot);
        pnlGeral.setVisible(true);

    }
    public void tema(){
        temaText = Color.black;
        temaTextBg = new Color(255, 255, 255);
        temaTxtAreaBorda = new Color(37, 37, 37);
        temaTxtAreaBg = new Color(255, 255, 255);
        temaFaixaInferior = new Color(0, 170, 220);
        temaFaixaMeio = new Color(250, 250, 250);
        temaFaixaSuperior = new Color(110, 195, 150);

    }

    public void definirPath(String caminho) throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        try {
            File arquivoJson = new File("src/main/resources/config.json");


            if (!Files.exists(arquivoJson.toPath())) {
                try {
                    Files.createFile(arquivoJson.toPath());
                    JSONObject jsonSufixo = new JSONObject();
                    jsonSufixo.put("caminhoDeSaida", caminho);
                    Files.write(arquivoJson.toPath(), jsonSufixo.toString().getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    System.out.println("verifica se arquivo existe: " + e.getMessage());
                }
            } else {
                // If the file already exists, just update the value
                JSONObject jsonSufixo = new JSONObject();
                jsonSufixo.put("caminhoDeSaida", caminho);
                Files.write(arquivoJson.toPath(), jsonSufixo.toString().getBytes(StandardCharsets.UTF_8));
            }
        }catch (IOException e) {
            System.out.println("Erro ao ler ou escrever no arquivo JSON: " + e.getMessage());
        }
    }
    public String lerPath() throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        File arquivoJson = new File("src/main/resources/config.json");
        String caminhoDeSaida = "";

        if (Files.exists(arquivoJson.toPath())) {
            String conteudoJson = lerArquivoComoString(String.valueOf(arquivoJson));
            JSONObject objetoJson = new JSONObject(conteudoJson);
            caminhoDeSaida = String.valueOf(objetoJson.getString("caminhoDeSaida"));
        }

        return caminhoDeSaida;
    }
    public void atualizarSufixo() throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        try {
            File arquivoJson = new File("src/main/resources/sufixoChaveNf.json");
            long sufixoChave = Long.parseLong(txtSufixoChaveNf.getText());

            if (!Files.exists(arquivoJson.toPath())) {
                try {
                    Files.createFile(arquivoJson.toPath());
                    JSONObject jsonSufixo = new JSONObject();
                    jsonSufixo.put("sufixoChaveNf", sufixoChave);
                    Files.write(arquivoJson.toPath(), jsonSufixo.toString().getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    System.out.println("verifica se arquivo existe: " + e.getMessage());
                }
            } else {
                // If the file already exists, just update the value
                JSONObject jsonSufixo = new JSONObject();
                jsonSufixo.put("sufixoChaveNf", sufixoChave);
                Files.write(arquivoJson.toPath(), jsonSufixo.toString().getBytes(StandardCharsets.UTF_8));
            }
        }catch (IOException e) {
            System.out.println("Erro ao ler ou escrever no arquivo JSON: " + e.getMessage());
        }
    }
    public String lerSufixo() throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        File arquivoJson = new File("src/main/resources/sufixoChaveNf.json");
        String sufixoChave = "";

        if (Files.exists(arquivoJson.toPath())) {
            String conteudoJson = lerArquivoComoString(String.valueOf(arquivoJson));
            JSONObject objetoJson = new JSONObject(conteudoJson);
            sufixoChave = String.valueOf(objetoJson.getLong("sufixoChaveNf"));
        }

        return sufixoChave;
    }
    public void lerXml(String arquivo) {
        try {
            txtResultado.setText("");

            File file = new File(arquivo);
            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = fabrica.newDocumentBuilder();
            Document doc = db.parse(file);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Define o tamanho da indentação (2 espaços)

            StringWriter stringWriter = new StringWriter();

            transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
            String xmlData = stringWriter.toString().replaceAll("\\s+\\n","\n");

            txtXML.setText(xmlData);
            txtXML.setCaretPosition(0);
            scrlPnlTxtXml.setViewportView(txtXML);
            scrlPnlTxtXml.setBounds(10, 40, 482, 550);
            pnlMeio.add(scrlPnlTxtXml);

            btnGerarNf.setEnabled(true);
            btnSalvarResultado.setEnabled(true);
            btnGerarLote.setEnabled(true);
        } catch (Exception e) {
            System.out.println("ler xml - msg excecao: " + e);
        }
    }
    public void gerarNota(){
        if(Integer.parseInt(txtNumNfInicio.getText()) > Integer.parseInt(txtNumNfFim.getText())  ){
            JOptionPane.showMessageDialog(null, "O valor limite para o numero da NF deve ser menor que o inicial", "Erro com o Fim do Range de numero de Nf", JOptionPane.INFORMATION_MESSAGE);

            txtResultado.setText("");
        }
        try {
            txtResultado.setText("");
            File file = new File(getArq());

            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = fabrica.newDocumentBuilder();
            Document doc = db.parse(file);

            // Obter o elemento <CNPJ> dentro do elemento <emit>
            NodeList emitCnpjList = doc.getElementsByTagName("emit");
            Element emitElement = (Element) emitCnpjList.item(0);
            NodeList emitCnpjNodeList = emitElement.getElementsByTagName("CNPJ");
            Element emitCnpjElement = (Element) emitCnpjNodeList.item(0);
            emitCnpjElement.setTextContent(txtCnpj.getText());

            // Obter o elemento <CNPJ> dentro do elemento <dest>
            NodeList destCnpjList = doc.getElementsByTagName("dest");
            Element destElement = (Element) destCnpjList.item(0);
            NodeList destCnpjNodeList = destElement.getElementsByTagName("CNPJ");
            Element destCnpjElement = (Element) destCnpjNodeList.item(0);
            destCnpjElement.setTextContent(txtCnpjConvenio.getText());

            Node chNFe = doc.getElementsByTagName("chNFe").item(0);
            chNFe.setTextContent(getChNFe(txtSufixoChaveNf.getText(), chNFe.getTextContent()));

            Node dhEmi = doc.getElementsByTagName("dhEmi").item(0);
            int dh = getDhEmissao(Integer.parseInt(txtDtEmissaoInicio.getText()),Integer.parseInt(txtDtEmissaoFim.getText()));
            System.out.println("Data emissao ha " + dh + " dias");
            dhEmi.setTextContent(getDataEmissao(dh));
            OffsetDateTime dataConvertida = OffsetDateTime.parse(getDataEmissao(dh));
            DateTimeFormatter mascara = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String dataEmissao = dataConvertida.format(mascara);
            lblDtEmissaoGerada.setText(dataEmissao);

            Node nNF = doc.getElementsByTagName("nNF").item(0);
            String numNF = getNumeroNf(Integer.parseInt(txtNumNfInicio.getText()),Integer.parseInt(txtNumNfFim.getText()));
            nNF.setTextContent(numNF);
            lblNumNfGerado.setText(numNF);

            Node vOrig = doc.getElementsByTagName("vOrig").item(0);
            String vlrOrigem = getValorTotal(Double.parseDouble(txtValorTotalInicio.getText().replace(",", ".")),Double.parseDouble(txtValorTotalFim.getText().replace(",", ".")));
            System.out.println(vlrOrigem);
            vOrig.setTextContent(vlrOrigem);
            lblValorTotalGerado.setText(vlrOrigem);

            NodeList fatList = doc.getElementsByTagName("fat");
            Element fatElement = (Element) fatList.item(0);
            NodeList vDescNodeList = fatElement.getElementsByTagName("vDesc");
            Element vDescElement = (Element) vDescNodeList.item(0);
            double desconto = Double.parseDouble(txtDesconto.getText().replace(",", "."));
            vDescElement.setTextContent(Double.toString(desconto));

            Node vLiq = doc.getElementsByTagName("vLiq").item(0);
            String vLiquido = getValorLiquido(Double.parseDouble(vlrOrigem),Double.parseDouble(txtDesconto.getText().replace(',', '.')));
            vLiq.setTextContent(vLiquido);

            int qtdeBoletos = Integer.parseInt(txtQtdeBoleto.getText());

            Element cobrElement = (Element) doc.getElementsByTagName("cobr").item(0);
            Element dupElement = (Element) doc.getElementsByTagName("dup").item(0);
            BigDecimal valorBoleto = new BigDecimal(Double.parseDouble(vLiquido) / qtdeBoletos).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal somaBoletos = BigDecimal.valueOf(0.0);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(getDataEmissao(dh), formatter);

            for(int i = 0; i < qtdeBoletos;i++){
                Element dupCopiaElement = (Element) dupElement.cloneNode(true);
                String vencimento = offsetDateTime.plusMonths(i+1).format(DateTimeFormatter.ofPattern("yyyy-MM"))+"-30";
                dupCopiaElement.getElementsByTagName("nDup").item(0).setTextContent("00" + (i +1));
                dupCopiaElement.getElementsByTagName("dVenc").item(0).setTextContent(vencimento);

                if(i == qtdeBoletos -1) {
                    BigDecimal ultimoBoleto = BigDecimal.valueOf(Double.parseDouble(vLiquido)).subtract(somaBoletos);
                    dupCopiaElement.getElementsByTagName("vDup").item(0).setTextContent(String.valueOf(ultimoBoleto));
                }else {
                    dupCopiaElement.getElementsByTagName("vDup").item(0).setTextContent(String.valueOf(valorBoleto));
                }
                somaBoletos = somaBoletos.add(valorBoleto);
                cobrElement.appendChild(dupCopiaElement);
            }

            NodeList dupList = doc.getElementsByTagName("dup");
            int numeroDups = dupList.getLength();

            for (int i = 0; i < numeroDups - qtdeBoletos; i++) {
                Node dup = dupList.item(0);
                dup.getParentNode().removeChild(dup);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Define o tamanho da indentação (2 espaços)

            StringWriter stringWriter = new StringWriter();

            transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
            String xmlData = stringWriter.toString().replaceAll("\\s+\\n","\n");

            txtResultado.setText(xmlData);
            txtResultado.setCaretPosition(0);
            scrlPnlTxtResultado.setViewportView(txtResultado);
            scrlPnlTxtResultado.setBounds(510, 220, 580, 360);
            pnlMeio.add(scrlPnlTxtResultado);

        } catch (Exception e) {
            System.out.println("gerarNf - msg excecao: " + e);
        }
    }
    public void salvarLote(){

        String arquivoSalvo;

        String path = null;
        try {
            path = lerPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmm");
        arquivoSalvo = "chNFe_" + txtSufixoChaveNf.getText() +
                "_NF" + lblNumNfGerado.getText() +
                "_TESTE-" + dtf.format(LocalDateTime.now()) + ".xml";
        PrintWriter pw;

        try {
            pw = new PrintWriter(new FileWriter(path+"\\"+arquivoSalvo));
            pw.println(txtResultado.getText());
            pw.close();

            long sufixoChave = Long.parseLong(txtSufixoChaveNf.getText());
            sufixoChave++;
            txtSufixoChaveNf.setText(String.valueOf(sufixoChave));
            atualizarSufixo();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class gerarNf implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            gerarNota();
        }
    }
    private static void mudarCorDeFundoDosJFieldTexts(Container container, Color backgroundColor, Color textColor) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setBackground(backgroundColor);
                textField.setForeground(textColor);
                Border lineBorder = BorderFactory.createLineBorder(new Color(100, 100, 100));
                Border paddedBorder = BorderFactory.createEmptyBorder(0, 4, 0, 0);
                textField.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddedBorder));

            } else if (component instanceof Container) {
                mudarCorDeFundoDosJFieldTexts((Container) component, backgroundColor, textColor);
            }
        }
    }
    public String getNumeroNf(int min, int max) {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(max - min + 1) + min);
    }
    public Integer getDhEmissao(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
    public String getValorTotal(double min, double max) {
        Random rand = new Random();
        double randomNumber = rand.nextDouble() * (max - min) + min;
        String numeroFormatado = String.format("%.2f", randomNumber).replace(",", ".");;
        return numeroFormatado;//
    }
    public String getValorLiquido(double valorTotal, double desconto) {
        double randomNumber = valorTotal - desconto;
        String numeroFormatado = String.format("%.2f", randomNumber).replace(",", ".");
        return numeroFormatado;//.replace("\\.", ",");
    }
    public String getChNFe(String sufixo, String chNfe) {
        int posicaoSufixo = chNfe.length() - sufixo.length();
        String padraoUltimosDigitos = "(\\d{"+posicaoSufixo+"})(\\d{" + sufixo.length()+"})";
        return chNfe.replaceFirst(padraoUltimosDigitos, "$1" + sufixo);
    }
    public static String getDataEmissao(int dias) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime emissionDateTime = agora.minusDays(dias);
        Instant instant = emissionDateTime.atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
        Date date = Date.from(instant);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String dataEmissao = df.format(date).replaceAll("(\\-\\d\\d)(\\d\\d)", "$1:$2");
        return dataEmissao;
    }
    public class Salvar implements ActionListener {
        public String arquivoSalvo;

        public void actionPerformed(ActionEvent ev) {
            JFileChooser fcSalvar = new JFileChooser();
            String path;
            try {
                path = lerPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fcSalvar.setCurrentDirectory(new File(path));
            fcSalvar.setDialogTitle("Salve o XML");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmm");
            arquivoSalvo = "chNFe_" + txtSufixoChaveNf.getText() +
                    "_NF" + lblNumNfGerado.getText() +
                    "_TESTE-" + dtf.format(LocalDateTime.now()) + ".xml";

            fcSalvar.setSelectedFile(new File(arquivoSalvo));
            int salvo = fcSalvar.showSaveDialog(Principal.this);

            if (salvo == 0) {
                PrintWriter pw;

                try {
                    pw = new PrintWriter(new FileWriter(fcSalvar.getSelectedFile()));
                    pw.println(txtResultado.getText());
                    pw.close();

                    long sufixoChave = Long.parseLong(txtSufixoChaveNf.getText());
                    sufixoChave++;
                    txtSufixoChaveNf.setText(String.valueOf(sufixoChave));
                    atualizarSufixo();

                    JOptionPane.showMessageDialog(null, "Arquivo salvo");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Arquivo nao foi criado");
                }
            }
        }
    }
    public class gerarLote implements ActionListener {
        public String arquivoSalvo;
        public void actionPerformed(ActionEvent ev) {
            int qtdeNfs = 0;

            String qtdeNfsStr = JOptionPane.showInputDialog("Quantidade de NFs:","5");
            if (qtdeNfsStr != null) {
                try {
                    qtdeNfs = Integer.parseInt(qtdeNfsStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Quantidade de NFs inválida");
                }
            }
            for (int i = 0; i < qtdeNfs; i++) {
                System.out.println(i + " indice");
                gerarNota();
                salvarLote();
            }
        }
    }
    public class escolheArquivo extends Component implements ActionListener {
        public String arquivo;

        public void actionPerformed(ActionEvent ev) {
            JFileChooser fc = new JFileChooser();
            String path = null;
            try {
                path = lerPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fc.setCurrentDirectory(new File(path));
            fc.setDialogTitle("Selecione o XML");

            fc.showOpenDialog(Principal.this);
            if (fc.getSelectedFile() != null) {
                arquivo = String.valueOf(fc.getSelectedFile());
                lblXMLEscolhido.setText(fc.getSelectedFile().getName());
                setArq(arquivo);
                lerXml(arquivo);
            }
        }
    }
    public class definirPath extends Component implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            String pathSugerido = "E:\\qa\\nf";
            String path = JOptionPane.showInputDialog("Digite o caminho: ", pathSugerido);
            try {
                definirPath(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public class SelecionarTexto implements FocusListener {

        private JTextField campoAlvo;

        public SelecionarTexto(JTextField campoAlvo) {
            this.campoAlvo = campoAlvo;
        }

        @Override
        public void focusGained(FocusEvent e) {
            campoAlvo.selectAll();
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }
    public class limitaTexto implements KeyListener {
        private int tamanho;
        private JTextField campoAlvo;

        public limitaTexto(int tamanho, JTextField campoAlvo) {
            this.tamanho = tamanho;
            this.campoAlvo = campoAlvo;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (campoAlvo.getSelectedText() != null && campoAlvo.getSelectedText().length() == tamanho) {
                campoAlvo.setText("");
            } else if (campoAlvo.getText().length() >= tamanho) // limitar a quantidade de caracteres na chamada
                e.consume();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable transferable = clipboard.getContents(null);
                try {
                    String textoColado = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                    if (textoColado.length() > tamanho) {
                        campoAlvo.setText(textoColado.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("/", "").substring(0, tamanho));
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    private static String lerArquivoComoString(String filePath) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}