package SprintFour;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {
    AbstractGame game;
    public Computer(AbstractGame _game) {
        game = _game;
    }

    String gptResponseRegex = "[^soSO0123456789]*";

    private int coordinatesToCellId(int row, int column) {
        int id = row * game.getBoardSize() + column + 1;
        if (id >= 1 && id <= game.getBoardSize() * game.getBoardSize()) {
            return id;
        } else {
            return -1;
        }
    }

    public int[] getMove() {
        int[] move = new int[]{0, 0, 0};
        String str = getMoveFromOpenAI();
        if(str.charAt(0) == 'S' || str.charAt(0) == 's') {
            move[2] = 1;
        } else {
            move[2] = 0;
        }

        int cellId = Integer.parseInt(str.substring(1));
        move[0] = (cellId - 1) / game.getBoardSize();
        move[1] = (cellId - 1) % game.getBoardSize();
        System.out.println("move[0] = " + move[0] + " move[1] = " + move[1] + " move[2] = " + move[2]);
        return move;
    }


    public String boardToEasyBoard() {
        String gridStr = "";
        gridStr += "[";
        for (int i = 0; i < game.getBoardSize(); i++) {
            if (i == 0) {
                gridStr += "(";
            } else {
                gridStr += ", (";
            }
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (j == 0) {
                    switch (game.getCell(i, j)) {
                        case 0:
                            gridStr += Integer.toString(coordinatesToCellId(i, j));
                            break;
                        case 1:
                            gridStr += "S";
                            break;
                        case 2:
                            gridStr += "O";
                            break;
                    }
                } else {
                    switch (game.getCell(i, j)) {
                        case 0:
                            gridStr += ", " + Integer.toString(coordinatesToCellId(i, j));
                            break;
                        case 1:
                            gridStr += ", S";
                            break;
                        case 2:
                            gridStr += ", O";
                            break;
                    }
                }
            }
            gridStr += ")";
        }
        gridStr += "]";
        System.out.println(gridStr);
        return gridStr;
    }

    //Inspiration: https://rollbar.com/blog/how-to-use-chatgpt-api-with-java/
    public String getMoveFromOpenAI() {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = System.getenv("GPT_API_KEY");

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body
            String body = "{\n" +
                    "  \"model\": \"gpt-4o\",\n" +
                    "  \"messages\": [\n" +
                    "    {\n" +
                    "      \"role\": \"system\",\n" +
                    "      \"content\": [\n" +
                    "        {\n" +
                    "          \"type\": \"text\",\n" +
                    "          \"text\": \"Stragegy Information: The goal of SOS is to form sequences of \\\"SOS\\\"s in a column, row, or diagonal. If there is any opportunity to create an SOS, then that is the best move. Otherwise, you want to avoid setting up your opponent to make an SOS.\\nData information: The following 2 dimensional array contains the state of an ongoing SOS game. If an element is a number, then that means it is an empty cell. If an element is an S or and O, then it is a filled cell if with an S or an O. each inner array represents a row. Make the best move, and represent the move in the following format: [S/O][cell id]. Do not include any other text in your response. Ensure that the [cell id] you select is listed as an integer, not S/O, within the array. Don't forget to play defense if you can't immediately form a new SOS!\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"role\": \"user\",\n" +
                    "      \"content\": [\n" +
                    "        {\n" +
                    "          \"type\": \"text\",\n" +
                    "          \"text\": \"" + boardToEasyBoard() + "\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"response_format\": {\n" +
                    "    \"type\": \"text\"\n" +
                    "  },\n" +
                    "  \"temperature\": 1,\n" +
                    "  \"max_completion_tokens\": 2048,\n" +
                    "  \"top_p\": 1,\n" +
                    "  \"frequency_penalty\": 0,\n" +
                    "  \"presence_penalty\": 0,\n" +
                    "  \"store\": true\n" +
                    "}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            String extractedResponse = extractMessageFromJSONResponse(response.toString());
            System.out.println("RJD ER: " + extractedResponse);
            Pattern pattern = Pattern.compile(gptResponseRegex);
            Matcher matcher = pattern.matcher(extractedResponse);
            String filteredResponse = matcher.replaceAll("");
            System.out.println("RJD FR: " + filteredResponse);
            return filteredResponse;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content")+ 11;

        int end = response.indexOf("\"", start);

        return response.substring(start, end);

    }
}
