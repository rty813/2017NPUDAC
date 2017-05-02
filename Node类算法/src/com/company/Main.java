package com.company;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Main {
    public static int N = 20;
    private static int[] target = new int[N];
    private static int n;
    private static int methods = 0;
    private static ArrayList<Node> que = new ArrayList<Node>();


    private static boolean check(int[] queue){
        for (int i = 0; i < n; i++){
            if (queue[i] != target[i]){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Node node = new Node();
        int[] nodeData = node.getData();
        n = in.nextInt();
        int[] max = new int[N];
        for (int i = 0; i < n; i++){
            max[i] = in.nextInt();
        }
        for (int i = 0; i < n; i++){
            nodeData[i] = in.nextInt();
        }
        for (int i = 0; i < n; i++){
            target[i] = in.nextInt();
        }

        que.add(node);

        int tail = 1;
        int t = 0;
        int flag = 1;
        int step = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
        map.put(node.hashCode(), 0);
        long startTime = System.currentTimeMillis();

        while (true){
            if (step == 13){
                System.out.println();
            }

//            这一步已经搜寻完毕
            if (t == flag){
                flag = tail;
                step++;
                map.putAll(tempMap);
                tempMap = new HashMap<Integer, Integer>();
                System.gc();
                System.out.println(String.format("head=%d\tflag=%d\tstep=%d", t, flag, step));
            }

//            队列空，无解
            if (t == tail){
                System.out.println(String.format("Over!\nTotal Status=%d", tail));
                RandomAccessFile resultAccessFile = new RandomAccessFile("result.txt", "rw");
                resultAccessFile.seek(resultAccessFile.length());
                resultAccessFile.write(String.format("Total Status=%d\r\n", tail).getBytes());
                resultAccessFile.close();
                break;
            }

//            找到解
            if (check(que.get(t).getData())){
                File file = new File("result.txt");
                file.delete();
                RandomAccessFile resultAccessFile = new RandomAccessFile("result.txt", "rw");
                dfs(t, 0, resultAccessFile);
                System.out.println("Success! step = " + step + " & Methods = " + methods + " & Status = " + tail);
                resultAccessFile.write(("Success! step = " + step +  " & Methods = " + methods + " & Status = " + tail).getBytes());
                resultAccessFile.close();
                long endTime = System.currentTimeMillis();
                System.out.println("耗时：" + (endTime - startTime));
                break;
            }

//            bfs
            node = que.get(t);
            nodeData = node.getData();
            for (int i = 0; i < n; i++){
                if (nodeData[i] == 0){
                    continue;
                }
                for (int j = 0; j < n; j++){
                    if ((j == i) || nodeData[j] == max[j]){
                        continue;
                    }
                    int[] temp = nodeData.clone();
                    if (nodeData[j] + nodeData[i] > max[j]){
                        temp[j] = max[j];
                        temp[i] = nodeData[i] - (max[j] - nodeData[j]);
                    }
                    else{
                        temp[j] = nodeData[j] + nodeData[i];
                        temp[i] = 0;
                    }

                    if (map.containsKey(Arrays.hashCode(temp))){
                        continue;
                    }

                    if (tempMap.containsKey(Arrays.hashCode(temp))){
                        int pos = tempMap.get(Arrays.hashCode(temp));
                        node = que.get(pos);
                        node.addPoint(t);
                        continue;
                    }
                    tempMap.put(Arrays.hashCode(temp), tail);
                    node = new Node();
                    node.setData(temp);
                    node.addPoint(t);
                    que.add(node);
                    tail++;
                }
            }
            t++;
        }
    }

    private static ArrayList<int[]> list = new ArrayList<int[]>();
    private static void dfs(int t, int step, RandomAccessFile resultAccessFile) throws IOException {
        ArrayList<Integer> point = que.get(t).getPoint();
        int[] data = que.get(t).getData();
        if (step >= list.size()){
            list.add(data);
        }
        else{
            list.set(step, data);
        }

        if (point.size() != 0){
            for(int i : point){
                dfs(i, step + 1, resultAccessFile);
            }
        }
        else{
            methods++;
            resultAccessFile.write(String.format("Method %d:\r\n", methods).getBytes());
            for(int i = list.size() - 1; i >= 0; i--){
                for (int j = 0; j < n; j++){
                    resultAccessFile.write(String.format("%d ", list.get(i)[j]).getBytes());
                }
                resultAccessFile.write("\r\n".getBytes());
            }
            resultAccessFile.write("\r\n".getBytes());
        }
    }
}
