import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
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
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    JLabel lblCnpj,lblConvenio, lblNumNFInicio, lblNumNfFim,lblDtEmissao, lblValorTotalInicio,lblValorTotalFim,lblDesconto,
            lblXML, lblXMLEscolhido,
            lblResultado, lblData, lblPagamento, lblNumProposta;
    JTextArea txtXML, txtResultado;
    JTextField txtCnpj,txtCnpjConvenio, txtNumNfInicio,txtNumNfFim,txtDtEmissao,txtValorTotalInicio,txtValorTotalFim,
            txtDesconto;
    JButton btnChoose, btnSalvarResultado, btnAtualizar;
    JScrollPane scrlPnlTxtXml;
    ImageIcon dolar;

    public Principal() {

        setTitle("Gerador de NFs");//definindo o titulo da janela
        setSize(1120, 700);
        setLocationRelativeTo(null);//centralizando a janela

        //definindo operação padrão para o botão fechar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//sem esta linha a janela não fecha pelo botão fechar
        //dolar = new ImageIcon(Toolkit.getDefaultToolkit().getImage("src\\main\\resources\\dolar.jpg"));
        String pathname="src/main/resources/dolar.jpg";
        //File file=new File(pathname);
        //InputStream image = this.getClass().getResourceAsStream("src\\main\\resources\\dolar.jpg");
        //System.out.println(image);
        dolar = new ImageIcon(pathname);
        setIconImage(dolar.getImage());

        pnlGeral = new JPanel();
        pnlGeral.setLayout(null);
        add(pnlGeral);

        pnlTopo = new JPanel();
        pnlTopo.setBounds(0, 0, 1120, 35);
        pnlTopo.setBackground(Color.white);

        btnChoose = new JButton("Selecionar XML base");//configurando o botao ok
        pnlTopo.add(btnChoose);//adicionando o botao configurado a janela
        btnChoose.addActionListener(new Choose());
        pnlTopo.setLayout(new FlowLayout());

        pnlMeio = new JPanel();
        pnlMeio.setLayout(null);
        pnlMeio.setBounds(0, 35, 1180, 594);
        pnlMeio.setBackground(new Color(200, 200, 240));

        lblCnpj = new JLabel("CNPJ emissor:");//configurando o label
        lblCnpj.setBounds(510, 8, 90, 25);
        pnlMeio.add(lblCnpj);//adicionando o label configurado a janela

        txtCnpj = new JTextField(14);
        txtCnpj.setText("65072595000136");
        txtCnpj.setBounds(600,5,120,25);
        txtCnpj.addFocusListener(new SelecionarTexto(txtCnpj));
        txtCnpj.addKeyListener(new limitaTexto(14, txtCnpj));
        pnlMeio.add(txtCnpj);//adicionando o campo de texto configurado a janela

        lblConvenio = new JLabel("CNPJ convenio:");//configurando o label
        lblConvenio.setBounds(740, 8, 90, 25);
        pnlMeio.add(lblConvenio);//adicionando o label configurado a janela

        txtCnpjConvenio = new JTextField(14);
        txtCnpjConvenio.setText("22767849000128");
        txtCnpjConvenio.setBounds(850,5,120,25);
        txtCnpjConvenio.addFocusListener(new SelecionarTexto(txtCnpjConvenio));
        txtCnpjConvenio.addKeyListener(new limitaTexto(14, txtCnpjConvenio));
        pnlMeio.add(txtCnpjConvenio);//adicionando o campo de texto configurado a janela

        lblNumNFInicio = new JLabel("Range de Numero da NF > Inicio:");//configurando o label
        lblNumNFInicio.setBounds(510, 40, 200, 25);
        pnlMeio.add(lblNumNFInicio);//adicionando o label configurado a janela

        txtNumNfInicio = new JTextField(7);//configurando o campo de texto pro tamanho 8
        txtNumNfInicio.setText("70000");
        txtNumNfInicio.setBounds(700, 40, 70, 25);
        txtNumNfInicio.addFocusListener(new SelecionarTexto(txtNumNfInicio));
        txtNumNfInicio.addKeyListener(new limitaTexto(7,txtNumNfInicio));
        pnlMeio.add(txtNumNfInicio);//adicionando o campo de texto configurado a janela

        lblNumNfFim = new JLabel("Fim:");//configurando o label
        lblNumNfFim.setBounds(775, 40, 50, 25);
        pnlMeio.add(lblNumNfFim);//adicionando o label configurado a janela

        txtNumNfFim = new JTextField(7);
        txtNumNfFim.setText("77000");
        txtNumNfFim.setBounds(810, 40, 70, 25);
        txtNumNfFim.addFocusListener(new SelecionarTexto(txtNumNfFim));
        txtNumNfFim.addKeyListener(new limitaTexto(7,txtNumNfFim));
        pnlMeio.add(txtNumNfFim);//adicionando o campo de texto configurado a janela

        lblDtEmissao = new JLabel("Dias para data emissao:");//configurando o label
        lblDtEmissao.setBounds(510, 70, 140, 25);
        pnlMeio.add(lblDtEmissao);//adicionando o label configurado a janela

        txtDtEmissao = new JTextField(7);
        txtDtEmissao.setText("2");
        txtDtEmissao.setBounds(655, 70, 25, 25);
        txtDtEmissao.addFocusListener(new SelecionarTexto(txtDtEmissao));
        txtDtEmissao.addKeyListener(new limitaTexto(2,txtDtEmissao));
        pnlMeio.add(txtDtEmissao);//adicionando o campo de texto configurado a janela

        lblValorTotalInicio = new JLabel("Range de Valor Total:");//configurando o label
        lblValorTotalInicio.setBounds(510, 100, 140, 25);
        pnlMeio.add(lblValorTotalInicio);//adicionando o label configurado a janela

        txtValorTotalInicio = new JTextField(7);
        txtValorTotalInicio.setText("200,53");
        txtValorTotalInicio.setBounds(655, 100, 120, 25);
        txtValorTotalInicio.addFocusListener(new SelecionarTexto(txtValorTotalInicio));
        txtValorTotalInicio.addKeyListener(new limitaTexto(14,txtValorTotalInicio));
        pnlMeio.add(txtValorTotalInicio);//adicionando o campo de texto configurado a janela

        lblValorTotalFim = new JLabel("Fim:");//configurando o label
        lblValorTotalFim.setBounds(780, 100, 40, 25);
        pnlMeio.add(lblValorTotalFim);//adicionando o label configurado a janela

        txtValorTotalFim = new JTextField(7);
        txtValorTotalFim.setText("15800,10");
        txtValorTotalFim.setBounds(825, 100, 120, 25);
        txtValorTotalFim.addFocusListener(new SelecionarTexto(txtValorTotalFim));
        txtValorTotalFim.addKeyListener(new limitaTexto(14,txtValorTotalFim));
        pnlMeio.add(txtValorTotalFim);//adicionando o campo de texto configurado a janela

        lblDesconto = new JLabel("Desconto:");//configurando o label
        lblDesconto.setBounds(510, 130, 140, 25);
        pnlMeio.add(lblDesconto);//adicionando o label configurado a janela

        txtDesconto = new JTextField(7);
        txtDesconto.setText("0");
        txtDesconto.setBounds(655, 130, 25, 25);
        txtDesconto.addFocusListener(new SelecionarTexto(txtDesconto));
        txtDesconto.addKeyListener(new limitaTexto(14,txtDesconto));
        pnlMeio.add(txtDesconto);//adicionando o campo de texto configurado a janela

        lblXML = new JLabel("XML base");
        lblXML.setBounds(15, 5, 350, 15);
        pnlMeio.add(lblXML);

        lblXMLEscolhido = new JLabel();
        lblXMLEscolhido.setBounds(15, 20, 350, 15);
        pnlMeio.add(lblXMLEscolhido);

        txtXML = new JTextArea();
        txtXML.setBorder(BorderFactory.createLineBorder(Color.black));
        txtXML.setBounds(10, 40, 480, 550);
        pnlMeio.add(txtXML);
        scrlPnlTxtXml = new JScrollPane();

        lblResultado = new JLabel("NF Gerada");
        lblResultado.setBounds(700, 270, 150, 30);
        pnlMeio.add(lblResultado);
        txtResultado = new JTextArea();
        txtResultado.setBorder(BorderFactory.createLineBorder(Color.black));
        txtResultado.setBounds(690, 300, 400, 280);
        pnlMeio.add(txtResultado);

        pnlBot = new JPanel();
        pnlBot.setLayout(null);
        pnlBot.setBounds(0, 626, 1120, 35);
        pnlBot.setBackground(new Color(50, 240, 240));

        lblData = new JLabel();
        lblData.setBounds(10, 2, 250, 30);
        pnlBot.add(lblData);

        lblPagamento = new JLabel();
        lblPagamento.setBounds(210, 2, 250, 30);
        pnlBot.add(lblPagamento);

        lblNumProposta = new JLabel();
        lblNumProposta.setBounds(480, 2, 200, 30);
        pnlBot.add(lblNumProposta);

        btnAtualizar = new JButton("Gerar NF");
        btnAtualizar.setBounds(640, 2, 50, 28);
        btnAtualizar.addActionListener(new Atualizar());
        btnAtualizar.setEnabled(false);
        String f5ToolTip = "<html>Preencha o CPF e Numero da proposta, então atualize." +
                "<br>(este botão fica ativo após escolher um arquivo.)</html>";
        byte[] f5ToolTipArr = f5ToolTip.getBytes();
        btnAtualizar.setToolTipText(new String(f5ToolTipArr, StandardCharsets.UTF_8));
        pnlBot.add(btnAtualizar);

        btnSalvarResultado = new JButton("Salvar Resultado");
        btnSalvarResultado.setBounds(695, 2, 130, 28);
        btnSalvarResultado.addActionListener(new Salvar());
        btnSalvarResultado.setEnabled(false);
        pnlBot.add(btnSalvarResultado);

        pnlGeral.add(pnlTopo);
        pnlGeral.add(pnlMeio);
        pnlGeral.add(pnlBot);
        //pnlGeral.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        pnlGeral.setVisible(true);



    }
    private static int getContaProposta(Node parentNode,String proposta) {
        int qtdEx = 0;
        NodeList nList = (NodeList) parentNode.getParentNode();

        for (int i = 0; i < nList.getLength(); i++) {
            Node n = nList.item(i);

            String propostaNo = n.getTextContent();
            if (propostaNo == null)
                propostaNo = "";
            if (proposta == null)
                proposta = "";
            if (propostaNo.contains(proposta)) {
                qtdEx++;
            }
        }

        return qtdEx;
    }
    public void lerXml(String arquivo) {
        String valor = "";
        String invoice = "";
        String apoliceNum = "";
        StringBuilder dataTransacao = new StringBuilder();
        String modoPagamento = "";
        try {
            txtResultado.setText("");
            File file = new File(arquivo);
            // Create a new XML document.
            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = fabrica.newDocumentBuilder();
            Document doc = db.parse(file);

            // Create a transformer factory.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // Create a transformer.
            Transformer transformer = transformerFactory.newTransformer();

            // Set the output format for the transformer.
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Create a string writer.
            StringWriter stringWriter = new StringWriter();

            // Write the XML document to the string writer.
            transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));

            // Get the string representation of the XML document.
            String xmlData = stringWriter.toString();
            /*
            // Print the XML data.
            System.out.println(xmlData);


            NodeList nodeList = doc.getElementsByTagName("ledgerJournalTrans");
            Node nodeJournal = nodeList.item(0);

            OutputFormat format = new OutputFormat(doc);    // Serialize DOM
            format.setIndenting(true);
            format.setIndent(6);
            StringWriter stringOut = new StringWriter();    // Write to a String
            XMLSerializer serial = new XMLSerializer(stringOut, format);
            serial.asDOMSerializer();  // As a DOM Serializer
            serial.serialize(doc.getDocumentElement());
            String xmlData = stringOut.toString();
            */
            txtXML.setText(xmlData);
            scrlPnlTxtXml.setViewportView(txtXML);
            scrlPnlTxtXml.setBounds(10, 40, 482, 550);
            pnlMeio.add(scrlPnlTxtXml);

            Node invoiceNode = doc.getElementsByTagName("nNF").item(0);
            Node newInvoiceNode = doc.createElement("nNF");
            newInvoiceNode.setTextContent(getNumeroNf(Integer.parseInt(txtNumNfInicio.getText()),Integer.parseInt(txtNumNfFim.getText())));

            doc.replaceChild(invoiceNode, newInvoiceNode);
            txtResultado.setText(doc.getTextContent());
/*
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element contemApolice = (Element) nodeList.item(i);

                if (contemApolice.getTextContent().contains(txtProposta.getText())) {
                    //JOptionPane.showMessageDialog( null, contemApolice.getElementsByTagName("Invoice").toString());
                    NodeList nodeDaProposta = contemApolice.getChildNodes();

                    //JOptionPane.showMessageDialog( null, nodeDaProposta.getLength());
                    for (int j = 0; j < nodeDaProposta.getLength(); j++) {
                        Node nodeAtual =  nodeDaProposta.item(j);

                        //JOptionPane.showMessageDialog( null, nodeAtual.getParentNode().getNodeName());
                        if (nodeAtual.getNodeName().equals("nDup"))
                            valor = nodeAtual.getTextContent();
                        if (nodeAtual.getNodeName().equals("Invoice"))
                            invoice = nodeAtual.getTextContent();
                        if (nodeAtual.getNodeName().equals("apolice"))
                            apoliceNum = nodeAtual.getTextContent();
                        if (nodeAtual.getNodeName().equals("PaymMode"))
                            modoPagamento = nodeAtual.getTextContent();
                        if (nodeAtual.getNodeName().equals("TransDate"))
                            dataTransacao = new StringBuilder(nodeAtual.getTextContent());

                        //System.out.println(nodeAtual.getTagName() + " : " + nodeAtual.getTextContent());
                    }

                    txtResultado.setText("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<ledgerJournalStatus>\n" +
                            "      <ledgerJournalTrans>\n" +
                            "            <dataAreaId>04</dataAreaId>\n" +
                            "            <fiscalEstablishment>001</fiscalEstablishment>\n" +
                            "            <invoiceId>" + invoice + "</invoiceId>\n" +
                            "            <salesId>" + invoice + "</salesId>\n" +
                            "            <cnpjcpfNum>" + txtCnpj.getText() + "</cnpjcpfNum>\n" +
                            "            <transDate>06/10/2021</transDate>\n" +
                            "            <paymentStatus>3</paymentStatus>\n" +
                            "            <finInterestAmountCur>0.0000</finInterestAmountCur>\n" +
                            "            <amountCurDebit>" + valor + "</amountCurDebit>\n" +
                            "            <amountCur>" + valor + "</amountCur>\n" +
                            "            <cashDiscAmount>0.00</cashDiscAmount>\n" +
                            "            <voucher>SEGCTPA_000" + invoice + "</voucher>\n" +
                            "            <paymMode>BOL_SAN240</paymMode>\n" +
                            "            <vl_multa>0.0000</vl_multa>\n" +
                            "            <dv_nao_envia_interface>0</dv_nao_envia_interface>\n" +
                            "            <Apolice>" + apoliceNum + "</Apolice>\n" +
                            "            <Parcela>1</Parcela>\n" +
                            "      </ledgerJournalTrans>\n" +
                            "</ledgerJournalStatus>");
                    lblPagamento.setText("Modo de pagamento: " + modoPagamento);
                    dataTransacao.insert(0, "Data da Transação: ");

                    byte[] dataT = dataTransacao.toString().getBytes();
                    lblData.setText(new String(dataT, StandardCharsets.UTF_8));

                    lblNumProposta.setText("Proposta \n" + (i+1) + "/" + nodeList.getLength());
                    int props = getContaProposta(nodeJournal,txtProposta.getText());
                   // System.out.println("numero de props " + props);
                    if(props >1 && !Objects.equals(txtProposta.getText(), " ")){
                        String cont = "<html>Foram encontradas "+props+" propostas"+
                                " no XML, deseja <br>continuar com a atual?<br>(O valor em amountCur e data transação<br> podem indicar a proposta correta)</html>";
                        byte[] contUTF = cont.getBytes();
                        int continua = JOptionPane.showConfirmDialog(null,new String(contUTF, StandardCharsets.UTF_8) , "Confirmacao de proposta",
                                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if(continua == 0)
                            break;

                    }
                    else
                            break;
                } else {
                    txtResultado.setText("Proposta nao encontrada");
                }
            }*/
            btnAtualizar.setEnabled(true);
            Action apertaF5 = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    lerXml(getArq());
                }
            };
            btnAtualizar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "apertaF5");
            btnAtualizar.getActionMap().put("apertaF5",apertaF5);

            btnSalvarResultado.setEnabled(true);
        } catch (Exception e) {
            System.out.println("msg excecao: " + e);
        }
    }

    public String getNumeroNf(int min, int max) {
        Random rand = new Random();
        return String.valueOf(rand.nextInt(max - min + 1) + min);
    }
    public static String getDataEmissao(int days) {
        LocalDate today = LocalDate.now();
        LocalDate emissionDate = today.minusDays(days);
        Instant instant = emissionDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Date date = Date.from(instant);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String dataEmissao = df.format(date).replaceAll("(\\-\\d\\d)(\\d\\d)", "$1:$2");
        return dataEmissao;
    }

    public class Salvar implements ActionListener {
        public String arquivoSalvo;

        public void actionPerformed(ActionEvent ev) {
            JFileChooser fcSalvar = new JFileChooser();
            fcSalvar.setCurrentDirectory(new File("\\\\srvomt302\\InterfacesI4PRO\\Processar\\SAX_SI4PRO_AC"));
            fcSalvar.setDialogTitle("Salve o XML");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HHmm");
            arquivoSalvo = "NF_" +  txtNumNfInicio.getText() +
                    "_TESTE-PRODUTO-" +dtf.format(LocalDateTime.now()) + ".xml";

            fcSalvar.setSelectedFile(new File(arquivoSalvo));
            int salvo = fcSalvar.showSaveDialog(Principal.this);

            if(salvo == 0){
                PrintWriter pw;

                try {
                    pw = new PrintWriter(new FileWriter(fcSalvar.getSelectedFile()));

                    pw.println(txtResultado.getText());
                    pw.close();
                    JOptionPane.showMessageDialog(null, "Arquivo salvo");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Arquivo nao foi criado");
                }
            }
        }
    }

    public class Atualizar implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            lerXml(getArq());
        }
    }


    public class SelecionarTexto implements FocusListener {

        private JTextField campoAlvo;
        public SelecionarTexto(JTextField campoAlvo) {
            this.campoAlvo = campoAlvo;
        }
        @Override
        public void focusGained(FocusEvent e){
            campoAlvo.selectAll();
        }
        @Override
        public void focusLost(FocusEvent e) {

        }
    }


    public class Choose extends Component implements ActionListener {
        public String arquivo;

        public void actionPerformed(ActionEvent ev) {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("E:\\qa\\nf"));
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
    public class limitaTexto implements KeyListener {
        private int tamanho;
        private JTextField campoAlvo;
        public limitaTexto(int tamanho, JTextField campoAlvo) {
            this.tamanho = tamanho;
            this.campoAlvo = campoAlvo;
        }
        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println(getDataEmissao(2));
            if (campoAlvo.getSelectedText() != null && campoAlvo.getSelectedText().length() == tamanho){
                campoAlvo.setText("");
            }
            else if (campoAlvo.getText().length() >= tamanho) // limitar a quantidade de caracteres na chamada
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
                        campoAlvo.setText(textoColado.replaceAll("\\.","").replaceAll("\\-","").replaceAll("/","").substring(0,tamanho));
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}