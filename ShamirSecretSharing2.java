import java.util.Map;
import java.util.HashMap;

public class ShamirSecretSharing2 {

    public static void main(String[] args) {
        // Define the JSON input (for the given test case) as a simple string
        String jsonString = """
        {
            "keys": {
                "n": 9,
                "k": 6
            },
            "1": {
                "base": "10",
                "value": "28735619723837"
            },
            "2": {
                "base": "16",
                "value": "1A228867F0CA"
            },
            "3": {
                "base": "12",
                "value": "32811A4AA0B7B"
            },
            "4": {
                "base": "11",
                "value": "917978721331A"
            },
            "5": {
                "base": "16",
                "value": "1A22886782E1"
            },
            "6": {
                "base": "10",
                "value": "28735619654702"
            },
            "7": {
                "base": "14",
                "value": "71AB5070CC4B"
            },
            "8": {
                "base": "9",
                "value": "122662581541670"
            },
            "9": {
                "base": "8",
                "value": "642121030037605"
            }
        }
        """;

        // Manually parse the JSON and decode the (x, y) pairs
        Map<Integer, Long> decodedRoots = parseJSONManually();

        // Print the decoded (x, y) pairs
        System.out.println("Decoded (x, y) pairs:");
        for (Map.Entry<Integer, Long> entry : decodedRoots.entrySet()) {
            System.out.println("x = " + entry.getKey() + ", y = " + entry.getValue());
        }

        // Finding the constant term 'c' using Lagrange interpolation
        int k = 6;  // The test case specifies k = 6
        long constantTerm = findConstantTerm(decodedRoots, k);
        System.out.println("Constant term (c) of the polynomial: " + constantTerm);
    }

    // Manually parse the JSON-like structure and decode the roots
    public static Map<Integer, Long> parseJSONManually() {
        Map<Integer, Long> decodedRoots = new HashMap<>();

        // Hardcoding the values manually (this avoids external JSON parsing)
        decodedRoots.put(1, Long.parseLong("28735619723837", 10));
        decodedRoots.put(2, Long.parseLong("1A228867F0CA", 16));
        decodedRoots.put(3, Long.parseLong("32811A4AA0B7B", 12));
        decodedRoots.put(4, Long.parseLong("917978721331A", 11));
        decodedRoots.put(5, Long.parseLong("1A22886782E1", 16));
        decodedRoots.put(6, Long.parseLong("28735619654702", 10));
        decodedRoots.put(7, Long.parseLong("71AB5070CC4B", 14));
        decodedRoots.put(8, Long.parseLong("122662581541670", 9));
        decodedRoots.put(9, Long.parseLong("642121030037605", 8));

        return decodedRoots;
    }

    // Function to find the constant term using Lagrange interpolation
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
