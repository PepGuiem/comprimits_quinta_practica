
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LZW {
    public static void compress(InputStream is, OutputStream os) throws IOException {
        /*Bateria de variables que utilitzem.*/
        byte[] bytes = is.readAllBytes();
        List<Byte> list = new ArrayList<>();
        byte pos = 0;

        /*Fem un bucle que passi per tots els elements de l'array*/
        for (int i = 0; i < bytes.length; i++) {

            /*Si la longitud de la llista és igual a 512*/
            if (list.size() == 512) {

                /*Cridem la funció ferNetSaLlista que afegeix els elements de la llista a l'arxiu*/
                afegirElementsDeLlistaAArxiu(list, os);

                /*I fem que la llista comenci de nou*/
                list = new ArrayList<>();
            }

            /*Si és el començament del bucle o el final del bucle*/
            if (i == 0 || i == bytes.length - 1) {

                /*Afegim la posició*/
                list.add(pos);

                /*I el byte*/
                list.add(bytes[i]);

                /*Si no*/
            } else {

                /*pos valdrà el que ens retorni la funció*/
                pos = aconseguirPosDelByte(bytes, list, pos, i);
            }
        }

        /*Cridem la funció per a escriure els elements de la llista a l'arxiu*/
        afegirElementsDeLlistaAArxiu(list, os);

        /*Finalment tencam els arxius*/
        is.close();
        os.close();
    }

    /*Aquesta funció ens retorna un byte que vendria a ser la posició del byte*/
    private static byte aconseguirPosDelByte(byte[] bytes, List<Byte> list, byte pos, int i) {

        /*Cream una variable que ens servira com a index*/
        int index = 1;

        /*Variable booleana de us temporal*/
        boolean elByteIndexEsUn0 = false;

        /*Bucle que recorri tots els elements de la llista*/
        for (int j = 1; j < list.size(); j += 2) {

            /*Si el byte del qual ens passa és igual al de la llista i la posició és la mateixa ens dona true*/
            elByteIndexEsUn0 = bytes[i] == list.get(j) && pos == list.get(j - 1);

            /*Si el boolean és true*/
            if (elByteIndexEsUn0) {

                /*pos valdrà el que val index*/
                pos = (byte) (index);

                /*I aturam el bucle*/
                break;
            }

            /*Incrementem 1 al index*/
            index++;
        }

        /*I pos valdrà el que val la funció*/
        pos = afegirByteIPosSiElByteIndexEsUn0(bytes, list, pos, i, elByteIndexEsUn0);

        /*Retornam pos*/
        return pos;
    }

    /*Funció que ens retorna el 0 a pos si es compleix la condició*/
    private static byte afegirByteIPosSiElByteIndexEsUn0(byte[] bytes, List<Byte> list, byte pos, int i,
                                                         boolean elByteIndexEsUn0) {

        /*Si el boolean es false*/
        if (!elByteIndexEsUn0) {

            /*Afegim la posició i el byte a la llista*/
            list.add(pos);
            list.add(bytes[i]);

            /*I fem que pos valgui 0*/
            pos = 0;
        }

        /*Finalment retornam pos*/
        return pos;
    }

    /*Funció per afegir els elements de la llista a l'arxiu*/
    private static void afegirElementsDeLlistaAArxiu(List<Byte> list, OutputStream os) throws IOException {
        for (Byte aByte : list) {
            os.write(aByte);
        }
    }

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = is.readAllBytes();
        for (int i = 1; i < bytes.length;  i = i + 2) {
            int index = bytes[i - 1] & 0xff;
            if (index == 0){
                os.write(bytes[i]);
            }else{
                escriureBytes(index,bytes[i],bytes,os);
            }
        }
        is.close();
        os.close();
    }

    private static void escriureBytes(int index, byte contingut, byte[] bytes, OutputStream os) throws IOException {
        int index1 = index * 2;
        List<Byte> li = new ArrayList<>();
            while (index1 != 0){
                li.add(contingut);
                contingut = bytes[index1 - 1];
                index1 = (bytes[index1 - 2] & 0xff) * 2;
            }
            li.add(contingut);
        for (int i = li.size() - 1; i >= 0; i--) {
            os.write(li.get(i));
        }
    }
}
