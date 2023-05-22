import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RLE {
    public static void compress(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = is.readAllBytes();
        int cont = 0;
        int contNum = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (i >= 1 && bytes[i - 1] == bytes[i]) {
                contNum++;
                if (contNum == 257) {
                    os.write(cont);
                    cont = 0;
                    contNum = 0;
                }
            }
            if (contNum > 1 && bytes[i - 1] == bytes[i]) {
                cont++;
            } else {
                if (cont != 0) {
                    os.write(cont);
                    os.write(bytes[i]);
                    cont = 0;
                    contNum = 0;
                } else {
                    os.write(bytes[i]);
                    if (seHaDeAfegirElComptador(bytes, i)) {
                        os.write(cont);
                        contNum = 0;
                    }
                }
            }
        }
        if (cont > 0) {
            os.write(cont);
        }
        os.close();
    }

    private static boolean seHaDeAfegirElComptador(byte[] bytes, int i) {
        return (i >= 1 && bytes[i - 1] == bytes[i]) && ((i + 1 < bytes.length && bytes[i] != bytes[i + 1])
                || i + 1 == bytes.length);
    }

    public static void decompress(InputStream is, OutputStream os) {
        try {
            byte[] bytes = is.readAllBytes();
            for (int i = 0; i < bytes.length; i++) {
                if (i > 0 && bytes[i - 1] == bytes[i] && i + 1 < bytes.length) {
                    os.write(bytes[i]);
                    System.out.println(bytes[i]);
                    afegirNombres(bytes[i], bytes[i + 1], os);
                    i++;
                } else {
                    os.write(bytes[i]);
                    System.out.println(bytes[i]);
                }
            }
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void afegirNombres(byte aByte, byte aByte1, OutputStream os) throws IOException {
        int temp = aByte1 & 0xff;
        for (int i = 0; i < temp; i++) {
            os.write(aByte);
            System.out.println(aByte);
        }
    }
}
