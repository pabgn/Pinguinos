/**
 * Clase principal controladora del juego.
 * Contiene la parte grafica y metodos necesarios de calculo
 * para el camino de los jugadores y suma de puntos.
 *
 * Creado por Pablo Garcia-Nieto Rodriguez (H2)
 */
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
public class Programa extends JPanel {
    //Creamos el Panel extendiendo a JPanel (Swing)
    private static int[][] tablero;
    private static JFrame frame;
    private static DefaultTableModel tableModel;
    private static DefaultTableModel pointsModel;
    private static int turno=1;
    static Pinguino[] jugadores= new Pinguino[2];
    static int deads=0;
    static int[] puntos = new int[2];
    static int[] remaining = new int[2];
    public Programa() throws IOException {
        //Generamos la tabla de puntuaci??n
        JPanel p = new JPanel();
        pointsModel =  new DefaultTableModel()
        {
            @Override

            public boolean isCellEditable(int row, int col) {
                return false;

            }
        };
        final JTable  pointsTable = new JTable(pointsModel);
        pointsModel.addColumn("Jugador");
        pointsModel.addColumn("Puntos");
        pointsModel.addRow(new Object[]{"Jugador 1", "0"});
        pointsModel.addRow(new Object[]{"Jugador 2", "0"});
        pointsTable.setPreferredScrollableViewportSize(new Dimension(500, 500));
        pointsTable.setFillsViewportHeight(true);
        pointsTable.setRowHeight(100);
        pointsTable.getTableHeader().setReorderingAllowed(false);
        p.add(pointsTable);
        add(p);
        //Generamos la tabla del juego
        tableModel = new DefaultTableModel()
        {
            @Override
            public Class getColumnClass(int column)
            {
                //Nos aseguramos que la respuesta a las celdas sea siempre un tipo ImageIcon
                return ImageIcon.class;
            }
            public boolean isCellEditable(int row, int col) {
               return false;

            }
        };
        //A??adimos las columnas
        tableModel.addColumn("1");
        tableModel.addColumn("2");
        tableModel.addColumn("3");
        tableModel.addColumn("4");
        tableModel.addColumn("5");
        // Ajustes de tama??os y colores
        final JTable  table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 500));
        table.setFillsViewportHeight(true);
        table.setRowHeight(100);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionBackground(Color.white);
        table.setTableHeader(null);
        //Interceptamos el doble click en una celda
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    //Comprobamos si es un movimiento permitido
                    if(deads!=2 && jugadores[turno-1].esMovimientoLegal(row, column, tablero)){
                        //Animamos el recorrido a la posici??n pedida
                        animar(jugadores[turno-1], row, column);
                    }

                }
            }
        });
        //A??adimos zona de scroll si fuese necesario
        JScrollPane panelConScroll = new JScrollPane(table);

        add(panelConScroll);
    }

    private static void crearUI() {
        //Creamos la pantalla inicial con su t?tulo e icono
        frame = new JFrame("Juego Pinguinos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon(Programa.class.getResource("icons/fish04.png"));
        frame.setIconImage(img.getImage());


        //Configuramos el contenido
        Programa panel;
        try{
            panel = new Programa();
            panel.setOpaque(true);
            frame.setContentPane(panel);

            //Metodos a ser llamados para mostrar la ventana
            frame.pack();
            frame.setVisible(true);
        }catch(IOException e){
        e.printStackTrace();
        }



    }
    private static void rellenar(int[][] tablero){
        for(int x=0;x<tablero.length;x++){
            for(int y=0;y<tablero[x].length;y++){
                int fish = 1 + (int)(Math.random()*3);
                tablero[x][y]=fish;
            }
            tableModel.insertRow(x, new Object[]{});


        }
    }
    private static void updateRows(){
        for(int x=0;x<tablero.length;x++){
            for(int y=0;y<tablero[x].length;y++){
                String rem="";
                if(tablero[x][y]==4 || tablero[x][y]==5){
                    rem=""+remaining[tablero[x][y]-4];
                }
                tableModel.setValueAt(new ImageIcon(Programa.class.getResource("icons/fish"+rem + tablero[x][y]+ ".png")), x, y);
            }
        }
    }
    private static int[] randomPosition(){

        int[] pos =  {0 + (int)(Math.random()*4), 0 + (int)(Math.random()*4)};
        return pos;
    }
    private static void sumaPuntos(int p, int x, int y){
        jugadores[p-1].setPeces_comidos(jugadores[p-1].getPeces_comidos()+remaining[p-1]);
        puntos[p-1]=puntos[p-1]+remaining[p-1];
        pointsModel.setValueAt(puntos[p-1],p-1,1);
    }
    private static void animar(Pinguino p, int x, int y){
        tablero[p.getCasillaX()][p.getCasillaY()]=0;
        int movimientos = Math.abs((x - p.getCasillaX()) + (y - p.getCasillaY()));
        for(int t=0;t<=movimientos;t++){
            if((x-p.getCasillaX())>0){

                p.setCasillaX(p.getCasillaX()+1);
            }
            if((x-p.getCasillaX())<0){
                p.setCasillaX(p.getCasillaX()-1);
            }
            if((y-p.getCasillaY())>0){
                p.setCasillaY(p.getCasillaY() + 1);
            }
            if((y-p.getCasillaY())<0){
                p.setCasillaY(p.getCasillaY() - 1);

            }
        }
        remaining[p.getJugador()-1]=tablero[p.getCasillaX()][p.getCasillaY()];
        tablero[p.getCasillaX()][p.getCasillaY()]=3+p.getJugador();
        updateRows();
        changeTurn();
    }
    public static void changeTurn(){
        turno= turno==1 ? 2:1;

        sumaPuntos(turno, jugadores[turno-1].getCasillaX(), jugadores[turno-1].getCasillaY());
        remaining[turno-1]=0;
        updateRows();

        setNames();
        pointsModel.setValueAt("<html><b>Jugador " + turno + "</b></html>", turno - 1,0);
        if(jugadores[turno-1].getVivo()){
        if(checkAlive(jugadores[turno-1])==false){
            deads++;
            if(deads==2){
                tablero[jugadores[turno-1].getCasillaX()][jugadores[turno-1].getCasillaY()]=60+jugadores[turno-1].getJugador();
                updateRows();
                JOptionPane.showMessageDialog(frame, "Fin del juego", "Juego", JOptionPane.INFORMATION_MESSAGE);

            }else{
            changeTurn();
            }
        }
        }else{
            changeTurn();
        }
    }
    public static boolean isFree(int x, int y){
        return x>=0 && y>=0 && x<=4 && y<=4 && tablero[x][y]!=0 && tablero[x][y]!=4 && tablero[x][y]!=5;
    }
    public static boolean checkAlive(Pinguino p){
        int posx = p.getCasillaX();
        int posy = p.getCasillaY();
        if(isFree(posx,posy+1) || isFree(posx,posy-1) || isFree(posx+1,posy) || isFree(posx-1,posy)){
            return true;
        }else{
            p.setVivo(false);
            return false;
        }

    }
    public static void setNames(){
        pointsModel.setValueAt("<html>Jugador 1</html>", 0, 0);
        pointsModel.setValueAt("<html>Jugador 2</html>", 1, 0);
    }
    /* Funcion inicial donde cargamos el hilo del UI e iniciamos jugadores */

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Generamos el tablero de 5x5
                tablero = new int[5][5];
                // Generamos la interfaz gr?fica
                crearUI();
                // Rellenamos aleatoriamente el tablero
                rellenar(tablero);
                // Mostramos en la tabla gr?fica el contenido
                remaining[0]=0;
                remaining[1]=0;
                updateRows();
                //Generamos los jugadores y sus posiciones iniciales
                int[] pos1={0,0};
                int[] pos2={0,0};
                // Nos aseguramos que sean diferentes
                while(pos1[0]==pos2[0] && pos1[1]==pos1[1]){
                    pos1=randomPosition();
                    pos2=randomPosition();
                }
                // Creamos las instancia de la clase pinguinos y las asignamos a un array
                Pinguino p1 = new Pinguino(1, pos1[0], pos1[1]);
                Pinguino p2 = new Pinguino(2, pos2[0], pos2[1]);
                jugadores[0] = p1;
                jugadores[1] = p2;

                // Los situamos en el tablero
                tablero[pos1[0]][pos1[1]]=4;
                tablero[pos2[0]][pos2[1]]=5;
                //Marcamos el turno
                pointsModel.setValueAt("<html><b>Jugador 1</b></html>", 0, 0);
                //Mostramos gr?ficamente el juego
                updateRows();


            }
        });
    }
}