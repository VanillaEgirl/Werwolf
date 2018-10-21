package root.ResourceManagement;

import root.Spieler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    void createFolderInAppdata() {
        File theDir = new File(ResourcePath.SAVE_FILE_PATH);

        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                System.out.println("Programm does not have the permisson to create a folder in Appdata.");
            }
        }
    }

    CompositionDto readComposition(String filePath) {
        File file = new File(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            CompositionDto compositionDto = new CompositionDto();
            compositionDto.spieler = readList(br);
            compositionDto.hauptrollen = readList(br);
            compositionDto.bonusrollen = readList(br);

            return compositionDto;
        } catch (IOException e) {
            System.out.println("Something went wrong while reading the composition file.");
            return null;
        }
    }

    boolean writeComposition(String filePath, List<String> spieler, List<String> hauptrollen, List<String> bonusrollen) {
        File file = createNewFile(filePath);

        if (file == null) {
            return false;
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writeArrayList(writer, spieler);
            writeArrayList(writer, hauptrollen);
            writeArrayList(writer, bonusrollen);

            writer.flush();
        } catch (IOException e) {
            System.out.println("Something went wrong while writing the composition file.");
            return false;
        }

        return true;
    }

    GameDto readGame(String filePath) {
        GameDto gameDto = new GameDto();
        File file = new File(filePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;

            line = br.readLine();
            int numberOfPlayers = Integer.parseInt(line);

            for (int i = 0; i < numberOfPlayers; i++) {
                line = br.readLine();
                String[] fractals = line.split(" ");

                String name = fractals[0].replace("*", " ");
                String hauptrolleString = fractals[1].replace("*", " ");
                String bonusrolleString = fractals[2].replace("*", " ");

                PlayerDto playerDto = new PlayerDto(name, hauptrolleString, bonusrolleString);
                gameDto.players.add(playerDto);
            }

            gameDto.compositionDto.hauptrollen = readList(br);
            gameDto.compositionDto.bonusrollen = readList(br);

            return gameDto;
        } catch (IOException e) {
            System.out.println("Something went wrong while reading the game file");
            return null;
        }
    }

    boolean writeGame(String filePath, List<Spieler> spieler, List<String> hauptrollenLeft, List<String> bonusrollenLeft) {
        File file = createNewFile(filePath);

        if (file == null) {
            return false;
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            ArrayList<String> compositionStrings = new ArrayList<>();

            for (Spieler currentSpieler : spieler) {
                compositionStrings.add(buildPlayerString(currentSpieler));
            }

            writeArrayList(writer, compositionStrings);
            writeArrayList(writer, hauptrollenLeft);
            writeArrayList(writer, bonusrollenLeft);

            writer.flush();
        } catch (IOException e) {
            System.out.println("Something went wrong while writing the game file");
            return false;
        }

        return true;
    }

    private File createNewFile(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File could not be created at location: " + filePath);
            return null;
        }

        return file;
    }


    private String buildPlayerString(Spieler spieler) {
        String name = spieler.name.replace(" ", "*");
        String hauptrolle = spieler.hauptrolle.name.replace(" ", "*");
        String bonusrolle = spieler.bonusrolle.name.replace(" ", "*");
        return name + " " + hauptrolle + " " + bonusrolle;
    }

    private void writeArrayList(Writer writer, List<String> arrayToWrite) throws IOException {
        int numberOfLines = arrayToWrite.size();
        writer.write(Integer.toString(numberOfLines));
        writer.write("\n");
        for (String line : arrayToWrite) {
            writer.write(line);
            writer.write("\n");
        }
    }

    private ArrayList<String> readList(BufferedReader br) throws IOException {
        ArrayList<String> listStrings = new ArrayList<>();
        String line = br.readLine();
        int numberOfBonusrollen = Integer.parseInt(line);

        for (int i = 0; i < numberOfBonusrollen; i++) {
            line = br.readLine();
            listStrings.add(line);
        }

        return listStrings;
    }
}
