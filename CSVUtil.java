import java.io.*;
import java.util.*;

public class CSVUtil {
    public static void saveProcessesCSV(File f, List<Process> processes) throws IOException {
        try (PrintWriter pw = new PrintWriter(f)) {
            pw.println("ID,Arrival,Burst,Priority");
            for (Process p : processes) {
                pw.println(p.id+","+p.arrival+","+p.burst+","+p.priority);
            }
        }
    }

    public static List<Process> loadProcessesCSV(File f) throws IOException {
        List<Process> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine(); // skip header
            while ((line=br.readLine())!=null) {
                String[] t = line.split(",");
                list.add(new Process(Integer.parseInt(t[0]), Integer.parseInt(t[1]),
                        Integer.parseInt(t[2]), Integer.parseInt(t[3])));
            }
        }
        return list;
    }
}
