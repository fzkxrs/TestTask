package testTask;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    Map<String, List<Integer>> directMap = new HashMap<>();
    List<List<String>> container = new ArrayList<>();
    List<Set<Integer>> groups = new ArrayList<>();
    Set<String> proceed = new HashSet<>();

    public static void main(String[] args) throws IOException {
        String path = "lng.txt";
        if (args.length > 0) {
            path = args[0];
        }
        Main main = new Main();
        main.findGroups(path);
    }

    public void findGroups(String path) throws IOException {
        String regex = "\"[0-9]*\"[0-9]*\"";
        Pattern isIncorrect = Pattern.compile(regex);
        String row;
        FileReader in = new FileReader(path);
        BufferedReader br = new BufferedReader(in);

        while ((row = br.readLine()) != null) {
            if(row.equals("")) continue;
            Matcher exclude = isIncorrect.matcher(row);
            if (exclude.find()) continue;
            container.add(List.of(row.split(";")));
        }
        br.close();
        container = container.stream().distinct().collect(Collectors.toList());

        for (int i = 0; i < container.size(); i++) {
            for (int j = 0; j < container.get(i).size(); j++) {
                if (container.get(i).get(j).equals("\"\"")) continue;
                List<Integer> list = new ArrayList<>();
                String pair = j + container.get(i).get(j);
                if (directMap.containsKey(pair)) {
                    list = directMap.get(pair);
                    list.add(i);
                    continue;
                }
                list.add(i);
                directMap.put(pair, list);
            }
        }

        for (Map.Entry<String, List<Integer>> entry : directMap.entrySet()) {
            if (proceed.contains(entry.getKey())) continue;
            Set<Integer> group = new HashSet<>();
            groups.add(recurse(entry.getValue(), group));
        }

        long groupsMoreThanOne = groups.stream().filter(e -> e.size() > 1).count();
        groups = groups.stream().sorted(Comparator.comparing(Set::size)).collect(Collectors.toList());
        Collections.reverse(groups);
        printGroups(groupsMoreThanOne);
    }

    public Set<Integer> recurse(List<Integer> rows, Set<Integer> group) {
        if (rows == null) return group;
        for (Integer row : rows) {
            for (int i = 0; i < container.get(row).size(); i++) {
                String col = i + container.get(row).get(i);
                if (proceed.contains(col)) continue;
                proceed.add(col);
                group = recurse(directMap.get(col), group);
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
                Iterator<String> iterator = container.get(num).iterator();
                while (iterator.hasNext()) {
                    write.append(iterator.next());
                    if (iterator.hasNext()) write.append(";");
                }
                write.append('\n');
            }
            writer.write(write.toString());
        }
        writer.close();
        System.out.println("Групп более чем с одним элементом: " + groupsMoreThanOne);
    }

}
