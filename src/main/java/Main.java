import testTask.TestTask;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String path = "lng.txt";
        if (args.length > 0) {
            path = args[0];
        }
        TestTask task = new TestTask();
        task.findGroups(path);
    }

}
