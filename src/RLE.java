import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RLE {
    public static void compress(InputStream is, OutputStream os) {
        try {
            byte[] bytes = is.readAllBytes();
            byte cont = 0;
            int contNum = 0;
            boolean si;

            for (int i = 0 ; i < bytes.length ; i++) {
                if (i >= 1  && bytes[i - 1] == bytes[i]){
                    contNum++;
                    si = true;
                    if (contNum == 257){
                            os.write(cont);
                            cont = 0;
                            contNum = 0;
                    }
                }else {
                    si = false;
                }
                    if (contNum > 1 && si) {
                        cont++;
                    } else {
                        if (cont > 0) {
                            os.write(cont);
                            cont = 0;
                            contNum = 0;
                            if (i + 1 == bytes.length  && bytes[i - 1] != bytes[i]) os.write(bytes[i]);
                        } else {
                            os.write(bytes[i]);
                            if ((i >= 1 && bytes[i - 1] == bytes[i]) && ((i + 1 < bytes.length && bytes[i] != bytes[i + 1])
                                    || i + 1 == bytes.length)) {
                                os.write(cont);
                                cont = 0;
                                contNum = 0;
                            }
                        }

                    }

                }
            if (cont > 0){
                os.write(cont);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void decompress(InputStream is, OutputStream os) {
        try {
            byte[] bytes = is.readAllBytes();
            List<Byte> li = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
