/**
 * Created by Pablo on 23/12/13.
 */
public class Pinguino {

    private int jugador;
    private int peces_comidos;
    private int casillaX;
    private int casillaY;
    private boolean vivo;

    public Pinguino(int j, int cx, int cy){
        this.jugador= j;
        this.casillaX=cx;
        this.casillaY=cy;
        this.vivo = true;
        this.peces_comidos=0;
    }
    public int getJugador(){
        return jugador;
    }
    public int  getPeces_comidos(){
        return peces_comidos;
    }
    public int getCasillaX(){
        return casillaX;
    }
    public int getCasillaY(){
        return casillaY;
    }
    public boolean getVivo(){
        return vivo;
    }
    public void setJugador(int j){
        jugador=j;
    }
    public void setPeces_comidos(int p){
        peces_comidos=p;
    }
    public void setCasillaX(int cx){
        casillaX=cx;
    }
    public void setCasillaY(int cy){
        casillaY=cy;
    }
    public void setVivo(boolean v){
        vivo=v;
    }

    public boolean esMovimientoLegal(int x, int y, int[][] tablero){
        boolean legal=true;
        //Izquierda o derecha
        if(x==casillaX){
            if(y-casillaY>0){
                for(int t=casillaY+1;t<=y;t++){
                    if(tablero[x][t]==0 || tablero[x][t]==4 || tablero[x][t]==5){
                        legal=false;
                    }
                }
            }else{
                for(int t=casillaY-1;t>=y;t--){
                    if(tablero[x][t]==0 || tablero[x][t]==4 || tablero[x][t]==5){
                        legal=false;
                    }
                }
            }


        }
        else{
            if(y==casillaY){
                if(x-casillaX>0){
                    for(int t=casillaX+1;t<=x;t++){
                        if(tablero[t][y]==0 || tablero[t][y]==4 || tablero[t][y]==5){
                            legal=false;
                        }
                    }
                }else{
                    for(int t=casillaX-1;t>=x;t--){
                        if(tablero[t][y]==0 || tablero[t][y]==4 || tablero[t][y]==5){
                            legal=false;
                        }
                    }
                }
            }else{
                legal=false;
            }
        }


    return legal;
    }

}
