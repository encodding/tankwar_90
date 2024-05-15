package cn.play.freely.game.tank.util;

import cn.play.freely.game.tank.main.Main;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.stream.Collectors;

public class LevelUtils {

    public static List<int[][]> levels = new ArrayList<>();

    public static final String LEVEL_DIR = getLevelDir();

    public static void loadLevels() throws IOException {
        if (Main.class.getResource("").getProtocol().equals("jar")){
            List<Path> result = null;
            try {
                result = getPathsFromResourceJAR(LEVEL_DIR);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            int[][] temp;
            for (Path path : result) {
                String filePathInJAR = path.toString();
                if (filePathInJAR.startsWith("/")) {
                    filePathInJAR = filePathInJAR.substring(1, filePathInJAR.length());
                }
                InputStream is = getFileFromResourceAsStream(filePathInJAR);
                List<String> lines = convertInputStreamToList(is);
                temp  = new int[lines.size()][lines.get(0).length()];
                for (int y = 0; y < lines.size(); y++) {
                    for (int x = 0; x < lines.get(y).length(); x++) {
                        temp[y][x] = lines.get(y).charAt(x) - 48;
                    }
                }
                levels.add(temp);
            }
            return;
        }
        File file = Paths.get(LEVEL_DIR).toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        int[][] temp;
        for (File itemFile : Objects.requireNonNull(file.listFiles())) {
            if (itemFile.isFile()) {
                List<String> lines = Files.readAllLines(itemFile.toPath());
                temp  = new int[lines.size()][lines.get(0).length()];
                for (int y = 0; y < lines.size(); y++) {
                    for (int x = 0; x < lines.get(y).length(); x++) {
                        temp[y][x] = lines.get(y).charAt(x) - 48;
                    }
                }
                levels.add(temp);
            }
        }
    }


    public static void saveLevel(int[][] grid) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                sb.append(grid[y][x]);
            }
            sb.append("\n");
        }
        if (isJarDev()){
            return;
        }
        Files.write(new File(LEVEL_DIR + "/" + levels.size() + ".level").toPath(), sb.toString().getBytes());
        levels.add(grid);
    }


    public static int[][] get(int index) {
        return levels.get(index);
    }

    public static String getLevelDir(){
        if (Main.class.getResource("").getProtocol().equals("jar")){
            return "/levels";
        }
        ClassLoader classLoader = LevelUtils.class.getClassLoader();
        URL resource = classLoader.getResource("levels");
        String path = resource.getPath();
        if (path.startsWith("/")){
            path = path.substring(1,path.length());
        }
        return path;
    }

    private static List<Path> getPathsFromResourceJAR(String folder)
            throws URISyntaxException, IOException {

        List<Path> result;
        String jarPath = LevelUtils.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();
        jarPath = jarPath.replace("\\", "/");
        URI uri = URI.create("jar:file:" + jarPath);
        FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
        return Files.walk(fs.getPath(folder))
                .filter(Files::isRegularFile)
                .sorted()
                .collect(Collectors.toList());

    }

    private static InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = LevelUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public static List<String> convertInputStreamToList(InputStream inputStream) {
        List<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        scanner.close();
        return list;
    }

    public static Boolean isJarDev(){
        return Main.class.getResource("").getProtocol().equals("jar");
    }

}
