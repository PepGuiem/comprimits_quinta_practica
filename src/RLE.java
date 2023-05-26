import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RLE {
    public static void compress(InputStream is, OutputStream os) throws IOException {
        /*Bateria de variables que utilitzem.*/
        byte[] bytes = is.readAllBytes();
        int cont = 0;
        int contNum = 0;

        /*Bucle que pasa per tots els bytes*/
        for (int i = 0; i < bytes.length; i++) {

            /*Si i és major o igual a 1 i el byte anterior és igual a l'actual.*/
            if (i >= 1 && bytes[i - 1] == bytes[i]) {

                /*A contNum li sumem 1*/
                contNum++;

                /*Si contNum es igual a 257*/
                if (contNum == 257) {

                    /*Escrivim el comptador a l'arxiu*/
                    os.write(cont);

                    /*I fem que els comptadors tornin a tenir un valor de 0*/
                    cont = 0;
                    contNum = 0;
                }
            }

            /*Si contNum és major a 1 i l'anterior byte és el mateix que l'actual*/
            if (contNum > 1 && bytes[i - 1] == bytes[i]) {

                /*Sumem 1 al comptador*/
                cont++;

                /*Si no*/
            } else {

                /*Creem una variable temporal que guarda els valors de la funció que cridem*/
                int[] temp = aconseguirContIContNum(cont, contNum, os, bytes, i);

                /*cont valdrà la primera posició de la variable temporal*/
                cont = temp[0];

                /*contNum valdrà la segona posició de la variable temporal*/
                contNum = temp[1];
            }
        }

        /*Si cont és major a 0*/
        if (cont > 0) {

            /*Afegim el comptador a l'arxiu*/
            os.write(cont);
        }

        /*Finalment tencam els dos arxius*/
        is.close();
        os.close();
    }

    /*Funció per aconseguir el cont i contNum i escriure a l'arxiu els bytes o/i el comptador*/
    private static int[] aconseguirContIContNum(int cont, int contNum, OutputStream os, byte[] bytes, int i)
            throws IOException {

        /*Si comptador no es igual a 0*/
        if (cont != 0) {

            /*Escrivim a l'arxiu el comptador i el byte*/
            os.write(cont);
            os.write(bytes[i]);

            /*I posem els comptador a 0*/
            cont = 0;
            contNum = 0;

            /*Si no*/
        } else {

            /*Escrivim el byte*/
            os.write(bytes[i]);

            /*I si es compleix la funció*/
            if (seHaDeAfegirElComptador(bytes, i)) {

                /*Escrivim el comptador i tornem a posar a 0 el contNum*/
                os.write(cont);
                contNum = 0;
            }
        }

        /*Finalment retornam el cont i contNum*/
        return new int[]{cont, contNum};
    }

    /*Si i és major o igual a 1 i el byte anterior i actual són iguals, i, si i + 1 és menor a la longitud
    de l'array de bytes i el byte actual és diferent del següent o finalment i + 1 és igual a la longitud
     de l'array del byte.*/
    private static boolean seHaDeAfegirElComptador(byte[] bytes, int i) {
        return (i >= 1 && bytes[i - 1] == bytes[i]) && ((i + 1 < bytes.length && bytes[i] != bytes[i + 1])
                || i + 1 == bytes.length);
    }

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = is.readAllBytes();
        os.write(bytes[0]);
        for (int i = 1; i < bytes.length; i++) {
            if (bytes[i - 1] == bytes[i]) {
                os.write(bytes[i]);
                afegirNombres(bytes[i], bytes[i + 1], os);
                i++;
            } else {
                os.write(bytes[i]);
            }
        }
        is.close();
        os.close();
    }

    private static void afegirNombres(byte aByte, byte quantitatDeBytes, OutputStream os) throws IOException {
        int temp = quantitatDeBytes & 0xff;
        for (int i = 0; i < temp; i++) {
            os.write(aByte);
        }
    }
}
