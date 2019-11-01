import java.io.*;

public class FileUtils {

    public static void largeFileIO(String inputFile) {
        try {
            BufferedInputStream bis = new BufferedInputStream((new FileInputStream(new File(inputFile))));

            BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);

            while (reader.ready()) {
                String line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String msd = "\""+111+"\"";
        System.out.println(msd);
        System.out.println(msd.replaceAll("\"",""));

    }


}
