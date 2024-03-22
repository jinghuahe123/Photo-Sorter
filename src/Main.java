import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1 ) {
            System.out.println("Please enter only one path.");
            System.exit(0);
        }

        String path = args[0];

        Scanner scanner = new Scanner(System.in);

        System.out.println("You have selected path: " + path);
        System.out.print("Type 'n' to exit, else type anything to continue: ");
        String input = scanner.nextLine();

        if (Objects.equals(input, "n")) {
            System.out.println("You have chose to exit. Process will now quit.");
            System.exit(0);
        }

        Path directory = Paths.get(path);

        try {
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> sort(filePath.toString(), path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sort(String filePath, String directoryPath) {

        try {
            BasicFileAttributes attr = Files.readAttributes(Path.of(filePath), BasicFileAttributes.class);
            FileTime modifiedTime = attr.lastModifiedTime();
            SimpleDateFormat dateFormat;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(new Date(modifiedTime.toMillis()));

            File f = new File(directoryPath + "/" + dateStr);

            cp(filePath, f.toString());
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    static void cp(String source, String target) {
        Path sourceDir = Paths.get(source);
        Path targetDir = Paths.get(target);
        try {
            Files.createDirectories(targetDir);
            Files.copy(sourceDir, targetDir.resolve(sourceDir.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println(source + " copied to " + target);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }
}