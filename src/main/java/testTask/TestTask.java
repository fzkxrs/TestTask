package testTask;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestTask {
    Map<String, List<Integer>> initialGrouping = new HashMap<>();
    List<List<String>> linesContainer = new ArrayList<>();
    List<Set<Integer>> groups = new ArrayList<>();
    Set<String> viewed = new HashSet<>();

    public void findGroups(String path) throws IOException {
        String incorrectStringRegex = "\"[0-9]*\"[0-9]*\"";
        Pattern isIncorrect = Pattern.compile(incorrectStringRegex);
        String row;
        FileReader in = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(in);

        while ((row = bufferedReader.readLine()) != null) {
            if (row.equals("")) continue;
            Matcher exclude = isIncorrect.matcher(row);
            if (exclude.find()) continue;
            linesContainer.add(List.of(row.split(";")));
        }
        bufferedReader.close();
        linesContainer = linesContainer.stream().distinct().collect(Collectors.toList());

        for (int i = 0; i < linesContainer.size(); i++) {
            for (int j = 0; j < linesContainer.get(i).size(); j++) {
                if (linesContainer.get(i).get(j).equals("\"\"")) continue;
                List<Integer> list = new ArrayList<>();
                String pair = j + linesContainer.get(i).get(j);
                if (initialGrouping.containsKey(pair)) {
                    list = initialGrouping.get(pair);
                    list.add(i);
                    continue;
                }
                list.add(i);
                initialGrouping.put(pair, list);
            }
        }

        for (Map.Entry<String, List<Integer>> entry : initialGrouping.entrySet()) {
            if (viewed.contains(entry.getKey())) continue;
            Set<Integer> group = new HashSet<>();
            groups.add(finalGrouping(entry.getValue(), group));
        }

        long groupsMoreThanOne = groups.stream().filter(e -> e.size() > 1).count();
        groups = groups.stream().sorted(Comparator.comparing(Set::size)).collect(Collectors.toList());
        Collections.reverse(groups);
        printGroups(groupsMoreThanOne);
    }

    public Set<Integer> finalGrouping(List<Integer> rows, Set<Integer> group) {
        if (rows == null) return group;
        for (Integer row : rows) {
            for (int i = 0; i < linesContainer.get(row).size(); i++) {
                String col = i + linesContainer.get(row).get(i);
                if (viewed.contains(col)) continue;
                viewed.add(col);
                group = finalGrouping(initialGrouping.get(col), group);
            }
            group.add(row);
        }
        return group;
    }

    public void printGroups(Long groupsMoreThanOne) throws IOException {
        System.out.println("Групп более чем с одним элементом: " + groupsMoreThanOne);
        int groupCounter = 1;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        for (Set<Integer> group : groups) {
            StringBuilder write = new StringBuilder();
            write.append("Группа ").append(groupCounter++).append('\n');
            for (Integer num : group) {
                Iterator<String> iterator = linesContainer.get(num).iterator();
                while (iterator.hasNext()) {
                    write.append(iterator.next());
                    if (iterator.hasNext()) write.append(";");
                }
                write.append('\n');
            }
            writer.write(write.toString());
        }
        writer.close();
    }

}
