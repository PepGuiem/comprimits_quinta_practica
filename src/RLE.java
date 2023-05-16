import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RLE {
    public static void compress(InputStream is, OutputStream os) {
        try {
            byte[] bytes = is.readAllBytes();
            List<Byte> li = new ArrayList<>();
            byte cont = 0;
            boolean esLaMateixaLletra;

            for (int i = 0 ; i < bytes.length ; i++) {
                esLaMateixaLletra = i >= 2  && bytes[i - 1] == bytes[i] ;
                if (esLaMateixaLletra){
                    cont++;
                }else{
                    if (cont > 0){
                        li.add(cont);
                        cont = 0;
                    }else {
                        li.add(bytes[i]);
                    }
                }
                if (i+1 == bytes.length && cont > 0){
                    li.add(cont);
                }
            }
            for (int i = 0; i < li.size(); i++) {
                os.write(li.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void decompress(InputStream is, OutputStream os) {

    }
}
