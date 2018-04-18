import java.io.File;

public class Rename {
    public static void main(String[] args) {
        String filePath = "E:\\Comslider\\On Campus Job\\Reo\\Rename\\";
        String oldfileName;
        String newfileName;
        File file1;
        File file2;
        for (int i = 31; i > 0; i--){
            for (int j = 4; j > 0; j--){
                oldfileName = "D201603" + String.format("%02d", i) + "A" + String.format("%02d", j) + ".txt";
                newfileName = "D201603" + String.format("%02d", i) + "A" + String.format("%02d", j+5) + ".txt";
                file1 = new File(filePath + oldfileName);
                file2 = new File(filePath + newfileName);
                if (file1.exists()){
                    boolean success = file1.renameTo(file2);
                    if (!success){
                        System.out.println("File was not renamed");
                    }
                }
            }
        }
    }
}
