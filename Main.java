import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Source: ");
        String directorypath = scanner.nextLine();
        System.out.println();

        Path directory = Paths.get(directorypath);

        try {
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(filepath -> sort(filepath.toString(), directorypath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sort(String path, String directorypath) {

        try {
            BasicFileAttributes attr = Files.readAttributes(Path.of(path), BasicFileAttributes.class);
            FileTime modifiedTime = attr.lastModifiedTime();
            SimpleDateFormat dateFormat;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(new Date(modifiedTime.toMillis()));

            File f = new File(directorypath + "/" + dateStr);

            cp(path, f.toString());
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    static void cp(String source, String target) {
        Path sourcedir = Paths.get(source);
        Path targetdir = Paths.get(target);
        try {
            Files.createDirectories(targetdir);
            Files.copy(sourcedir, targetdir.resolve(sourcedir.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println(source + " moved to " + target);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }
}
