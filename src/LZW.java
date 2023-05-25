
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


        for (int i = 0; i < bytes.length; i++) {
            if (list.size() == 256 * 2) {
                ferNetSaLListta(list, os);
                list = new ArrayList<>();
            }
            if (i == 0 || i == bytes.length - 1) {
                list.add(pos);
                list.add(bytes[i]);
            } else {
                pos = aconseguirPosDelByte(bytes, list, pos, i);
            }
        }
        ferNetSaLListta(list, os);
        is.close();
        os.close();
    }

    private static byte aconseguirPosDelByte(byte[] bytes, List<Byte> list, byte pos, int i) {
        int l = 1;
        boolean elByteIndexEsUn0 = false;
        for (int j = 1; j < list.size(); j = j + 2) {
            elByteIndexEsUn0 = bytes[i] == list.get(j) && pos == list.get(j - 1);
            if (elByteIndexEsUn0) {
                pos = (byte) (l);
                break;
            }
            l++;
        }
        pos = afegirByteYPosSiElByteIndexEsUn0(bytes, list, pos, i, elByteIndexEsUn0);
        return pos;
    }

    private static byte afegirByteYPosSiElByteIndexEsUn0(byte[] bytes, List<Byte> list, byte pos, int i,
                                                         boolean elByteIndexEsUn0) {
        if (!elByteIndexEsUn0) {
            list.add(pos);
            pos = 0;
            list.add(bytes[i]);
        }
        return pos;
    }

    private static void ferNetSaLListta(List<Byte> list, OutputStream os) throws IOException {
        for (Byte aByte : list) {
            os.write(aByte);
        }
    }

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = is.readAllBytes();
        for (int i = 1; i < bytes.length;  i = i + 2) {
            int index = bytes[i - 1];
            index = index < 0 ? index + 256 : index;
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
        int index1 = (index * 2);
        List<Byte> li = new ArrayList<>();
            while (index1 != 0){
                li.add(contingut);
                contingut = bytes[index1 - 1];
                index1 = bytes[index1 - 2];
                if (index1 < 0) index1 = index1 + 256;
                index1 = index1 * 2;
            }
            li.add(contingut);
        for (int i = li.size() - 1; i >= 0; i--) {
            os.write(li.get(i));
        }
    }
}
