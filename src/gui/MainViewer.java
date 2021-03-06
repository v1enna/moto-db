package gui;

import javax.swing.JFrame;
import config.Config;
import endpoint.Actor;
import java.awt.event.MouseAdapter;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.io.IOException;
import java.util.ArrayList;

public class MainViewer extends Actor {

    /*
     * La JTextArea result è l'area di testo in cui verranno stampati i risultati
     * delle query in funzione dell'operazione selezionata.
     */
    private static JTextPane result;
    private static ArrayList<String> resultContent;

    public MainViewer() {
        debug("?Genero l'interfaccia...");
        new MainFrame();
    }

    public class MainFrame extends JFrame {

        private static final long serialVersionUID = 1L;

        public MainFrame() {
            /*
             * Configurazione di default del MainFrame. Per informazioni relative all'icona
             * del frame, consultare DebugFrame().
             */
            setTitle(Config.MAIN_VIEWER_FRAME_TITLE);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(Config.MAIN_VIEWER_FRAME_SIZE[0], Config.MAIN_VIEWER_FRAME_SIZE[1]);
            setLocationRelativeTo(null); // Posiziona centralmente il frame alla creazione

            try {
                setIconImage(ImageIO.read(new File(Config.MOTODB_ICON_PATH)));
            } catch (IOException e) {
                debug("!Impossibile trovare l'icona dell'applicazione. Ricontrolla la configurazione.");
            }

            /*
             * Routine di popolamento della GUI. Riferirsi alla documentazione per i singoli
             * componenti. -- Gerarchia: [LeftSide, RightSide] -- [LeftSide]: {Buttons}
             * [RightSide]: {Outer > Scrollbar > TextArea}
             */
            JPanel rightSide = createRightSide();
            JPanel leftSide = createLeftSide();

            add(leftSide, BorderLayout.WEST);
            add(rightSide, BorderLayout.CENTER);

            setVisible(true);

            resultContent = new ArrayList<String>();

            MainViewer.display(Config.MAIN_VIEWER_FRAME_DEFAULT_TEXT);

            debug(":MainFrame creato e popolato con successo");
        }

        private JPanel createButton(int operationId) {
            /*
             * Gerarchia: Container > Button > Label
             */
            JPanel buttonContainer = new JPanel(new GridLayout(1, 1));
            JPanel button = new JPanel();
            JLabel label = new JLabel("Operazione " + operationId);

            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setBorder(new EmptyBorder(Config.MAIN_VIEWER_FRAME_INNER_BUTTON_PADDING[0],
                    Config.MAIN_VIEWER_FRAME_INNER_BUTTON_PADDING[1], Config.MAIN_VIEWER_FRAME_INNER_BUTTON_PADDING[2],
                    Config.MAIN_VIEWER_FRAME_INNER_BUTTON_PADDING[3]));
            button.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_BUTTON_BACKGROUND));
            button.addMouseListener(new ButtonListener(operationId - 1));

            label.setForeground(Color.WHITE);
            label.setFont(new Font(Config.MAIN_VIEWER_FRAME_BUTTON_FONT, Font.BOLD,
                    Config.MAIN_VIEWER_FRAME_BUTTON_FONT_SIZE));

            button.add(label);

            buttonContainer.add(button);

            return buttonContainer;
        }

        private JPanel createRightSide() {
            /*
             * Elabora il lato destro del MainFrame. In particolare, nella spaziatura viene
             * implementata una scrollbar personalizzata (ScrollPane, sotto forma di
             * JScrollPane) e all'interno di quest'ultima vengono poi inseriti più layer
             * grafici per l'area di testo (resultArea, sotto forma di JPanel) nella quale
             * verranno stampati i risultati delle operazioni.
             */
            JPanel outer = new JPanel();

            outer.setLayout(new GridLayout(1, 1));
            outer.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_OUTER_BACKGROUND));
            outer.setBorder(new EmptyBorder(Config.MAIN_VIEWER_FRAME_OUTER_PADDING[0],
                    Config.MAIN_VIEWER_FRAME_OUTER_PADDING[1], Config.MAIN_VIEWER_FRAME_OUTER_PADDING[2],
                    Config.MAIN_VIEWER_FRAME_OUTER_PADDING[3]));

            outer.add(createScrollPane(), BorderLayout.CENTER);

            return outer;

        }

        private JPanel createLeftSide() {
            /*
             * Gerarchia: Container > Wrapper > TextArea
             */
            JPanel buttonsContainer = new JPanel();
            JPanel buttonsWrapper = new JPanel();

            buttonsContainer.setBorder(new EmptyBorder(Config.MAIN_VIEWER_FRAME_OUTER_BUTTON_PADDING[0],
                    Config.MAIN_VIEWER_FRAME_OUTER_BUTTON_PADDING[1], Config.MAIN_VIEWER_FRAME_OUTER_BUTTON_PADDING[2],
                    Config.MAIN_VIEWER_FRAME_OUTER_BUTTON_PADDING[3]));
            buttonsContainer.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_BUTTON_BACKGROUND));

            buttonsWrapper.setLayout(new GridLayout(Config.Operations.size(), 1));

            for (int i = 0; i < Config.Operations.size(); i++)
                buttonsWrapper.add(createButton(i + 1));

            buttonsContainer.add(buttonsWrapper);

            return buttonsContainer;
        }

        private JPanel createResultPanel() {
            /*
             * Gerarchia: Container > Wrapper > TextArea
             */
            JPanel resultContainer = new JPanel(new GridLayout(1, 1));
            JPanel resultWrapper = new JPanel(new GridLayout(1, 1));
            JTextPane result = new JTextPane();

            resultContainer.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_INNER_BACKGROUND));
            resultContainer.setBorder(new EmptyBorder(Config.MAIN_VIEWER_FRAME_RESULT_OUTER_PADDING[1],
                    Config.MAIN_VIEWER_FRAME_RESULT_OUTER_PADDING[1], Config.MAIN_VIEWER_FRAME_RESULT_OUTER_PADDING[2],
                    Config.MAIN_VIEWER_FRAME_RESULT_OUTER_PADDING[3]));

            result.setBorder(new EmptyBorder(Config.MAIN_VIEWER_FRAME_RESULT_INNER_PADDING[0],
                    Config.MAIN_VIEWER_FRAME_RESULT_INNER_PADDING[1], Config.MAIN_VIEWER_FRAME_RESULT_INNER_PADDING[2],
                    Config.MAIN_VIEWER_FRAME_RESULT_INNER_PADDING[3]));
            result.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_INNER_BACKGROUND));
            result.setForeground(Color.WHITE);
            result.setEditable(false);
            result.setFont(new Font(Config.MAIN_VIEWER_FRAME_RESULT_FONT, Font.PLAIN,
                    Config.MAIN_VIEWER_FRAME_RESULT_FONT_SIZE));
            result.setContentType("text/html");

            resultWrapper.add(result);
            resultContainer.add(resultWrapper);

            setResult(result);

            return resultContainer;
        }

        public JScrollPane createScrollPane() {
            /*
             * Routine di stilizzazione della scrollbar. Per stilizzare la scrollbar è stata
             * usata un'API pubblica integrata in Swing, ossia UIManager.
             */
            JScrollPane scrollPane = new JScrollPane(createResultPanel());

            UIManager.put("ScrollBar.thumb", new ColorUIResource(Color.decode(Config.SCROLLBAR_THUMB)));
            UIManager.put("ScrollBar.thumbDarkShadow",
                    new ColorUIResource(Color.decode(Config.SCROLLBAR_THUMB_DARK_SHADOW)));
            UIManager.put("ScrollBar.thumbShadow", new ColorUIResource(Color.decode(Config.SCROLLBAR_THUMB_SHADOW)));
            UIManager.put("ScrollBar.thumbHighlight",
                    new ColorUIResource(Color.decode(Config.SCROLLBAR_THUMB_HIGHLIGHT)));
            UIManager.put("ScrollBar.track", new ColorUIResource(Color.decode(Config.SCROLLBAR_TRACK)));

            scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode(Config.SCROLLBAR_BORDER), 2));
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI());
            scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI());

            return scrollPane;
        }

    }

    public static void display(String str) {
        String msg = "";
        switch (str.charAt(0)) {
            case '!':
                msg += "<b><span style='font-size: " + Config.DISPLAY_FAIL_FONT_SIZE + "; font-family:"
                        + Config.DISPLAY_FAIL_FONT + "; color:" + Config.DISPLAY_FAIL_FONT_COLOR + "'>"
                        + str.substring(1) + "</span></b><br />\n";
                break;

            case ':':
                msg += "<b><span style='font-size: " + Config.DISPLAY_SUCCESS_FONT_SIZE + "; font-family:"
                        + Config.DISPLAY_SUCCESS_FONT + "; color:" + Config.DISPLAY_SUCCESS_FONT_COLOR + "'>"
                        + str.substring(1) + "</span></b>\n";
                break;

            case '#':
                msg += "<b><span style='font-size: " + Config.DISPLAY_CODE_FONT_SIZE + "; font-family:"
                        + Config.DISPLAY_CODE_FONT + "; color:" + Config.DISPLAY_CODE_FONT_COLOR + "'>"
                        + str.substring(1) + "</span></b>\n";
                break;

            case '|':
                msg += str.substring(1);
                break;

            default:
                msg += "<span style='font-size: " + Config.DISPLAY_REGULAR_FONT_SIZE + "; font-family:"
                        + Config.DISPLAY_REGULAR_FONT + "; color:" + Config.DISPLAY_REGULAR_FONT_COLOR + "'>" + str
                        + "</span>\n";
                break;

        }

        resultContent.add(msg);

        String content = "";

        for (String prev : resultContent)
            content += prev + "<br />";

        result.setText(content);

    }

    /*
     * Getter: Result
     */
    public JTextPane getResult() {
        return result;
    }

    /*
     * Setter: Result
     */
    public void setResult(JTextPane newResult) {
        result = newResult;
    }

    public class ButtonListener extends MouseAdapter {

        /*
         * Logica di creazione del QueryFrame per la rispettiva operazione.
         */
        private int operationId;

        public ButtonListener(int operationId) {
            this.operationId = operationId;
        }

        /*
         * Al click del mouse su uno qualsiasi dei bottoni delle operazioni, genera un
         * QueryFrame dinamico per quella stessa operazione. Il QueryViewer delegherà al
         * QueryBuilder l'elaborazione della query grezza, se necessario.
         */
        public void mouseClicked(MouseEvent e) {
            debug("?Richiedo la creazione di un QueryViewer...");
            new QueryViewer(Config.Operations.get(operationId), result);

            MainViewer.display(":<b>Hai selezionato l'operazione " + (operationId + 1) + "</b><br />");

            debug(":QueryViewer creato con successo per l'operazione " + (operationId + 1));
        }

        /*
         * MouseEntered e MouseExited permettono di implementare una breve animazione
         * grafica al passaggio del mouse.
         */
        public void mouseEntered(MouseEvent e) {
            Object source = e.getSource();
            JPanel button = (JPanel) source;

            button.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_BUTTON_BACKGROUND_HOVER));
        }

        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            JPanel button = (JPanel) source;

            button.setBackground(Color.decode(Config.MAIN_VIEWER_FRAME_BUTTON_BACKGROUND));
        }

    }

}
