import java.io.*;

/**
 * Created by Mr.Zero on 2016/10/20.
 */
public class IOHelper {
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    public IOHelper() {
        try {
            reader = new BufferedReader(new FileReader(new File("input.c")));
            writer = new BufferedWriter(new FileWriter(new File("output.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int read() throws IOException {
        return reader.read();
    }

    public void write(Token token) {
        String str = token.toString();
        try {
            writer.write(str, 0, str.length());
            System.out.print(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeIO(){
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writer!=null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
