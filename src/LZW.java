
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

    public static void decompress(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = is.readAllBytes();
        byte contingut;
        int index = 0;
        boolean temp = false;
        byte [] bytes1 = new byte[256];
        List<Byte> list = new ArrayList<>();
        for (int i = 1; i < bytes.length;  i = i + 2) {
            index = bytes[i - 1];
            if (index < 0) index = index + 256;
            contingut = bytes[i];
            if (index == 0){
                list.add(contingut);
            }else if (i < 255){
                escriureBytes(index,contingut,bytes,list);
            } else if (i % 255 == 0){
                bytes1 = new byte[256];
                for (int j = 0; j < bytes1.length; j++) {
                    bytes1[j] = bytes[i + j];
                }
                temp = true;
            }

            if (temp){
                escriureBytes(index,contingut,bytes1,list);
            }
            System.out.println(list);
        }
        ferNetSaLListta(list,os);
    }

    private static void escriureBytes(int index, byte contingut, byte[] bytes,List<Byte> list){
         int index1 = (index * 2);
        List<Byte> li = new ArrayList<>();
        try {
            while (index1 != 0){
                li.add(contingut);
                contingut = bytes[index1 - 1];
                index1 = bytes[index1 - 2];
                index1 = index1 * 2;
            }
            li.add(contingut);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(index1);
        }

        for (int i = li.size() - 1; i >= 0; i--) {
            list.add(li.get(i));
        }
    }
}
