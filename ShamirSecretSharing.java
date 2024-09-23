import java.util.Map;
import java.util.HashMap;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        // Example JSON input (as a string)
        String jsonString = "{ \"keys\": { \"n\": 4, \"k\": 3 },"
                          + "\"1\": {\"base\": \"10\", \"value\": \"4\"},"
                          + "\"2\": {\"base\": \"2\", \"value\": \"111\"},"
                          + "\"3\": {\"base\": \"10\", \"value\": \"12\"},"
                          + "\"6\": {\"base\": \"4\", \"value\": \"213\"} }";

        // Parse the JSON and decode the (x, y) pairs manually
        Map<Integer, Long> decodedRoots = parseJSON(jsonString);

        // Print the decoded (x, y) pairs
        System.out.println("Decoded (x, y) pairs:");
        for (Map.Entry<Integer, Long> entry : decodedRoots.entrySet()) {
            System.out.println("x = " + entry.getKey() + ", y = " + entry.getValue());
        }

        // Finding the constant term 'c' using Lagrange interpolation
        int k = decodedRoots.size();
        long constantTerm = findConstantTerm(decodedRoots, k);
        System.out.println("Constant term (c) of the polynomial: " + constantTerm);
    }

    // Function to manually parse the JSON string and decode the roots
    public static Map<Integer, Long> parseJSON(String jsonString) {
        Map<Integer, Long> decodedRoots = new HashMap<>();

        // Split the JSON into sections
        String[] parts = jsonString.split("\\},\\\"");
        for (String part : parts) {
            if (part.contains("\"base\":") && part.contains("\"value\":")) {
                // Extract the key, base, and value
                String key = part.split("\\\": \\{")[0].replaceAll("\\D+", "");
                String base = part.split("\"base\": \"")[1].split("\"")[0];
                String value = part.split("\"value\": \"")[1].split("\"")[0];

                // Decode the value using the base
                int x = Integer.parseInt(key);
                long y = Long.parseLong(value, Integer.parseInt(base));
                decodedRoots.put(x, y);
            }
        }

        return decodedRoots;
    }

    // Function to find the constant term using Lagrange Interpolation
    public static long findConstantTerm(Map<Integer, Long> roots, int k) {
        double constantTerm = 0.0;

        // Iterate over each (x_i, y_i) pair
        for (Map.Entry<Integer, Long> iEntry : roots.entrySet()) {
            int xi = iEntry.getKey();
            long yi = iEntry.getValue();

            double li = 1.0;
            // Calculate the Lagrange basis polynomial li(x)
            for (Map.Entry<Integer, Long> jEntry : roots.entrySet()) {
                int xj = jEntry.getKey();
                if (xi != xj) {
                    li *= (0.0 - xj) / (xi - xj);  // li(0) for the constant term
                }
            }

            // Add the contribution of yi * li(0) to the constant term
            constantTerm += yi * li;
        }

        // Return the rounded constant term as a long integer
        return Math.round(constantTerm);
    }
}
