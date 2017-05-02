package com.npu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    private static final int N = 5;
    private static int n;
    private static long tail;
    private static int[] queue = new int[N];
    private static int[] queue2 = new int[N];
    private static int step = 0;
    private static int[] target = new int[N];
    private static int[] max = new int[N];
    private static long flag = 1;
    private static int minStep = -1;
    private static int methodNum = 0;

    private static boolean check(){
        for (int i = 0; i < n; i++){
            if (queue[i] != target[i]){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            File file = new File("1.txt");
            file.delete();
            file = new File("result.txt");
            file.delete();
            RandomAccessFile accessFile = new RandomAccessFile("1.txt", "rw");
            Scanner in = new Scanner(System.in);
            n = in.nextInt();
            for (int i = 0; i < n; i++) {
                max[i] = in.nextInt();
            }
            for (int i = 0; i < n; i++) {
                queue[i] = in.nextInt();
                accessFile.write(String.format("%d ", queue[i]).getBytes());
            }
            accessFile.write("-1\r\n".getBytes());
            for (int i = 0; i < n; i++) {
                target[i] = in.nextInt();
            }
//            System.exit(0);
            long startTime = System.currentTimeMillis();
            tail = 1;
            accessFile.seek(0);
            int t = 0;
            while (true){
                if (t == flag){
                    step++;
                    flag = tail;
                    System.out.println(String.format("head=%d\ttail=%d\tstep=%d\tflag=%d", t, tail, step, flag));
                }
                long prePos = accessFile.getFilePointer();
                String line = accessFile.readLine();
                if ((line == null) || (line.equals(""))){
                    break;
                }
                String[] lines = line.split(" ");
                for (int i = 0; i <= n; i++){
                    queue[i] = Integer.parseInt(lines[i]);
                }
                long pos = accessFile.getFilePointer();
                int[] preQue = new int[N];

                if (queue[n] != -1){
                    accessFile.seek(queue[n]);
                    lines = accessFile.readLine().split(" ");
                    for (int i = 0; i <= n; i++){
                        preQue[i] = Integer.parseInt(lines[i]);
                    }
                }

                if ((step > minStep) && (minStep != -1)){
                    RandomAccessFile resultFile = new RandomAccessFile("result.txt", "rw");
                    resultFile.seek(resultFile.length());
                    System.out.println("Over! MethodNum = " + methodNum);
                    System.out.println(String.format("MinStep = %d", minStep));
                    resultFile.write(String.format("Over! MethodNum = %d\r\n", methodNum).getBytes());
                    resultFile.write(String.format("MinStep = %d", minStep).getBytes());
                    resultFile.close();
                    long endTime = System.currentTimeMillis();
                    System.out.println("耗时：" + (endTime - startTime));
                    break;
                }

                if (check()){
                    if ((step > minStep) && (minStep == -1)){
                        minStep = step;
                    }
                    methodNum++;
                    RandomAccessFile resultFile = new RandomAccessFile("result.txt", "rw");
                    resultFile.seek(resultFile.length());
                    resultFile.write(("No" + methodNum).getBytes());
                    resultFile.write("\r\n".getBytes());
                    System.out.println("No" + methodNum);
                    long temp = prePos;
                    while (temp != -1){
                        System.out.println(line);
                        resultFile.write((line + "\r\n").getBytes());
                        temp = Integer.parseInt(lines[n]);
                        if (temp == -1){
                            break;
                        }
                        accessFile.seek(temp);
                        line = accessFile.readLine();
                        lines = line.split(" ");
                    }
                    resultFile.write("\r\n".getBytes());
                    resultFile.close();
                    System.out.println();
//                    new Scanner(System.in).nextLine();
                }

                for (int i = 0; i < n; i++){
                    if (queue[i] == 0){
                        continue;
                    }
                    for (int j = 0; j < n; j++){
                        if ((j == i) || (queue[j] == max[j])){
                            continue;
                        }
                        for (int k = 0; k < n; k++){
                            queue2[k] = queue[k];
                        }
                        if (queue[j] + queue[i] > max[j]){
                            queue2[j] = max[j];
                            queue2[i] = queue[i] - (max[j] - queue[j]);
                        }
                        else{
                            queue2[j] = queue[j] + queue[i];
                            queue2[i] = 0;
                        }
//                        queue2存储着与queue能一步到达的目标状态
//                        if (Arrays.equals(queue2, preQue)){
//                            continue;
//                        }
                        int k;
                        for (k = 0; k < n; k++){
                            if (queue2[k] != preQue[k]){
                                break;
                            }
                        }
                        if (k == n){
                            continue;
                        }

                        tail++;1
                        if (tail % 200000 == 0){
                            System.out.println(String.format("head=%d\ttail=%d\tstep=%d\tflag=%d", t, tail, step, flag));
                        }
                        accessFile.seek(accessFile.length());
                        for (k = 0; k < n; k++){
                            accessFile.write(String.format("%d ", queue2[k]).getBytes());
                        }
                        accessFile.write(String.format("%d\r\n", prePos).getBytes());

//                    System.out.println(tail);
                    }
                }
                accessFile.seek(pos);
                t++;
            }
            accessFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
