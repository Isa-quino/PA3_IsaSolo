import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CsvFlex {
    public static class Row {
        private final Map<String,String> map;
        public Row(Map<String,String> map){ this.map = map; }
        public String get(String key){ return map.getOrDefault(key, "").trim(); }
        public boolean has(String key){ return map.containsKey(key); }
    }

    public static List<Row> read(String fileName) throws FileNotFoundException {
        List<Row> rows = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(fileName))) {
            if (!sc.hasNextLine()) return rows;
            String header = sc.nextLine();
            String[] headers = header.split("\\s*,\\s*|\\t+");
            for (int i=0;i<headers.length;i++) headers[i]=headers[i].trim();

            while (sc.hasNextLine()){
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s*,\\s*|\\t+",-1);
                Map<String,String> map = new HashMap<>();
                for (int i=0;i<headers.length;i++){
                    String key = headers[i];
                    String val = (i<parts.length)? parts[i] : "";
                    map.put(key, val);
                }
                rows.add(new Row(map));
            }
        }
        return rows;
    }

    public static int toInt(String s, int def){ try { return Integer.parseInt(s.trim()); } catch(Exception e){ return def; } }
    public static double toDouble(String s, double def){ try { return Double.parseDouble(s.trim()); } catch(Exception e){ return def; } }
    public static boolean toBool(String s, boolean def){ 
        if (s==null) return def;
        String t = s.trim();
        if (t.equalsIgnoreCase("true")||t.equalsIgnoreCase("yes")) return true;
        if (t.equalsIgnoreCase("false")||t.equalsIgnoreCase("no")) return false;
        return def;
    }
}
