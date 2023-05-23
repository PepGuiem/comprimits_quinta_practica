
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LZW {
    public static void compress(InputStream is, OutputStream os) throws IOException {
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
        os.close();
    }

    private static byte aconseguirPosDelByte(byte[] bytes, List<Byte> list, byte pos, int i) {
        int l = 1;
        boolean si = false;
        for (int j = 1; j < list.size(); j = j + 2) {
            si = bytes[i] == list.get(j) && pos == list.get(j - 1);
            if (si) {
                pos = (byte) (l);
                break;
            }
            l++;
        }
        if (!si) {
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

    public static void decompress(InputStream is, OutputStream os) {
        
    }
}
